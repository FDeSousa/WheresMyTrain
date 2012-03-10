package com.fdesousa.android.WheresMyTrain.Widget.Config;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.StationsListLine;
import com.fdesousa.android.WheresMyTrain.UiElements.LinesPickerActivity;
import com.fdesousa.android.WheresMyTrain.Widget.Widget;

/**
 * <h1>Lines.java</h1>
 * Exactly as its superclass, except for the ability to write configuration
 * and choices data to a SharedPreferences file, which is useful for the
 * configuration of a widget for WMT in later development.
 * 
 * @see com.fdesousa.android.WheresMyTrain.UiElements.LinesPickerActivity
 * @author Filipe De Sousa
 *
 */
public class Lines extends LinesPickerActivity {
	protected int appWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get the appWidgetId of the appWidget being configured
		appWidgetId = getIntent().getExtras()
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

		// Setup the cancel result first, in case the user cancels this activity early
		setResult(RESULT_CANCELED, new Intent()
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId));

		//	To save ourselves the hassle, check the fetched appWidgetId now
		if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) finish();
	}

	/**
	 * Almost identical to its super-class implementation of onListItemClick,
	 * but uses Stations.class instead of StationPickerActivity.class
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		StationsListLine line = adapter.getItem(position);
		this.uiController.setTextColour(uiController.getLineColour(line.linecode));

		Intent getStation = new Intent(this, Stations.class)
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
			setResult(RESULT_OK, data.putExtra(
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