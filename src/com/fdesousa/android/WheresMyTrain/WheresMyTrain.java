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

import com.fdesousa.android.WheresMyTrain.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLStation;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
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

	private void instantiateVariables() {
		INSTANCE = this;

		mJsonR = new TflJsonReader(getCacheDir());
		//	Send the request to prepare the JSON data while other stuff goes on
		mJsonR.prepareStationsList();

		book = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Book.otf");
		bold = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Bold.otf");

		//	For safety, since Bakerloo is shown first, use Bakerloo colours to initialise
		textColour = getResources().getColor(R.color.bakerloo_colour);

		//	Initialise the display widgets
		linesSpinner = (Spinner) findViewById(R.id.lines_spinner);
		stationsSpinner = (Spinner) findViewById(R.id.stations_spinner);
		predictionsList = (ExpandableListView) findViewById(R.id.platforms_list);
	}

	private void setupSpinners() {
		//	Get the prepared JSON data now to fill the spinners
		SLContainer sList = mJsonR.getStationsList();
		//	Displays stylised list of TfL lines
		LinesSpinnerAdapter mLineAdapter = new LinesSpinnerAdapter(sList.lines);
		linesSpinner.setAdapter(mLineAdapter);

		linesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the item selected by setting the second spinner with the correct stations
				line = (SLLine) parent.getItemAtPosition(pos);
				StationsSpinnerAdapter mStationAdapter = new StationsSpinnerAdapter(line.stations, line.linecode);
				stationsSpinner.setAdapter(mStationAdapter);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing
			}
		});

		stationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the selected item by getting detailed predictions for that line & station
				//+	choice, displaying that in the ExpandableListView
				station = (SLStation) parent.getItemAtPosition(pos);
				mJsonR.preparePredictionsDetailed(line.linecode, station.stationcode);
				DPContainer sPredictions = mJsonR.getPredictionsDetailed();
				//	Always get the first element in sPredictions.stations as tfl.php only includes 1 station
				//+ per Detailed Predictions request, but places it into an array.
				PlatformsExpListAdapter platformsList = new PlatformsExpListAdapter(sPredictions.stations.get(0).platforms);
				predictionsList.setAdapter(platformsList);
				//	Edit the title bar every time station is changed to reflect the changes
				if (customTitleBarSupported) editTitleBar();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing	
			}
		});
	}
	
	private void setupCustomTitleBar() {
		//	Setup the custom title bar
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		//	Get the widget instances
		titleBar = findViewById(R.id.custom_title_bar);
		lineTitle = (TextView) findViewById(R.id.text_line);
		stationTitle = (TextView) findViewById(R.id.text_station);
		//	Set colours and text of widgets
		titleBar.setBackgroundColor(textColour);
		if (line != null) lineTitle.setText(line.linename);
		if (station != null) stationTitle.setText(station.stationname);
	}
	
	private void editTitleBar() {
		//	Set colours and text of widgets
		if (line != null) lineTitle.setText(line.linename);
		if (station != null) stationTitle.setText(station.stationname);
		titleBar.setBackgroundColor(textColour);
	}

	/**
	 * Simple, easy, dirty static method for showing Toast messages from any class
	 * @param message
	 */
	public final static void displayToast(final String message) {
		Toast.makeText(INSTANCE.getApplicationContext(), message, Toast.LENGTH_LONG);
	}

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

	public int getTextColour() {
		return textColour;
	}

	public void setTextColour(int textColour) {
		this.textColour = textColour;
	}
}