package com.fdesousa.android.WheresMyTrain.UiElements;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListLine;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListReader;

public class LinesPickerActivity extends ListActivity {

	public static final String LINE_COLOUR_EXTRA = "line_colour";
	public static final String SLLINE_EXTRA = "sl_line";
	public static final String LINE_CODE_RESULT = "linecode";
	public static final String STATION_CODE_RESULT = "stationcode";
	public static final int PICK_STATION_REQUEST = 1122334455;
	protected ArrayAdapter<StationsListLine> adapter;
	protected UiController uiController;

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

		// Setup the cancel result first, in case the user cancels this activity early
		setResult(RESULT_CANCELED, new Intent());

		//	Setup the uiController for later use
		uiController = new UiControllerConfig(getResources(), getAssets(), customTitleBar, this, false);
		uiController.refreshMainTitleBar("Choose Underground Line");
		//	Time to sort out the list of lines and their associated stations
		TflJsonReader<StationsListContainer> mJsonR = new StationsListReader(getCacheDir());
		StationsListContainer container = mJsonR.get();
		adapter = new LinesArrayAdapter(this, container.lines, uiController);
		//	Now set the ListAdapter so we can see this data set
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		StationsListLine line = adapter.getItem(position);
		this.uiController.setTextColour(uiController.getLineColour(line.linecode));

		Intent getStation = new Intent(this, StationsPickerActivity.class)
				.putExtra(LINE_COLOUR_EXTRA, this.uiController.getTextColour())
				.putExtra(SLLINE_EXTRA, line);
		startActivityForResult(getStation, PICK_STATION_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_STATION_REQUEST && resultCode == RESULT_OK) {
			//	Line and station chosen, set result to OK, finish normally
			setResult(RESULT_OK, data);
			finish();
		}
	}

}
