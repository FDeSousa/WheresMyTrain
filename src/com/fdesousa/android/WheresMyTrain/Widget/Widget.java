package com.fdesousa.android.WheresMyTrain.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Widget extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		SharedPreferences settings = context.getSharedPreferences("com.fdesousa.android.WheresMyTrain.Widget", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putString("line", "c");
		edit.putString("station", "bnk");
		edit.commit();
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
		
		//	Update the widgets via the service
		context.startService(intent);
	}
	
	@Override
	public void onDisabled(Context context) {
		context.stopService(new Intent(context, UpdateWidgetService.class));
		super.onDisabled(context);
	}
}
