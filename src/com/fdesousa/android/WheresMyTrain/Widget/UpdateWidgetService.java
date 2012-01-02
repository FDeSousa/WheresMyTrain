package com.fdesousa.android.WheresMyTrain.Widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.UiElements.UiControllerConfig;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		//	Get all of the widget IDs
		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		SharedPreferences settings = this.getSharedPreferences(Widget.TAG, Context.MODE_PRIVATE);

		//	Make the view. Can be generic for all of the widgets
		RemoteViews remoteView = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);
		//	Instantiate TflJsonReader once before all of the widgets are updated
		TflJsonReader mJsonR = new TflJsonReader(getCacheDir());
		//	Create the PendingIntent for updates when clicking the button
		//Intent update = new Intent(Widget.WIDGET_UPDATE);
		//PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, update, 0);

		//	Loop through, updating all widgets
		for (int widgetId : allWidgetIds) {
			//	Set the onClickPendingIntent for each widget
			//remoteView.setOnClickPendingIntent(R.id.refresh_button_widget, pendingIntent);
			//	Get the values from shared preferences
			String lineKey = Widget.LINE_PREFS_KEY + widgetId;
			String stationKey = Widget.STATION_PREFS_KEY + widgetId;
			String line = settings.getString(lineKey, "b");
			String station = settings.getString(stationKey, "chx");

			//	Request and parse the data
			DPContainer result = mJsonR.getDetailedPredictions(line, station);

			//	Setup the view with the new data
			//	First the title bar
			remoteView.setTextViewText(R.id.line_text_widget, result.information.linename);
			remoteView.setTextViewText(R.id.station_text_widget, result.stations.get(0).stationname);
			UiControllerConfig.setWidgetTitleShape(remoteView, line);

			//	Second the informational portion of the widget
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm.ss");
			String cTime = sdf.format(new Date());
			remoteView.setTextViewText(R.id.text_results_widget, "Updated: " + cTime);

			//	Finally, refresh the view
			appWidgetManager.updateAppWidget(widgetId, remoteView);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
