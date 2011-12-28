package com.fdesousa.android.WheresMyTrain.Widget;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions.DPContainer;

import android.app.Service;
import android.appwidget.AppWidgetManager;
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
		
		SharedPreferences settings = getSharedPreferences("com.fdesousa.android.WheresMyTrain.Widget", MODE_PRIVATE);
		String line = settings.getString("line", "b");
		String station = settings.getString("station", "chx");
		
		//	Loop through, updating all widgets I guess?
		for (int widgetId : allWidgetIds) {
			//	Make the view
			RemoteViews remoteView = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);
			
			//	Setup the results for the request
			TflJsonReader mJsonR = new TflJsonReader(getCacheDir());
			DPContainer result = mJsonR.getDetailedPredictions(line, station);

			remoteView.setTextViewText(R.id.text_widget, result.toString());
			
			appWidgetManager.updateAppWidget(widgetId, remoteView);
		}
		stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
