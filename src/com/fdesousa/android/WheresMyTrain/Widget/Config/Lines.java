package com.fdesousa.android.WheresMyTrain.Widget.Config;

import java.util.Calendar;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.UiElements.LinesArrayAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;
import com.fdesousa.android.WheresMyTrain.UiElements.UiControllerConfig;
import com.fdesousa.android.WheresMyTrain.Widget.Widget;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Lines extends ListActivity {
	public static final String LINE_COLOUR_EXTRA = "line_colour";
	public static final String SLLINE_EXTRA = "sl_line";
	public static final String LINE_CODE_RESULT = "linecode";
	public static final String STATION_CODE_RESULT = "stationcode";
	public static final int PICK_STATION_REQUEST = 1122334455;

	private ArrayAdapter<SLLine> adapter;
	private UiController uiController;

	// Anything else
	/**
	 * Simple boolean for determining whether the Custom Title bar window
	 * feature is enabled
	 */
	private boolean customTitleBar;
	private int appWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Requesting window features must be done before anything else, so do it now
		customTitleBar = requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the content view to our main layout
		setContentView(R.layout.widget_config_layout);

		// get the appWidgetId of the appWidget being configured
		appWidgetId = getIntent().getExtras()
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

		// Setup the cancel result first, in case the user cancels this activity early
		setResult(RESULT_CANCELED, new Intent()
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId));

		//	To save ourselves the hassle, check the fetched appWidgetId now
		if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		//	Setup the uiController for later use
		uiController = new UiControllerConfig(getResources(), getAssets(), customTitleBar, this, false);
		uiController.refreshMainTitleBar("Choose Underground Line");
		//	Time to sort out the list of lines and their associated stations
		TflJsonReader mJsonR = new TflJsonReader(getCacheDir());
		SLContainer container = mJsonR.getStationsList();
		adapter = new LinesArrayAdapter(this, container.lines, uiController);
		//	Now set the ListAdapter so we can see this data set
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		SLLine line = adapter.getItem(position);
		this.uiController.setTextColour(uiController.getLineColour(line.linecode));

		Intent getStation = new Intent(this, Stations.class)
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
				.putExtra(LINE_COLOUR_EXTRA, this.uiController.getTextColour())
				.putExtra(SLLINE_EXTRA, line);
		startActivityForResult(getStation, PICK_STATION_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_STATION_REQUEST && resultCode == RESULT_OK) {
			//	Write out the results, relevant to the widget being configured only
			writePreferences(data.getExtras());

			//	Prepare the alarm service to perform the updates to the widget
			Intent intent = new Intent(Widget.WIDGET_UPDATE);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			//	Set the start time to 2 seconds from now
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.add(Calendar.SECOND, 2);
			//	Set to repeat every 2 minutes (2 * 60 * 1000)
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
					cal.getTimeInMillis(), 2 * 60 * 1000, pendingIntent);
			//	Save the AlarmManager to later be cancelled if necessary
			Widget.saveAlarmManager(alarmManager, pendingIntent);

			//	Line and station chosen, set result to OK, finish normally
			setResult(RESULT_OK, new Intent().putExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId));
			finish();
		}
	}

	private void writePreferences(Bundle result) {
		//	Make up the line and station preferences keys
		String lineKey = Widget.LINE_PREFS_KEY + appWidgetId;
		String stationKey = Widget.STATION_PREFS_KEY + appWidgetId;
		String lineColourKey = Widget.LINE_COLOUR_KEY + appWidgetId;
		//	Get our shared preferences file for editing
		SharedPreferences settings = this.getSharedPreferences(Widget.TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit()
				.putString(lineKey, result.getString(LINE_CODE_RESULT))
				.putString(stationKey, result.getString(STATION_CODE_RESULT))
				.putInt(lineColourKey, this.uiController.getTextColour());
		//	Done reading, commit to shared preferences file
		edit.commit();
	}
}