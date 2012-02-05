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

import android.app.ExpandableListActivity;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonFetcher;
import com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions.DPAsyncTask;
import com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.LineStatus.LSAsyncTask;
import com.fdesousa.android.WheresMyTrain.Library.requests.LineStatus.LSContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.SLAsyncTask;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.SLStation;
import com.fdesousa.android.WheresMyTrain.UiElements.StationsSpinnerAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;
import com.fdesousa.android.WheresMyTrain.UiElements.UiControllerMain;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * <b>WheresMyTrain : Activity</b>
 * <p>Main Activity for the app, instantiating and controlling most UI elements.<br/>
 * Provides access to application resources, assets, UI widgets, useful instances.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class WheresMyTrain extends ExpandableListActivity {
	// Useful logging variables
	/** Tag to be used when Logging an exception/error/anything at all */
	public static final String TAG = "com.fdesousa.android.WheresMyTrain";
	/**
	 * Current instance of this activity. Quick and dirty access to resources,
	 * context, etc.
	 */
	//public static WheresMyTrain INSTANCE;
	/** Instance of UiController for setting up, and controlling, all of the UI */
	private UiController uiController;

	// Main view widgets
	/** Spinner used for selecting Underground Line - colour-coded text choices */
	private Spinner linesSpinner;
	/**
	 * Spinner used for selecting tube station from the given Line -
	 * colour-coded by Line
	 */
	private Spinner stationsSpinner;
	/** Button for displaying service status to the user */
	private Button serviceStatus;
	/**
	 * Expandable List used for display Platforms and predicted Trains'
	 * destination and timing
	 */
	private PullToRefreshExpandableListView predictionsList;

	/** Instance of the Adapter for StationsSpinner */
	private StationsSpinnerAdapter mStationAdapter;

	// JSON reading related instances
	/**
	 * Instance of the currently selected Line for rapid access to code, name
	 * and stations
	 */
	private SLLine line;
	/**
	 * Instance of the currently selected Station for rapid access to code and
	 * name
	 */
	private SLStation station;

	// Anything else
	/**
	 * Simple boolean for determining whether the Custom Title bar window
	 * feature is enabled
	 */
	private boolean customTitleBar;
	/** Simple boolean for determining if connectivity is available */
	private boolean connected;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Setup the activity, first calling super class onCreate
		super.onCreate(savedInstanceState);
		// Requesting window features must be done before anything else, so do it now
		customTitleBar = requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the content view to our main layout
		setContentView(R.layout.detailed_predictions);

		// Check the connectivity
		checkConnectivity();
		if (this.connected) {
			// Now it's time to instantiate and setup things
			instantiateVariables();
			setupWidgets();			
		}
	}

	private void checkConnectivity() {
		this.connected = TflJsonFetcher.isReachable(this);
		
		if (!this.connected) {
			Toast.makeText(this, "Server is unreachable. Check connectivity", Toast.LENGTH_LONG);
		}
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
			// Refresh the predictions
			if (getPredictions instanceof DPAsyncTask) {
				getPredictions.cancel(true);
			}
			getPredictions = new DPAsyncTask(this, uiController, line.linecode, station.stationcode).execute();

			// Also refresh line status
			if (getLineStatus instanceof LSAsyncTask) {
				getLineStatus.cancel(true);
			}
			getLineStatus = new LSAsyncTask(this, uiController, line).execute();
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

	// Convenience method for instantiation
	/**
	 * Convenience method to setup variables in the onCreate.<br/>
	 * Makes onCreate tidier to look at
	 */
	private void instantiateVariables() {
		//INSTANCE = this;
		uiController = new UiControllerMain(getResources(), getAssets(), customTitleBar, this);

		// For safety, since Bakerloo is shown first, use Bakerloo colours to
		// initialise
		uiController.setTextColour(uiController.getLineColour("b"));

		// Initialise the display widgets
		linesSpinner = (Spinner) findViewById(R.id.lines_spinner);
		stationsSpinner = (Spinner) findViewById(R.id.stations_spinner);
		serviceStatus = (Button) findViewById(R.id.service_status);
		predictionsList = (PullToRefreshExpandableListView) findViewById(R.id.platforms_list);
	}

	// Spinners related methods
	/**
	 * Convenience method to setup the Spinners in the onCreate.<br/>
	 * Makes onCreate tidier to look at
	 */
	private void setupWidgets() {
		// First of all, reset the line status button
		resetLineStatusButton();
		// Call an Asynchronous Task to instantiate the Stations List
		if (prepareStationsList instanceof SLAsyncTask) {
			prepareStationsList.cancel(true);
		}
		prepareStationsList = new SLAsyncTask(this, uiController).execute();

		// Set the OnItemSelectedListener for Lines Spinner
		linesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// Handle the item selected by setting the second
				// spinner with the correct stations
				line = (SLLine) parent.getItemAtPosition(pos);
				if (connected) {
					setupStationsSpinner();
					// Refresh the line status too, since a line has
					// been selected now
					if (getLineStatus instanceof LSAsyncTask) {
						getLineStatus.cancel(true);
					}
					getLineStatus = new LSAsyncTask(WheresMyTrain.this, uiController, line).execute();
				}
			}
			@Override
			// Do nothing
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		// Set the OnItemSelectedListener for Stations Spinner
		stationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// Handle the selected item by getting detailed
				// predictions for that line & station choice, 
				// displaying that in the ExpandableListView
				station = (SLStation) parent.getItemAtPosition(pos);
				if (connected) {
					if (getPredictions instanceof DPAsyncTask) {
						getPredictions.cancel(true);
					}
					getPredictions = new DPAsyncTask(WheresMyTrain.this, uiController, line.linecode, station.stationcode).execute();
					// Edit the title bar every time station is changed
					// to reflect the changes
					if (customTitleBar)
						if (uiController instanceof UiControllerMain)
							((UiControllerMain) uiController).refreshMainTitleBar(line.linename, station.stationname);
				}
			}
			@Override
			// Do nothing
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		// Finally, set the OnRefreshListener for Predictions List
		predictionsList.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (connected) {
					// Check if there's already an instance, if so, cancel for safety
					if (getPredictions instanceof DPAsyncTask) {
						getPredictions.cancel(true);
					}
					// Then instantiate a-new and execute the request
					getPredictions = new DPAsyncTask(WheresMyTrain.this, uiController, line.linecode, station.stationcode).execute();
					// Refresh the line status too, since we're refreshing everything
					if (getLineStatus instanceof LSAsyncTask) {
						getLineStatus.cancel(true);
					}
					// Instantiate a-new and execute this request too
					getLineStatus = new LSAsyncTask(getParent(), uiController, line).execute();
				}
			}
		});
	}

	/**
	 * Utility method. Sets up the spinner for Stations. Useful for new
	 * selection, but also for restoring the default station at start
	 */
	private void setupStationsSpinner() {
		// Update the data set and reset the adapter
		mStationAdapter = new StationsSpinnerAdapter(line.stations, line.linecode, getLayoutInflater(), uiController);
		stationsSpinner.setAdapter(mStationAdapter);
	}

	// Fetch, parse, display the list of lines and stations
	/** To avoid conflicts, have a copy of the AsyncTask to cancel if needed */
	private AsyncTask<Void, Void, SLContainer> prepareStationsList;

	// Get/Refresh the detailed predictions
	/** To avoid conflicts, have a copy of the AsyncTask to cancel if needed */
	private AsyncTask<Void, Void, DPContainer> getPredictions;

	// Get/Refresh the line status
	/** To avoid conflicts, have a copy of the AsyncTask to cancel if needed */
	private AsyncTask<Void, Void, LSContainer> getLineStatus;

	private void resetLineStatusButton() {
		// Reset the status button to black and white, unknown status
		serviceStatus.setBackgroundResource(R.drawable.btn_white_basic);
		serviceStatus.setTextColor(Color.BLACK);
		serviceStatus.setTypeface(uiController.book);
		serviceStatus.setText("Unknown Status");
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).setLineStatusDialogText("Unknown Status", "Please wait");
	}

	// onClick method for the line status button
	/**
	 * OnClick method for the serviceStatus Button, as defined in layout
	 * XML</br> Just shows the line status dialog
	 * @param v - instance of View (generally will be the Button itself)
	 */
	public void showLineStatus(View v) {
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).showLineStatusDialog();
	}
}