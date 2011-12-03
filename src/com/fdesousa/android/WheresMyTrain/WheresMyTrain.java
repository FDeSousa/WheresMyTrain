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

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fdesousa.android.WheresMyTrain.UiElements.LinesSpinnerAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.PlatformsExpListAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.StationsSpinnerAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;
import com.fdesousa.android.WheresMyTrain.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.requests.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.requests.LineStatus.LSContainer;
import com.fdesousa.android.WheresMyTrain.requests.LineStatus.LSLine;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLStation;

/**
 * <b>WheresMyTrain : Activity</b>
 * <p>Main Activity for the app, instantiating and controlling most UI elements.<br/>
 * Provides access to application resources, assets, UI widgets, useful instances.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class WheresMyTrain extends Activity {

	//	Useful logging variables
	/**	Tag to be used when Logging an exception/error/anything at all							*/
	public static final String TAG = "WheresMyTrain";
	/**	Current instance of this activity. Quick and dirty access to resources, context, etc.	*/
	public static WheresMyTrain INSTANCE;
	/**	Instance of UiController for setting up, and controlling, all of the UI					*/
	public static UiController UI_CONTROLLER;

	//	Main view widgets
	/**	Spinner used for selecting Underground Line - colour-coded text choices					*/
	private Spinner linesSpinner;
	/**	Spinner used for selecting tube station from the given Line - colour-coded by Line		*/
	private Spinner stationsSpinner;
	/**	Button for displaying service status to the user										*/
	private Button serviceStatus;
	/**	Expandable List used for display Platforms and predicted Trains' destination and timing	*/
	private ExpandableListView predictionsList;

	//	Adapters for Spinners and ExpandableListView widgets
	/**	Instance of the Adapter for LinesSpinner												*/
	private LinesSpinnerAdapter mLineAdapter;
	/**	Instance of the Adapter for StationsSpinner												*/
	private StationsSpinnerAdapter mStationAdapter;
	/**	Instance of the Adapter for PredictionsList												*/
	private PlatformsExpListAdapter mPlatformAdapter;

	//	JSON reading related instances
	/**	Private instance of TflJsonReader for fetching and parsing JSON requests				*/
	private TflJsonReader mJsonR;
	/**	Instance of the currently selected Line for rapid access to code, name and stations		*/
	private SLLine line;
	/**	Instance of the currently selected Station for rapid access to code and name			*/
	private SLStation station;

	//	Anything else
	/**	Simple boolean for determining whether the Custom Title bar window feature is enabled	*/
	private boolean customTitleBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//	Requesting window features must be done before anything else, so do it now
		customTitleBar = requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.detailed_predictions);

		instantiateVariables();
		setupWidgets();
		if (customTitleBar) UI_CONTROLLER.setupCustomTitleBar(line, station);
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
		case R.id.about:
			break;
		case R.id.exit:
			UI_CONTROLLER.displayExitConfirmationDialog();
			break;
		case R.id.refresh:
			new RefreshPredictions().execute();
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		UI_CONTROLLER.displayExitConfirmationDialog();
	}

	//	Convenience method for instantiation
	/**
	 * Convenience method to setup variables in the onCreate.<br/>
	 * Makes onCreate tidier to look at
	 */
	private void instantiateVariables() {
		INSTANCE = this;
		UI_CONTROLLER = new UiController(getResources(), getAssets());

		mJsonR = new TflJsonReader(getCacheDir());

		//	For safety, since Bakerloo is shown first, use Bakerloo colours to initialise
		UI_CONTROLLER.setTextColour(getResources().getColor(R.color.bakerloo_colour));

		//	Initialise the display widgets
		linesSpinner = (Spinner) findViewById(R.id.lines_spinner);
		stationsSpinner = (Spinner) findViewById(R.id.stations_spinner);
		serviceStatus = (Button) findViewById(R.id.service_status);
		predictionsList = (ExpandableListView) findViewById(R.id.platforms_list);
	}

	//	Spinners related methods
	/**
	 * Convenience method to setup the Spinners in the onCreate.<br/>
	 * Makes onCreate tidier to look at
	 */
	private void setupWidgets() {
		//	First of all, reset the line status button
		resetLineStatusButton();
		//	Call an Asynchronous Task to instantiate the Stations List
		new PrepareStationsList().execute();

		linesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the item selected by setting the second spinner with the correct stations
				line = (SLLine) parent.getItemAtPosition(pos);
				new RefreshLineStatus().execute();
				setupStationsSpinner();
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
				if (customTitleBar) UI_CONTROLLER.refreshTitleBar(line, station);
			}
			@Override	// Do nothing
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	/**
	 * Utility method. Sets up the spinner for Stations. Useful for new selection, but also for
	 * restoring the default station at start
	 */
	private void setupStationsSpinner() {
		//	Update the data set and reset the adapter
		mStationAdapter = new StationsSpinnerAdapter(line.stations, line.linecode);
		stationsSpinner.setAdapter(mStationAdapter);
	}

	//	Fetch, parse, display the list of lines and stations
	/**
	 * AsyncTask sub-class to achieve a non-blocking manner in which to get the stations list,
	 * even if the Internet connection is a little bit slow on the mobile side.<br/>
	 * Stations List can sometimes take several seconds to process on the server-side alone,
	 * for which amount of time, the UI thread would be blocked otherwise.<br/>
	 * Will not block the UI thread, which is the important part.
	 * @author Filipe De Sousa
	 */
	private class PrepareStationsList extends AsyncTask<Void, Void, SLContainer> {
		@Override
		protected SLContainer doInBackground(Void... params) {
			//	Send the request to prepare the JSON data while other stuff goes on
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

	//	Refresh the detailed predictions
	/**
	 * AsyncTask sub-class to achieve a non-blocking manner in which to get detailed predictions,
	 * even if the internet connection is a little bit slow on the mobile side.<br/>
	 * Will not block the UI thread, which is the important part.
	 * @author Filipe De Sousa
	 */
	private class RefreshPredictions extends AsyncTask<Void, Void, DPContainer> {
		@Override
		protected void onPreExecute() {
			//	Hide the expandable list view, to make sure old predictions aren't shown
			predictionsList.setVisibility(View.INVISIBLE);
		}
		@Override
		protected DPContainer doInBackground(Void... params) {
			//	Send the request to prepare the JSON data while other stuff goes on
			//	Get the prepared JSON data now to fill the spinners
			return mJsonR.getDetailedPredictions(line.linecode, station.stationcode);
		}
		@Override
		protected void onPostExecute(DPContainer result) {
			//	Because of how tfl.php sends predictions data, there is only ever ONE station in stations array
			mPlatformAdapter = new PlatformsExpListAdapter(result.stations.get(0).platforms);
			//	Show the expandable list view, to show new predictions
			predictionsList.setVisibility(View.VISIBLE);
			//	(Re)set the adapter onto the ExpandableListView
			predictionsList.setAdapter(mPlatformAdapter);
		}
	}

	//	Refresh the line status
	private class RefreshLineStatus extends AsyncTask<Void, Void, LSContainer> {
		@Override
		protected void onPreExecute() {
			resetLineStatusButton();
		}
		@Override
		protected LSContainer doInBackground(Void... params) {
			//	Send the request to prepare the JSON data, but only get lines with incidents
			//	Get the prepared JSON data now to fill the button
			return mJsonR.getLineStatus(false);
		}
		@Override
		protected void onPostExecute(LSContainer result) {
			getLineStatus(result);
		}
	}

	private void resetLineStatusButton() {
		//	Reset the status button to black and white, unknown status
		serviceStatus.setBackgroundResource(R.drawable.btn_white_matte);
		serviceStatus.setTextColor(Color.BLACK);
		serviceStatus.setTypeface(UI_CONTROLLER.book);
		serviceStatus.setText("Unknown Status");
	}

	private void getLineStatus(LSContainer linestatus) {
		LSLine singleLine;

		/*	These lines:
		 *	Bakerloo, Central, District, Jubilee, Metropolitan, Northern, Piccadilly, Victoria, Waterloo & City
		 *	get treated in the same way. Search for the line name, place description in Button text
		 */
		if (! line.linecode.equals("h")) {
			//	Search for the line, check the result isn't null
			if ((singleLine = linestatus.searchByLinename(line.linename)) != null) {
				//	Determine if there's Good Service (represented by GS in statusid)
				if (singleLine.statusid.equals("GS")) {
					//	If so, button is green
					serviceStatus.setBackgroundResource(R.drawable.btn_green_matte);
				} else {
					//	If not, button is orange
					serviceStatus.setBackgroundResource(R.drawable.btn_orange_matte);
				}
				//	Set the status message either way
				serviceStatus.setText(singleLine.description);
			}
		} else {
			/*	Hammersmith & City, Circle lines together
			 *	Due to how H&C and Circle lines are handled by TfL in predictions,
			 *	we need to search for two line status instances, compare, and show
			 */
			;
		}

		serviceStatus.setTextColor(Color.WHITE);
	}

	//	Other methods
	/**
	 * Simple, easy, dirty static method for showing Toast messages from any class
	 * @param message - String to display to the user
	 */
	public final static void displayToast(final String message) {
		Toast.makeText(INSTANCE.getApplicationContext(), message, Toast.LENGTH_LONG);
	}
}