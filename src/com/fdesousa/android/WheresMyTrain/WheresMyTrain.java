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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.fdesousa.android.WheresMyTrain.Library.ConfigCodes;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonFetcher;
import com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions.DetailedPredictionsAsyncTask;
import com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions.DetailedPredictionsContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.LineStatus.LineStatusAsyncTask;
import com.fdesousa.android.WheresMyTrain.Library.requests.LineStatus.LineStatusContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListAsyncTask;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListLine;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListReader;
import com.fdesousa.android.WheresMyTrain.UiElements.LinesPickerActivity;
import com.fdesousa.android.WheresMyTrain.UiElements.StationsPickerActivity;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;
import com.fdesousa.android.WheresMyTrain.UiElements.UiControllerMain;

/**
 * <h1>WheresMyTrain : Activity</h1>
 * <p>Main Activity for the app, instantiating and controlling most UI elements.<br/>
 * Provides access to application resources, assets, UI widgets, useful instances.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class WheresMyTrain extends ExpandableListActivity {
	/** Tag to be used when Logging an exception/error/anything at all */
	public static final String TAG = "com.fdesousa.android.WheresMyTrain";

	/** 
	 * Instance of UiController for setting up, and controlling, all of the UI 
	 */
	private UiController uiController;

	/** 
	 * Button used for selecting Underground Line - colour-coded text choices 
	 */
	private Button linesChooser;
	/**
	 * Button used for selecting tube station from the given Line -
	 * colour-coded by Line
	 */
	private Button stationsChooser;
	/** 
	 * Button for displaying service status to the user 
	 */
	private Button serviceStatus;

	/**
	 * Container instance of stations list for rapid access to Line and Station
	 * lists for opening activities.
	 */
	private StationsListContainer container;

	/**
	 * Instance of the currently selected Line for rapid access to code, name
	 * and stations
	 */
	private String linecode;
	private String linename;
	/**
	 * Instance of the currently selected Station for rapid access to code and
	 * name
	 */
	private String stationcode;
	private String stationname;

	/**
	 * Simple boolean for determining whether the Custom Title bar window
	 * feature is enabled
	 */
	private boolean customTitleBar;
	/** 
	 * Boolean to store result when determining if connectivity is available 
	 */
	private boolean connected;

	/**
	 * Get/Refresh the detailed predictions
	 * To avoid conflicts, have a copy of the AsyncTask to cancel if needed
	 */
	private AsyncTask<Void, Void, DetailedPredictionsContainer> getPredictions;

	/**
	 * Get/Refresh the line status
	 * To avoid conflicts, have a copy of the AsyncTask to cancel if needed
	 */
	private AsyncTask<Void, Void, LineStatusContainer> getLineStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Setup the activity, first calling super class onCreate
		super.onCreate(savedInstanceState);
		// Requesting window features must be done before anything else, so do it now
		customTitleBar = requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the content view to our main layout
		setContentView(R.layout.main_detailed_predictions);
		// Now it's time to instantiate and setup the GUI
		instantiateVariables();
		setupWidgets();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Check the connectivity
		checkConnectivity();
		if (!this.connected) {
			Toast.makeText(this, "Server is unreachable. Check connectivity", Toast.LENGTH_LONG);
			this.finish();
		}
	}

	/**
	 * Convenience method to automate checking the connectivity of the device.
	 */
	private void checkConnectivity() {
		this.connected = TflJsonFetcher.isReachable(this);

		if (!this.connected) {
			Toast.makeText(this, "Server is unreachable. Check connectivity", Toast.LENGTH_LONG);
		}
	}

	/**
	 * Method called when first instantiating menu. Only called once.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mInflater = getMenuInflater();
		mInflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Method called when a Menu item is chosen by the user.
	 * Determines what to do depending upon the item clicked.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			// Display about dialog
			if (uiController instanceof UiControllerMain)
				((UiControllerMain) uiController).displayAboutDialog();
			break;
		case R.id.exit:
			// Display the finish confirmation dialog
			if (uiController instanceof UiControllerMain)
				((UiControllerMain) uiController).displayExitConfirmationDialog();
			break;
		case R.id.refresh:
			// Refresh the predictions and line status
			performRefresh();
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		// Display the finish confirmation dialog
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).displayExitConfirmationDialog();
	}

	/**
	 * Convenience method to setup variables in the onCreate.<br/>
	 * Makes onCreate tidier to look at
	 */
	private void instantiateVariables() {
		Lines.setResources(getResources());
		uiController = new UiControllerMain(getResources(), getAssets(), customTitleBar, this);

		// Initialise the display widgets
		linesChooser = (Button) findViewById(R.id.lines_spinner);
		stationsChooser = (Button) findViewById(R.id.stations_spinner);
		serviceStatus = (Button) findViewById(R.id.service_status);
	}

	/**
	 * Convenience method to setup the Spinners in the onCreate.<br/>
	 * Makes onCreate tidier to look at, though adds
	 */
	private void setupWidgets() {
		// First of all, reset the line status button
		resetLineStatusButton();
		//	Set the startup colours to jubilee line grey
		uiController.setTextColour(Lines.JUBILEE.getColourCode());
		uiController.refreshMainTitleBar();
		//	Set the typefaces
		linesChooser.setTypeface(uiController.book);
		stationsChooser.setTypeface(uiController.book);
		//	Set the initial text of the buttons to prompt the user
		linesChooser.setText("Please choose a line");
		//	Hide unnecessary on-screen widgets
		stationsChooser.setVisibility(View.INVISIBLE);
		serviceStatus.setVisibility(View.INVISIBLE);
		
		StationsListReader reader = new StationsListReader(getCacheDir());
		StationsListAsyncTask asyncTask = new StationsListAsyncTask(reader);
		// Call an Asynchronous Task to instantiate the Stations List Container
		try {
			container = asyncTask.execute().get(60L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			//	TODO Handle this with a loop(?)
		} catch (ExecutionException e) {
			//	Silently ignore, we have inner-try/catch/finally to handle computation errors
		} catch (TimeoutException e) {
			//	TODO Handler a timeout by warning the user, finishing activity(?)
		}
	}

	private void resetLineStatusButton() {
		// Reset the status button to black and white, unknown status
		serviceStatus.setBackgroundResource(R.drawable.btn_white_basic);
		serviceStatus.setTextColor(Color.BLACK);
		serviceStatus.setTypeface(uiController.book);
		serviceStatus.setText("Unknown Status");
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).setLineStatusDialogText("Unknown Status", "Please wait");
	}

	/**
	 * onClick method for Line choice button. Launches the activity
	 * allowing the user to choose a line.
	 * @param v - View instance of the Button
	 */
	public void chooseLine(View v) {
		Intent lineChoiceIntent = new Intent(this, 
				LinesPickerActivity.class).putExtra(ConfigCodes.SLCONTAINER_EXTRA, container);
		startActivityForResult(lineChoiceIntent, ConfigCodes.PICK_LINE_REQUEST);
	}

	/**
	 * onClick method for Station choice button. Launches the activity
	 * allowing the user to choose a station.
	 * @param v - View instance of the Button
	 */
	public void chooseStation(View v) {
		StationsListLine line = container.getLineByLineCode(linecode);
		Intent stationChoiceIntent = new Intent(this, StationsPickerActivity.class)
				.putExtra(ConfigCodes.SLLINE_EXTRA, line)
				.putExtra(ConfigCodes.LINE_COLOUR_EXTRA, uiController.getTextColour());
		startActivityForResult(stationChoiceIntent, ConfigCodes.PICK_STATION_REQUEST);
	}

	/**
	 * OnClick method for the LineStatus Button, as defined in layout
	 * XML</br> Just shows the line status dialog
	 * @param v - View instance of the Button
	 */
	public void showLineStatus(View v) {
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).showLineStatusDialog();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			if (requestCode == ConfigCodes.PICK_LINE_REQUEST) {
				//	Get the return String denoting line code
				linecode = extras.getString(ConfigCodes.LINE_CODE_RESULT);
				linename = extras.getString(ConfigCodes.LINE_NAME_RESULT);
				onLineChosen();
			} else if (requestCode == ConfigCodes.PICK_STATION_REQUEST) {
				//	Get the return String denoting station code
				stationcode = extras.getString(ConfigCodes.STATION_CODE_RESULT);
				stationname = extras.getString(ConfigCodes.STATION_NAME_RESULT);
				onStationChosen();
				//	Refresh detailed predictions and line status
				performRefresh();
			}
		}
	}

	/**
	 * Utility method to refresh the line status and detailed predictions
	 */
	private void performRefresh() {
		//	Only refresh if connectivity is available
		if (connected) {
			//	First start the fetch line status task
			if (getLineStatus instanceof LineStatusAsyncTask) getLineStatus.cancel(true);
			getLineStatus = new LineStatusAsyncTask(this, uiController, container.getLineByLineCode(linecode));
			getLineStatus.execute();

			//	Secondly start the fetch detailed predictions task
			if (getPredictions instanceof DetailedPredictionsAsyncTask) getPredictions.cancel(true);
			getPredictions = new DetailedPredictionsAsyncTask(this, uiController, linecode, stationcode);
			getPredictions.execute();
		}
	}
	
	private void onLineChosen() {
		//	Get Lines enum for the current line code in use
		Lines lines = Lines.getLineByCode(linecode);
		//	Change button text to display line name
		linesChooser.setTextColor(lines.getColourCode());
		linesChooser.setText(linename);
		//	Set the titlebar up correctly
		uiController.setTextColour(lines.getColourCode());
		uiController.refreshMainTitleBar(linename);
		//	Make station button visible
		stationsChooser.setVisibility(View.VISIBLE);
		stationsChooser.setText("Now choose a station");
		stationsChooser.setTextColor(lines.getColourCode());
		resetLineStatusButton();
		this.getExpandableListView().setVisibility(View.INVISIBLE);
	}
	
	private void onStationChosen() {
		serviceStatus.setVisibility(View.VISIBLE);
		stationsChooser.setText(stationname);
		uiController.refreshMainTitleBar(linename, stationname);
	}
	
}
