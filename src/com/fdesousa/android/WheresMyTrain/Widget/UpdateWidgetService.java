package com.fdesousa.android.WheresMyTrain.Widget;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.UiElements.UiControllerConfig;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		//	Get all of the widget IDs
		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		SharedPreferences settings = this.getSharedPreferences(Widget.TAG, Context.MODE_PRIVATE);

		//	Loop through, updating all widgets I guess?
		for (int widgetId : allWidgetIds) {
			//	Make the view, stop showing the refresh button
			RemoteViews remoteView = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);

			//	Get the values from shared preferences
			String lineKey = Widget.LINE_PREFS_KEY + widgetId;
			String stationKey = Widget.STATION_PREFS_KEY + widgetId;
			String lineColourKey = Widget.LINE_COLOUR_KEY + widgetId;
			String line = settings.getString(lineKey, "b");
			String station = settings.getString(stationKey, "chx");
			int lineColour = settings.getInt(lineColourKey, 0);

			//	Request and parse the data
			TflJsonReader mJsonR = new TflJsonReader(getCacheDir());
			DPContainer result = mJsonR.getDetailedPredictions(line, station);

			//	Setup the view with the new data
			remoteView.setTextViewText(R.id.line_text_widget, result.information.linename);
			remoteView.setTextViewText(R.id.station_text_widget, result.stations.get(0).stationname);
			UiControllerConfig.setWidgetTitleShape(remoteView, line);
			//	Finally, refresh the view
			appWidgetManager.updateAppWidget(widgetId, remoteView);
		}
		stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static PendingIntent makeControlPendingIntent(Context context, String command, int appWidgetId) {
		Intent active = new Intent(context, UpdateWidgetService.class);
		active.setAction(command);
		active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// Uri data makes PendingIntent unique, to avoid updating on FLAG_UPDATE_CURRENT
		// If there are multiple widgets, they won't override each other
		Uri data = Uri.withAppendedPath(
				Uri.parse("://widget/id/#" + command + appWidgetId),
				String.valueOf(appWidgetId));
		active.setData(data);
		return(PendingIntent.getService(context, 0, active, PendingIntent.FLAG_UPDATE_CURRENT));
	}

}
