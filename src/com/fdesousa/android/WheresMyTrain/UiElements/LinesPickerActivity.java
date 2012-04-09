package com.fdesousa.android.WheresMyTrain.UiElements;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.ConfigCodes;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListLine;

public class LinesPickerActivity extends ListActivity {

	protected ArrayAdapter<StationsListLine> adapter;
	protected UiController uiController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the content view to our main layout
		setContentView(R.layout.widget_config_layout);
		// Setup the cancel result first, in case the user cancels this activity early
		setResult(RESULT_CANCELED, new Intent());

		//	Setup the uiController for later use
		uiController = new UiControllerConfig(getResources(), getAssets(), this);
		//	Time to sort out the list of lines and their associated stations
		StationsListContainer container = getIntent().getExtras().getParcelable(ConfigCodes.SLCONTAINER_EXTRA);
		adapter = new LinesArrayAdapter(this, container.lines, uiController);
		//	Now set the ListAdapter so we can see this data set
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		StationsListLine line = adapter.getItem(position);
		//	Return String with the line code only
		Intent lineResult = new Intent()
				.putExtra(ConfigCodes.LINE_CODE_RESULT, line.linecode)
				.putExtra(ConfigCodes.LINE_NAME_RESULT, line.linename);
		setResult(RESULT_OK, lineResult);
		finish();
	}

}
