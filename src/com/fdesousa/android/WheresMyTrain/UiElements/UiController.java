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

import com.fdesousa.android.WheresMyTrain.Lines;

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

	public UiController(Resources resources, AssetManager assetManager, Activity activity) {
		this.resources = resources;
		this.activity = activity;
		book = Typeface.createFromAsset(assetManager, "fonts/Quicksand_Book.otf");
		bold = Typeface.createFromAsset(assetManager, "fonts/Quicksand_Bold.otf");
	}
	
	// ----------------------------------------------------------------------
	//	Colours, colours and more colours
	/**
	 * Convenience method to get the right colour for the right train line
	 * @param linecode - the ID of the train line to find the colour for
	 * @return the integer colour code for the given train line
	 */
	public int getLineColour(String linecode) {
		int colour = 0;
		try {
			colour = Lines.getLineByCode(linecode).getColourCode();
		} catch (NullPointerException e) {
			//	Someone forgot to set resources beforehand, set them now
			Lines.setResources(resources);
			colour = Lines.getLineByCode(linecode).getColourCode();
		}
		return colour;
	}

	/**
	 * Current text colour for on-screen widgets to use. Dependent upon the
	 * current Line
	 */
	protected int textColour;

	/**
	 * Simple getter for text colour
	 * @return integer value that textColour is set to
	 */
	public int getTextColour() {
		return textColour;
	}

	/**
	 * Simple setter for text colour
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
