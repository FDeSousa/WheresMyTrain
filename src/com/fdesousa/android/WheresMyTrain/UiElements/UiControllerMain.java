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
import android.content.res.AssetManager;
import android.content.res.Resources;

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

	public UiControllerMain(Resources resources, AssetManager assetManager, Activity activity) {
		super(resources, assetManager, activity);
		buildLineStatusDialog();
	}

	// ----------------------------------------------------------------------
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

}
