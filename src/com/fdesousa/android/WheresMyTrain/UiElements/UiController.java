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
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.LibraryMain;

/**
 * <b>UiController</b>
 * <p>
 * Convenience and utility class for handling some UI widgets, and useful
 * variables.<br/>
 * Provides access to useful variables (i.e. Typefaces, Application Resources,
 * etc.) and controls certain UI widgets for the application.<br/>
 * As UiControllerMain and UiControllerConfig were originally unrelated, but
 * doing roughly the same job, just for separate activities, this super-class was
 * written, just to ease the load a little bit.
 * </p>
 * 
 * @author Filipe De Sousa
 * @version 0.7
 */
public abstract class UiController {
	// Fonts to use for all text
	/**
	 * Quicksand Book font to be used for standard dialogs and text, not bold,
	 * not italic
	 */
	public Typeface book;
	/**
	 * Quicksand Bold font to be used for headers, extra emphasis. Bold as name
	 * suggests
	 */
	public Typeface bold;

	protected Resources resources;
	protected Activity activity;

	public UiController(Resources resources, AssetManager assetManager,
			boolean titleBarVisibility, Activity activity) {
		this.resources = resources;
		this.activity = activity;
		book = Typeface.createFromAsset(assetManager, "fonts/Quicksand_Book.otf");
		bold = Typeface.createFromAsset(assetManager, "fonts/Quicksand_Bold.otf");
		setupCustomTitleBar();
		//	Method makes title bar disappear if it's true, so invert the boolean
		setCustomTitleBarVisibility(!titleBarVisibility);
	}
	
	// ----------------------------------------------------------------------
	//	Useful for the titlebar
	/**
	 * View instance for the custom Title bar. Useful for changing background
	 * colours. As it is only used for changing background colour, which is
	 * generic to all Views, we save the type-casting calling for that
	 */
	protected View titleBar;
	
	/**
	 * Convenience method to make the custom title bar disappear.<br/>
	 * Useful when custom title bar is not supported. Makes view invisible, take
	 * up no space
	 * 
	 * @param gone - true to make view disappear, false to make it visible
	 */
	private void setCustomTitleBarVisibility(boolean gone) {
		if (titleBar instanceof View) {
			if (gone) {
				titleBar.setVisibility(View.GONE);
			} else {
				titleBar.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void setTitleBarColour(int colour) {
		//	Check titleBar has been instantiated
		if (titleBar instanceof View
				//	Then check it's actually visible too
				&& titleBar.getVisibility() == View.VISIBLE) {
			//	Then just simply set the background colour
			titleBar.setBackgroundColor(colour);
		}
	}

	/**
	 * Utility method to do the initial instantiation and setup of the custom
	 * title bar (even if it is not displayed, though behaviour may change)
	 */
	protected abstract void setupCustomTitleBar();
	
	/**
	 * Convenience method to refresh the text and colour of the title bar
	 */
	public abstract void refreshMainTitleBar(String... params);

	// ----------------------------------------------------------------------
	//	Colours, colours and more colours
	/**
	 * Convenience method to get the right colour for the right train line
	 * 
	 * @param linecode
	 *            - the ID of the train line to find the colour for
	 * @return the integer colour code for the given train line
	 */
	public int getLineColour(String linecode) {
		int colour = 0;

		if (linecode.equals(LibraryMain.BAKERLOO_CODE))
			colour = resources.getColor(R.color.bakerloo_colour);
		else if (linecode.equals(LibraryMain.CENTRAL_CODE))
			colour = resources.getColor(R.color.central_colour);
		else if (linecode.equals(LibraryMain.DISTRICT_CODE))
			colour = resources.getColor(R.color.district_colour);
		else if (linecode.equals(LibraryMain.HAMMERSMITH_CODE))
			colour = resources.getColor(R.color.hammersmith_colour);
		else if (linecode.equals(LibraryMain.JUBILEE_CODE))
			colour = resources.getColor(R.color.jubilee_colour);
		else if (linecode.equals(LibraryMain.METROPOLITAN_CODE))
			colour = resources.getColor(R.color.metropolitan_colour);
		else if (linecode.equals(LibraryMain.NORTHERN_CODE))
			colour = resources.getColor(R.color.northern_colour);
		else if (linecode.equals(LibraryMain.PICCADILLY_CODE))
			colour = resources.getColor(R.color.piccadilly_colour);
		else if (linecode.equals(LibraryMain.VICTORIA_CODE))
			colour = resources.getColor(R.color.victoria_colour);
		else if (linecode.equals(LibraryMain.WATERLOO_CODE))
			colour = resources.getColor(R.color.waterloo_colour);

		return colour;
	}

	/**
	 * Current text colour for on-screen widgets to use. Dependent upon the
	 * current Line
	 */
	protected int textColour;

	/**
	 * Simple getter for text colour
	 * 
	 * @return integer value that textColour is set to
	 */
	public int getTextColour() {
		return textColour;
	}

	/**
	 * Simple setter for text colour
	 * 
	 * @param textColour
	 *            integer value to set textColour to
	 */
	public void setTextColour(int textColour) {
		this.textColour = textColour;
	}
	
	/**
	 * Convenience method to decide if details string is too short/is empty.<br/>
	 * If so, return the description string instead. Useful for line status dialog
	 * @param details - the line status details for a specific line
	 * @param description - the line status description for a specific line
	 * @return the string that should be displayed in the dialog
	 */
	public static final String decideMessageChoice(final String details, final String description) {
		String outDetails;
		if (details.length() < 1) {
			// If so, use the description text instead, which is never empty
			outDetails = description;
		} else {
			// If it's a long message, use the text
			outDetails = details;
		}
		return outDetails;
	}
}
