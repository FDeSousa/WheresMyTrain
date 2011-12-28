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

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.WheresMyTrain;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

/**
 * <b>UiController</b>
 * <p>
 * Convenience and utility class for handling some UI widgets, and useful
 * variables.<br/>
 * Provides access to useful variables (i.e. Typefaces, Application Resources,
 * etc.) and controls certain UI widgets for the application.
 * </p>
 * Extends UiController to add specific methods for WheresMyTrain's main Activity
 * @author Filipe De Sousa
 * @version 0.7
 */
public class UiControllerConfig extends UiController {

	private View titleBar;
	private TextView lineTitle;

	public UiControllerConfig(Resources resources, AssetManager assetManager) {
		super(resources, assetManager);
		setupCustomTitleBar();
	}

	private void setupCustomTitleBar() {
		WheresMyTrain w = WheresMyTrain.INSTANCE;
		// Get the widget instances
		titleBar = w.findViewById(R.id.custom_title_bar_config);
		lineTitle = (TextView) w.findViewById(R.id.text_choose_line_config);
		lineTitle.setTypeface(bold);
		w.findViewById(R.id.custom_title_bar_btn_back_config).setVisibility(View.INVISIBLE);
	}
}
