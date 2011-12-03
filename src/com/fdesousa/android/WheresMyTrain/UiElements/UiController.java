package com.fdesousa.android.WheresMyTrain.UiElements;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLStation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class UiController {
	//	Fonts to use for all text
	/**	Quicksand Book font to be used for standard dialogs and text, not bold, not italic		*/
	public Typeface book;
	/**	Quicksand Bold font to be used for headers, extra emphasis. Bold as name suggests		*/
	public Typeface bold;
	
	private Resources resources;
	
	public UiController(Resources resources, AssetManager assetManager) {
		buildExitConfirmationDialog();
		this.resources = resources;
		book = Typeface.createFromAsset(assetManager, "fonts/Quicksand_Book.otf");
		bold = Typeface.createFromAsset(assetManager, "fonts/Quicksand_Bold.otf");
	}

	//	Quit dialog variables, builder and methods
	private DialogInterface.OnClickListener exitConfirmationDialogClickListener;
	private AlertDialog.Builder exitConfirmation;

	/**
	 * Method to build the Exit Confirmation dialog showed when pressing the back button.
	 */
	private void buildExitConfirmationDialog() {
		//	Setup the quitDialogClickListener so we know what to do when back is pressed
		exitConfirmationDialogClickListener = new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE: WheresMyTrain.INSTANCE.finish();
				case DialogInterface.BUTTON_NEGATIVE: dialog.cancel();
				}
			}
		};
		//	Setup the AlertDialog.Builder to display the dialog later
		exitConfirmation = new AlertDialog.Builder(WheresMyTrain.INSTANCE)
			.setMessage("Exit Where's My Train?")
			.setPositiveButton(android.R.string.yes, exitConfirmationDialogClickListener)
			.setNegativeButton(android.R.string.no, exitConfirmationDialogClickListener);
		//	Setup of Dialog finished
	}

	/**
	 * Convenience method to display the Exit confirmation dialog
	 */
	public void displayExitConfirmationDialog() {
		exitConfirmation.show();
	}
	
	//	Custom title bar widgets and methods
	/**	View instance for the custom Title bar. Useful for changing background colours.
	 *	As it is only used for changing background colour, which is generic to all Views,
	 *	we save the type-casting calling for that
	 */
	private View titleBar;
	/**	TextView instance for the line textview in our custom title bar							*/
	private TextView lineTitle;
	/**	TextView instance for the station textview in our custom title bar						*/
	private TextView stationTitle;
	
	/**
	 * Utility method to do the initial instantiation and setup of the custom title bar
	 */
	public void setupCustomTitleBar(SLLine line, SLStation station) {
		WheresMyTrain w = WheresMyTrain.INSTANCE;
		//	Get the widget instances
		titleBar = w.findViewById(R.id.custom_title_bar);
		lineTitle = (TextView) w.findViewById(R.id.text_line);
		lineTitle.setTypeface(bold);
		stationTitle = (TextView) w.findViewById(R.id.text_station);
		stationTitle.setTypeface(bold);
		refreshTitleBar(line, station);
	}

	/**
	 * Convenience method to refresh the text and colour of the title bar
	 */
	public void refreshTitleBar(SLLine line, SLStation station) {
		//	Set colours and text of widgets
		titleBar.setBackgroundColor(textColour);
		if (line != null) {
			if (line.linename.length() > 15) {
				lineTitle.setText(line.linename.substring(0, 15));
			} else {
				lineTitle.setText(line.linename);				
			}
		}
		if (station != null) {
			if (station.stationname.length() > 15) {
				stationTitle.setText(station.stationname.substring(0, 15));
			} else {
				stationTitle.setText(station.stationname);				
			}
		}
	}
	
	/**
	 * Convenience method to get the right colour for the right train line
	 * @param linecode - the ID of the train line to find the colour for
	 * @return the integer colour code for the given train line
	 */
	public int getLineColour(String linecode) {
		int colour = 0;

		if (linecode.equals("b"))		colour = resources.getColor(R.color.bakerloo_colour);
		else if (linecode.equals("c"))	colour = resources.getColor(R.color.central_colour);
		else if (linecode.equals("d"))	colour = resources.getColor(R.color.district_colour);
		else if (linecode.equals("h"))	colour = resources.getColor(R.color.hammersmith_colour);
		else if (linecode.equals("j"))	colour = resources.getColor(R.color.jubilee_colour);
		else if (linecode.equals("m"))	colour = resources.getColor(R.color.metropolitan_colour);
		else if (linecode.equals("n"))	colour = resources.getColor(R.color.northern_colour);
		else if (linecode.equals("p"))	colour = resources.getColor(R.color.piccadilly_colour);
		else if (linecode.equals("v"))	colour = resources.getColor(R.color.victoria_colour);
		else if (linecode.equals("w"))	colour = resources.getColor(R.color.waterloo_colour);

		return colour;
	}
	

	/**	Current text colour for on-screen widgets to use. Dependent upon the current Line		*/
	private int textColour;
	/**
	 * Simple getter for text colour
	 * @return integer value that textColour is set to
	 */
	public int getTextColour() {
		return textColour;
	}

	/**
	 * Simple setter for text colour
	 * @param textColour integer value to set textColour to
	 */
	public void setTextColour(int textColour) {
		this.textColour = textColour;
	}
}
