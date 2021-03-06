package com.fdesousa.android.WheresMyTrain.UiElements;

/******************************************************************************
 * Copyright 2011 Filipe De Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.widget.TextView;

import com.fdesousa.android.WheresMyTrain.R;

/**
 * <b>UiControllerMain</b>
 * <p>
 * Convenience and utility class for handling some UI widgets, and useful variables.<br/>
 * Provides access to useful variables (i.e. Typefaces, Application Resources,
 * etc.) and controls certain UI widgets for the application.
 * </p>
 * Extends UiController to add specific methods for WheresMyTrain's main Activity
 * @author Filipe De Sousa
 * @version 0.8
 */
public class UiControllerMain extends UiController {

	public UiControllerMain(Resources resources, AssetManager assetManager,
			boolean titleBarVisibility, Activity activity) {
		super(resources, assetManager, titleBarVisibility, activity);
		buildExitConfirmationDialog();
		buildAboutDialog();
		buildLineStatusDialog();
	}

	// ----------------------------------------------------------------------
	// Quit dialog variables, builder and methods
	private DialogInterface.OnClickListener exitConfirmationDialogClickListener;
	private AlertDialog exitConfirmation;

	/**
	 * Method to build the Exit Confirmation dialog showed when pressing the
	 * back button.
	 */
	private void buildExitConfirmationDialog() {
		// Setup the quitDialogClickListener so we know what to do when back is
		// pressed
		exitConfirmationDialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					activity.finish();
				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
				}
			}
		};
		// Setup the AlertDialog.Builder to display the dialog later
		exitConfirmation = new AlertDialog.Builder(activity)
				.setMessage("Exit Where's My Train?")
				.setPositiveButton(android.R.string.yes,
					exitConfirmationDialogClickListener)
				.setNegativeButton(android.R.string.no,
					exitConfirmationDialogClickListener)
				.create();
		// Setup of Dialog finished
	}

	/**
	 * Convenience method to display the Exit confirmation dialog
	 */
	public void displayExitConfirmationDialog() {
		exitConfirmation.show();
	}

	// About dialog variables, builder and methods
	private AlertDialog aboutDialog;

	private void buildAboutDialog() {
		String title = String.format("About %s",
				resources.getString(R.string.app_name));
		final SpannableString s = new SpannableString(
				resources.getString(R.string.about_text));
		Linkify.addLinks(s, Linkify.ALL);

		aboutDialog = new AlertDialog.Builder(activity)
				.setTitle(title)
				.setMessage(s)
				.setCancelable(true)
				.setIcon(R.drawable.ic_launcher)
				.setPositiveButton(resources.getString(android.R.string.ok),
						null).create();
	}

	public void displayAboutDialog() {
		aboutDialog.show();
	}

	// Line Status information dialog variables, builder and methods
	private AlertDialog lineStatusDialog;

	private void buildLineStatusDialog() {
		lineStatusDialog = new AlertDialog.Builder(activity)
				.setCancelable(true)
				.setPositiveButton(resources.getString(android.R.string.ok),
						null).create();
	}

	public void setLineStatusDialogText(String title, String message) {
		lineStatusDialog.setTitle(title);
		lineStatusDialog.setMessage(message);
	}

	public void showLineStatusDialog() {
		lineStatusDialog.show();
	}

	// ----------------------------------------------------------------------
	// Main Activity's Custom title bar widgets and methods
	/** TextView instance for the line textview in our custom title bar */
	private TextView lineTitle;
	/** TextView instance for the station textview in our custom title bar */
	private TextView stationTitle;

	@Override
	protected void setupCustomTitleBar() {
		// Get the widget instances
		titleBar = activity.findViewById(R.id.custom_title_bar);
		lineTitle = (TextView) activity.findViewById(R.id.text_line);
		lineTitle.setTypeface(bold);
		stationTitle = (TextView) activity.findViewById(R.id.text_station);
		stationTitle.setTypeface(bold);
	}

	@Override
	public void refreshMainTitleBar(String... params) {
		// Set colours and text of widgets
		titleBar.setBackgroundColor(textColour);
		String line = params.length >= 1 ? params[0] : "";
		String station = params.length >= 2 ? params[1] : "";
		//	Check out and cut down line
		if (line.length() > 15) {
			lineTitle.setText(line.substring(0, 15));
		} else {
			lineTitle.setText(line);
		}
		//	Check out and cut down station
		if (station.length() > 15) {
			stationTitle.setText(station.substring(0, 15));
		} else {
			stationTitle.setText(station);
		}
	}

}
