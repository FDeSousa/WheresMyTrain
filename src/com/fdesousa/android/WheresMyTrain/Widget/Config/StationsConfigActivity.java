package com.fdesousa.android.WheresMyTrain.Widget.Config;

import com.fdesousa.android.WheresMyTrain.UiElements.StationsPickerActivity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * <h1>Stations.java</h1>
 * StationPickerActivity subclass which changes little about its
 * behaviour, apart from setting an extra in return bundle, to make
 * it more relevant to configuration of a widget.
 * @see com.fdesousa.android.WheresMyTrain.UiElements.StationsPickerActivity
 * @author Filipe De Sousa
 */
public class StationsConfigActivity extends StationsPickerActivity {
	private int appWidgetId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		//	Get the extras from our passed-in Intent
		Bundle extras = getIntent().getExtras();
		this.appWidgetId = extras
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	
		// Setup the cancel result first, in case the user cancels this activity early
		setResult(RESULT_CANCELED, new Intent()
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId));
	}
}
