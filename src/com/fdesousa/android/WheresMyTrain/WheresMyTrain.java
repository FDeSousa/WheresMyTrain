package com.fdesousa.android.WheresMyTrain;

/*****************************************************************************************************
 *	Copyright (c) 2011 Filipe De Sousa
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *	associated documentation files (the "Software"), to deal in the Software without restriction,
 *	including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *	sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or
 *	substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *	NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *	DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************************/

import com.fdesousa.android.WheresMyTrain.UiElements.LinesSpinnerAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.PlatformsExpListAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.StationsSpinnerAdapter;
import com.fdesousa.android.WheresMyTrain.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.requests.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLStation;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WheresMyTrain extends Activity {
	//	Useful logging variables
	/**	Tag to be used when Logging an exception/error/anything at all							*/
	public static final String TAG = "WheresMyTrain";
	/**	Current instance of this activity. Quick and dirty access to resources, context, etc.	*/
	public static WheresMyTrain INSTANCE;

	//	Fonts to use for all text
	/**	Quicksand Book font to be used for standard dialogs and text, not bold, not italic		*/
	public Typeface book;
	/**	Quicksand Bold font to be used for headers, extra emphasis. Bold as name suggests		*/
	public Typeface bold;

	//	Main view widgets
	/**	Spinner used for selecting Underground Line - colour-coded text choices					*/
	private Spinner linesSpinner;
	/**	Spinner used for selecting tube station from the given Line - colour-coded by Line		*/
	private Spinner stationsSpinner;
	/**	Expandable List used for display Platforms and predicted Trains' destination and timing	*/
	private ExpandableListView predictionsList;

	//	Adapters for Spinners and ExpandableListView widgets
	/**	Instance of the Adapter for LinesSpinner												*/
	private LinesSpinnerAdapter mLineAdapter;
	/**	Instance of the Adapter for StationsSpinner												*/
	private StationsSpinnerAdapter mStationAdapter;
	/**	Instance of the Adapter for PredictionsList												*/
	private PlatformsExpListAdapter mPlatformAdapter;

	//	Custom title bar widgets
	/**	View instance for the custom Title bar. Useful for changing background colours.
	 *	As it is only used for changing background colour, which is generic to all Views,
	 *	we save the type-casting calling for that
	 */
	private View titleBar;
	/**	TextView isntance for the line textview in our custom title bar							*/
	private TextView lineTitle;
	/**	TextView isntance for the station textview in our custom title bar						*/
	private TextView stationTitle;

	//	JSON reading related instances
	/**	Private instance of TflJsonReader for fetching and parsing JSON requests				*/
	private TflJsonReader mJsonR;
	/**	Instance of the currently selected Line for rapid access to code, name and stations		*/
	private SLLine line;
	/**	Instance of the currently selected Station for rapid access to code and name			*/
	private SLStation station;

	//	Anything else
	/**	Current text colour for on-screen widgets to use. Dependant upon the current Line		*/
	private int textColour;
	/**	Simple boolean for determining whether the Custom Title bar window feature is enabled	*/
	private boolean customTitleBarSupported;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//	Requesting window features must be done before anything else, so do it now
		customTitleBarSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.detailed_predictions);

		instantiateVariables();
		setupSpinners();
		if (customTitleBarSupported) setupCustomTitleBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mInflater = getMenuInflater();
		mInflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.more:
		case R.id.set_default:
		case R.id.refresh:
			new RefreshPredictions().execute();
			break;
		}
		return true;
	}

	/**
	 * Convenience method to setup variables in the onCreate.<br/>
	 * Makes onCreate tidier to look at
	 */
	private void instantiateVariables() {
		INSTANCE = this;

		mJsonR = new TflJsonReader(getCacheDir());

		book = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Book.otf");
		bold = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Bold.otf");

		//	For safety, since Bakerloo is shown first, use Bakerloo colours to initialise
		textColour = getResources().getColor(R.color.bakerloo_colour);

		//	Initialise the display widgets
		linesSpinner = (Spinner) findViewById(R.id.lines_spinner);
		stationsSpinner = (Spinner) findViewById(R.id.stations_spinner);
		predictionsList = (ExpandableListView) findViewById(R.id.platforms_list);

	}

	/**
	 * Convenience method to setup the Spinners in the onCreate.<br/>
	 * Makes onCreate tidier to look at
	 */
	private void setupSpinners() {
		//	Call an Asynchronous Task to instantiate the Stations List
		new PrepareStationsList().execute();

		linesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the item selected by setting the second spinner with the correct stations
				line = (SLLine) parent.getItemAtPosition(pos);
				//	Update the data set and reset the adapter
				mStationAdapter = new StationsSpinnerAdapter(line.stations, line.linecode);
				stationsSpinner.setAdapter(mStationAdapter);
			}
			@Override	// Do nothing
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		stationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the selected item by getting detailed predictions for that line & station
				//+	choice, displaying that in the ExpandableListView
				station = (SLStation) parent.getItemAtPosition(pos);
				new RefreshPredictions().execute();
				//	Edit the title bar every time station is changed to reflect the changes
				if (customTitleBarSupported) refreshTitleBar();
			}
			@Override	// Do nothing
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	/**
	 * AsyncTask sub-class to achieve a non-blocking manner in which to get the stations list,
	 * even if the internet connection is a little bit slow on the mobile side.<br/>
	 * Stations List can sometimes take several seconds to process on the server-side alone,
	 * for which amount of time, the UI thread would be blocked otherwise.<br/>
	 * Will not block the UI thread, which is the important part.
	 * @author Filipe De Sousa
	 */
	private class PrepareStationsList extends AsyncTask<Void, Void, SLContainer> {
		@Override
		protected SLContainer doInBackground(Void... params) {
			//	Send the request to prepare the JSON data while other stuff goes on
			mJsonR.prepareStationsList();
			//	Get the prepared JSON data now to fill the spinners
			return mJsonR.getStationsList();
		}
		@Override
		protected void onPostExecute(SLContainer result) {
			//	Initialise the Lines Spinner adapter
			mLineAdapter = new LinesSpinnerAdapter(result.lines);
			//	Set the adapter onto the Lines Spinner
			linesSpinner.setAdapter(mLineAdapter);			
		}
	}
	
	/**
	 * AsyncTask sub-class to achieve a non-blocking manner in which to get detailed predictions,
	 * even if the internet connection is a little bit slow on the mobile side.<br/>
	 * Will not block the UI thread, which is the important part.
	 * @author Filipe De Sousa
	 */
	private class RefreshPredictions extends AsyncTask<Void, Void, DPContainer> {
		@Override
		protected DPContainer doInBackground(Void... params) {
			mJsonR.preparePredictionsDetailed(line.linecode, station.stationcode);
			return mJsonR.getPredictionsDetailed();
		}
		@Override
		protected void onPostExecute(DPContainer result) {
			//	Because of how tfl.php sends predictions data, there is only ever ONE station in stations array
			mPlatformAdapter = new PlatformsExpListAdapter(result.stations.get(0).platforms);
			//	(Re)set the adapter onto the ExpandableListView
			predictionsList.setAdapter(mPlatformAdapter);
		}
	}

	/**
	 * Utility method to do the initial instantiation and setup of the custom title bar
	 */
	private void setupCustomTitleBar() {
		//	Setup the custom title bar
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		//	Get the widget instances
		titleBar = findViewById(R.id.custom_title_bar);
		lineTitle = (TextView) findViewById(R.id.text_line);
		lineTitle.setTypeface(bold);
		stationTitle = (TextView) findViewById(R.id.text_station);
		stationTitle.setTypeface(bold);
		refreshTitleBar();
	}

	/**
	 * Convenience method to refresh the text and colour of the title bar
	 */
	private void refreshTitleBar() {
		//	Set colours and text of widgets
		titleBar.setBackgroundColor(textColour);
		if (line != null) {
			if (line.linename.length() > 20) {
				lineTitle.setText(line.linename.substring(0, 20));
			} else {
				lineTitle.setText(line.linename);				
			}
		}
		if (station != null) {
			if (station.stationname.length() > 20) {
				stationTitle.setText(station.stationname.substring(0, 20));
			} else {
				stationTitle.setText(station.stationname);				
			}
		}
	}

	/**
	 * Simple, easy, dirty static method for showing Toast messages from any class
	 * @param message
	 */
	public final static void displayToast(final String message) {
		Toast.makeText(INSTANCE.getApplicationContext(), message, Toast.LENGTH_LONG);
	}

	/**
	 * Convenience method to get the right colour for the right train line
	 * @param linecode - the ID of the train line to find the colour for
	 * @return the integer colour code for the given train line
	 */
	public int getLineColour(String linecode) {
		Resources r = getResources();
		int colour = 0;

		if (linecode.equals("b"))		colour = r.getColor(R.color.bakerloo_colour);
		else if (linecode.equals("c"))	colour = r.getColor(R.color.central_colour);
		else if (linecode.equals("d"))	colour = r.getColor(R.color.district_colour);
		else if (linecode.equals("h"))	colour = r.getColor(R.color.hammersmith_colour);
		else if (linecode.equals("j"))	colour = r.getColor(R.color.jubilee_colour);
		else if (linecode.equals("m"))	colour = r.getColor(R.color.metropolitan_colour);
		else if (linecode.equals("n"))	colour = r.getColor(R.color.northern_colour);
		else if (linecode.equals("p"))	colour = r.getColor(R.color.piccadilly_colour);
		else if (linecode.equals("v"))	colour = r.getColor(R.color.victoria_colour);
		else if (linecode.equals("w"))	colour = r.getColor(R.color.waterloo_colour);

		return colour;
	}

	/**
	 * Simple getter for text colour
	 * @return integer value that textColour is set to
	 */
	public int getTextColour() {
		return textColour;
	}

	/**
	 * Simple setter for text colour
	 * @param textColour integer value to set textColour to
	 */
	public void setTextColour(int textColour) {
		this.textColour = textColour;
	}
}