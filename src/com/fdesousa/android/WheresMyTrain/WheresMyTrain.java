package com.fdesousa.android.WheresMyTrain;

/******************************************************************************
 * Copyright 2011 Filipe De Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

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
		//	Set the content view to our main layout
		setContentView(R.layout.detailed_predictions);
		//	Now it's time to instantiate and setup things
		instantiateVariables();
		setupWidgets();
		//	Check if we have removed the title bar, then setup the custom one
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
			//	Display about dialog
			UI_CONTROLLER.displayAboutDialog();
			break;
		case R.id.exit:
			//	Display the finish confirmation dialog
			UI_CONTROLLER.displayExitConfirmationDialog();
			break;
		case R.id.refresh:
			//	Refresh the predictions
			new RefreshPredictions().execute();
			//	Also refresh line status
			new RefreshLineStatus().execute();
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		//	Display the finish confirmation dialog
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
				setupStationsSpinner();
				//	Refresh the line status too, since a line has been selected now
				new RefreshLineStatus().execute();
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
	/**
	 * AsyncTask sub-class to achieve a non-blocking manner in which to get line status,
	 * even if the internet connection is a little bit slow on the mobile side.<br/>
	 * Will not block the UI thread, which is the important part.
	 * @author Filipe De Sousa
	 */
	private class RefreshLineStatus extends AsyncTask<Void, Void, LSContainer> {
		@Override
		protected void onPreExecute() {
			//	Just clean up the line status button
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
			//	Since line status has been updated now, determine what to display to the user
			determineLineStatus(result);
		}
	}

	private void resetLineStatusButton() {
		//	Reset the status button to black and white, unknown status
		serviceStatus.setBackgroundResource(R.drawable.btn_white_matte);
		serviceStatus.setTextColor(Color.BLACK);
		serviceStatus.setTypeface(UI_CONTROLLER.book);
		serviceStatus.setText("Unknown Status");
		UI_CONTROLLER.setLineStatusDialogText("Unknown Status", "Please wait");
	}

	/**
	 * Rather ungainly and long method to set the button text and dialog text depending upon
	 * the station we're currently viewing. Needs cutting down.
	 * @param linestatus - instance of LSContainer with line status info
	 */
	private void determineLineStatus(LSContainer linestatus) {
		//	Only used for dialog box title, but still has to be set below
		String title = "";
		//	Generally two-letter informational code. I.e. GS=Good Service, PC=Part Closure, CS=Closed
		String statusid = "";
		//	Two-word description of status (labelled description). I.e. Good Service, Part Closure, Planned Closure, etc.
		String description = "";
		//	One-sentence long description of status, or usually empty if Good Service
		String details = "";

		if (! line.linecode.equals(StandardCodes.HAMMERSMITH_CODE)) {
			/*	These lines:
			 *	Bakerloo, Central, District, Jubilee, Metropolitan, Northern, Piccadilly, Victoria, Waterloo & City
			 *	get treated in the same way. Search for the line name, place description in Button text
			 */
			LSLine singleLine;
			String linename = line.linename;
			//	Waterloo and City line uses ampersand in detailed predictions, but "and" in line status
			if (line.linecode.equals(StandardCodes.WATERLOO_CODE))
				linename = StandardCodes.WATERLOO_NAME;

			//	Search for the line, check the result isn't null
			if ((singleLine = linestatus.searchByLinename(linename)) != null) {
				title = String.format("%s Line", singleLine.linename);
				statusid = singleLine.statusid;
				description = singleLine.description;
				details = decideMessageChoice(singleLine.details, singleLine.description);
			}
		} else {
			/*	Hammersmith & City, Circle lines together
			 *	Due to how H&C and Circle lines are handled by TfL in predictions,
			 *	we need to search for two line status instances, compare, and show
			 */
			//	Set title for Hammersmith & City and Circle lines, but make it short
			title = "H & C, Circle Lines";
			//	H&C line variable
			LSLine hLine;
			//	Circle line variable
			LSLine cLine;

			//	Get the line status for Hammersmith & City line
			if ((hLine = linestatus.searchByLinename(StandardCodes.HAMMERSMITH_NAME)) != null) {
				//	Just determine whether the description is empty
				statusid = hLine.statusid;
				description = hLine.description;
				//	Format the message string for the dialog, decide whether to use details/description
				details = String.format("%s Line:\n%s", StandardCodes.HAMMERSMITH_NAME,
						decideMessageChoice(hLine.details, hLine.description));
			} else {
				//	If nothing is returned, make up status, pretending it's all good
				statusid = StandardCodes.GOOD_SERVICE_CODE;
				description = "Good Service";
				details = String.format("%s Line:\n%s", StandardCodes.HAMMERSMITH_NAME, "Good Service");
			}

			//	Get the line status for Circle line
			if ((cLine = linestatus.searchByLinename(StandardCodes.CIRCLE_NAME)) != null) {
				//	Details are easy, just construct the String again, decide whether to use details or description
				details = String.format("%s\n\n%s Line:\n%s", details, StandardCodes.CIRCLE_NAME,
						decideMessageChoice(cLine.details, cLine.description));
				
				//	Determine which status description to use now
				if (! cLine.statusid.equals(StandardCodes.GOOD_SERVICE_CODE) && ! cLine.statusid.equals(statusid)) {
					/* If Circle line statusid isn't GS, and isn't the same as current statusid,
					 * likely to be worse than H&C, set it as statusid
					 */
					statusid = cLine.statusid;
					description = cLine.description;
				}
			} else {
				//	If Circle line isn't found, assume Good Service, make up details
				details = String.format("%s\n\n%s Line:\n%s", details, StandardCodes.CIRCLE_NAME, "Good Service");
			}
		}

		//	Determine button colour to use depending upon service status ID
		if (statusid.equals(StandardCodes.GOOD_SERVICE_CODE)) {
			//	If Good Service (GS code), button is green
			serviceStatus.setBackgroundResource(R.drawable.btn_green_matte);
		} else if (statusid.equals(StandardCodes.CLOSED_CODE)) {
			//	If Closed (CS code), button is red
			serviceStatus.setBackgroundResource(R.drawable.btn_red_matte);
		} else {
			//	Otherwise, button is orange, assume part-closure/delays/unknown
			serviceStatus.setBackgroundResource(R.drawable.btn_orange_matte);
		}
		//	Set the status message either way
		serviceStatus.setText(description);
		//	Setup the appropriate text colour
		serviceStatus.setTextColor(Color.WHITE);
		
		//	Now setup the line status dialog's title and message
		UI_CONTROLLER.setLineStatusDialogText(title, details);
	}
	
	/**
	 * Convenience method to decide if details string is too short/is empty.<br/>
	 * If so, return the description string instead. Useful for line status dialog
	 * @param details - the line status details for a specific line
	 * @param description - the line status description for a specific line
	 * @return the string that should be displayed in the dialog
	 */
	private String decideMessageChoice(String details, String description) {
		String outDetails;
		if (details.length() < 1) {
			//	If so, use the description text instead, which is never empty
			outDetails = description;
		} else {
			//	If it's a long message, use the text
			outDetails = details;
		}
		return outDetails;
	}

	//	onClick method for the line status button
	/**
	 * OnClick method for the serviceStatus Button, as defined in layout XML</br>
	 * Just shows the line status dialog
	 * @param v - instance of View (generally will be the Button itself)
	 */
	public void showLineStatus(View v) {
		UI_CONTROLLER.showLineStatusDialog();
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