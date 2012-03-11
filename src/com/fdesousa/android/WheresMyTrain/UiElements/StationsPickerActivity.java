package com.fdesousa.android.WheresMyTrain.UiElements;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.ConfigCodes;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListLine;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListStation;

public class StationsPickerActivity extends ListActivity {

	private ArrayAdapter<StationsListStation> adapter;
	private UiController uiController;
	/**
	 * Simple boolean for determining whether the Custom Title bar window
	 * feature is enabled
	 */
	private boolean customTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Requesting window features must be done before anything else, so do it now
		customTitleBar = requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the content view to our main layout
		setContentView(R.layout.widget_config_layout);

		//	Get the extras from our passed-in Intent
		int colour = getIntent().getExtras().getInt(ConfigCodes.LINE_COLOUR_EXTRA, android.R.color.black);
		StationsListLine line = getIntent().getExtras().getParcelable(ConfigCodes.SLLINE_EXTRA);

		// Setup the cancel result first, in case the user cancels this activity early
		setResult(RESULT_CANCELED, new Intent());

		setUpUiController(colour);

		//	Setup the adapter, set it to the Activity
		adapter = new StationsArrayAdapter(this, line.stations, uiController);
		setListAdapter(adapter);
	}

	/**
	 * Convenience method to setup the UiController during the onCreate method
	 * @param colour - integer value of colour to use for the title bar and text
	 */
	private void setUpUiController(int colour) {
		//	Setup the uiController for later use
		uiController = new UiControllerConfig(getResources(), getAssets(), customTitleBar, this);
		uiController.setTextColour(colour);
		uiController.setTitleBarColour(colour);
		uiController.refreshMainTitleBar("Choose Station");
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		StationsListStation station = adapter.getItem(position);
		//	Construct a proper result to return
		Intent result = new Intent()
				.putExtra(ConfigCodes.STATION_CODE_RESULT, station.stationcode)
				.putExtra(ConfigCodes.STATION_NAME_RESULT, station.stationname);
		setResult(RESULT_OK, result);
		//	Since the result has been finished and set, finish up
		finish();
	}

}
