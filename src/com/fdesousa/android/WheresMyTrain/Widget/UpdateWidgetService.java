package com.fdesousa.android.WheresMyTrain.Widget;

import com.fdesousa.android.WheresMyTrain.R;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		//	Get all of the widget IDs
		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		
		//	Loop through, updating all widgets I guess?
		for (int widgetId : allWidgetIds) {
			//	Make the view
			RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);
			
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
