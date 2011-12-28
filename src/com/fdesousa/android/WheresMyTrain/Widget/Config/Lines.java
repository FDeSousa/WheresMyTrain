package com.fdesousa.android.WheresMyTrain.Widget.Config;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.UiElements.UiControllerConfig;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Lines extends ListActivity {

	private boolean customTitleBar;
	private static UiControllerConfig UI_CONTROLLER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Requesting window features must be done before anything else, so do it now
		customTitleBar = requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the content view to our main layout
		setContentView(R.layout.detailed_predictions);
		UI_CONTROLLER = new UiControllerConfig(getResources(), getAssets());

        // get the appWidgetId of the appWidget being configured
        int appWidgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        // Setup the cancel result first, in case the user cancels this activity early
        setResult(RESULT_CANCELED, new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId));		
	}
}
