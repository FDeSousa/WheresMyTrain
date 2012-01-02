package com.fdesousa.android.WheresMyTrain.Widget;

import com.fdesousa.android.WheresMyTrain.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	public static final String TAG = "com.fdesousa.android.WheresMyTrain.Widget";
	public static final String LINE_PREFS_KEY = "line";
	public static final String STATION_PREFS_KEY = "station";
	public static final String LINE_COLOUR_KEY = "line_colour";
	public static final String WIDGET_UPDATE = "com.fdesousa.android.WheresMyTrain.Widget.Update";

	private static AlarmManager mAlarmManager;
	private static PendingIntent mPendingIntent;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		String action = intent.getAction();

		//	Check out what we're receiving here
		if (action.equals(WIDGET_UPDATE)) {
			//	It matches the (hopefully) unique WIDGET_UPDATE string, so perform an update
			//	Must get the widget IDs for the updates to be performed
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName mAppWidget = new ComponentName(context.getPackageName(), Widget.class.getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(mAppWidget);
			//	Finally, call the update method
			onUpdate(context, appWidgetManager, appWidgetIds);
		} else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)) {
			//	Fix for an Android 1.5 issue, only added just-in-case someone on 1.5 uses this
			final int appWidgetId = intent.getExtras()
					.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				onDeleted(context, new int[] { appWidgetId });
			}
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//	Get all Ids
		ComponentName mWidget = new ComponentName(context, Widget.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(mWidget);

		//	Build the Intent to call the Service
		Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		//	Alternative here to register the button onClickPendingIntent:
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		//	Check out the PendingIntent we have stored
		if (!(mPendingIntent instanceof PendingIntent)) {
			Intent update = new Intent(Widget.WIDGET_UPDATE);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, update, 0);
			mPendingIntent = pendingIntent;
		}
		remoteView.setOnClickPendingIntent(R.id.refresh_button_widget, mPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteView);

		//	Update the widgets via the service
		context.startService(intent);
	}

	@Override
	public void onDisabled(Context context) {
		mAlarmManager.cancel(mPendingIntent);
		super.onDisabled(context);
	}

	public static void saveAlarmManager(AlarmManager alarmManager, PendingIntent pendingIntent) {
		mAlarmManager = alarmManager;
		mPendingIntent = pendingIntent;
	}

	public static PendingIntent getUpdatePendingIntent() {
		return mPendingIntent;
	}
}
