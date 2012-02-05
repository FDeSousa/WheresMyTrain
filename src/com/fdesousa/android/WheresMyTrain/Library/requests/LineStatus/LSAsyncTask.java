package com.fdesousa.android.WheresMyTrain.Library.requests.LineStatus;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.LibraryMain;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.Library.requests.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;
import com.fdesousa.android.WheresMyTrain.UiElements.UiControllerMain;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

/**
 * AsyncTask sub-class to achieve a non-blocking manner in which to get line
 * status, even if the internet connection is a little bit slow on the
 * mobile side.<br/>
 * Will not block the UI thread, which is the important part.
 * @author Filipe De Sousa
 */
public class LSAsyncTask extends AsyncTask<Void, Void, LSContainer> {
	private UiController uiController;
	private TflJsonReader<LSContainer> mJsonR;
	private Button serviceStatus;
	private SLLine line;
	
	public LSAsyncTask(Activity activity, UiController uiController, SLLine line) {
		this.uiController = uiController;
		this.line = line;
		mJsonR = new LSReader(false);
		serviceStatus = (Button) activity.findViewById(R.id.service_status);
	}
	
	private void resetLineStatusButton() {
		// Reset the status button to black and white, unknown status
		serviceStatus.setBackgroundResource(R.drawable.btn_white_basic);
		serviceStatus.setTextColor(Color.BLACK);
		serviceStatus.setTypeface(uiController.book);
		serviceStatus.setText("Unknown Status");
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).setLineStatusDialogText("Unknown Status", "Please wait");
	}
	
	@Override
	protected void onPreExecute() {
		// Just clean up the line status button
		resetLineStatusButton();
	}

	@Override
	protected LSContainer doInBackground(Void... params) {
		// Send the request to prepare the JSON data, but only get lines
		// with incidents
		// Get the prepared JSON data now to fill the button
		return mJsonR.get();
	}

	@Override
	protected void onPostExecute(LSContainer result) {
		// Since line status has been updated now, determine what to display to the user
		determineLineStatus(result, line, uiController, serviceStatus);
	}
	
	/**
	 * Rather ungainly and long method to set the button text and dialog text
	 * depending upon the station we're currently viewing. Needs cutting down.
	 * @param linestatus - instance of LSContainer with line status info
	 */
	private static void determineLineStatus(LSContainer linestatus, SLLine line, UiController uiController, Button serviceStatus) {
		// Only used for dialog box title, but still has to be set below
		String title = "";
		// Generally two-letter informational code. I.e. GS=Good Service,
		// PC=Part Closure, CS=Closed
		String statusid = "";
		// Two-word description of status (labelled description). I.e. Good
		// Service, Part Closure, Planned Closure, etc.
		String description = "";
		// One-sentence long description of status, or usually empty if Good
		// Service
		String details = "";

		if (!line.linecode.equals(LibraryMain.HAMMERSMITH_CODE)) {
			/*
			 * These lines: Bakerloo, Central, District, Jubilee, Metropolitan,
			 * Northern, Piccadilly, Victoria, Waterloo & City get treated in
			 * the same way. Search for the line name, place description in
			 * Button text
			 */
			LSLine singleLine;
			String linename = line.linename;
			// Waterloo and City line uses ampersand in detailed predictions,
			// but "and" in line status
			if (line.linecode.equals(LibraryMain.WATERLOO_CODE))
				linename = LibraryMain.WATERLOO_NAME;

			// Search for the line, check the result isn't null
			if ((singleLine = linestatus.searchByLinename(linename)) != null) {
				title = String.format("%s Line", singleLine.linename);
				statusid = singleLine.statusid;
				description = singleLine.description;
				details = decideMessageChoice(singleLine.details,
						singleLine.description);
			}
		} else {
			/*
			 * Hammersmith & City, Circle lines together Due to how H&C and
			 * Circle lines are handled by TfL in predictions, we need to search
			 * for two line status instances, compare, and show
			 */
			// Set title for Hammersmith & City and Circle lines, but make it
			// short
			title = "H & C, Circle Lines";
			// H&C line variable
			LSLine hLine;
			// Circle line variable
			LSLine cLine;

			// Get the line status for Hammersmith & City line
			if ((hLine = linestatus
					.searchByLinename(LibraryMain.HAMMERSMITH_NAME)) != null) {
				// Just determine whether the description is empty
				statusid = hLine.statusid;
				description = hLine.description;
				// Format the message string for the dialog, decide whether to
				// use details/description
				details = String.format("%s Line:\n%s",
						LibraryMain.HAMMERSMITH_NAME,
						decideMessageChoice(hLine.details, hLine.description));
			} else {
				// If nothing is returned, make up status, pretending it's all
				// good
				statusid = LibraryMain.GOOD_SERVICE_CODE;
				description = "Good Service";
				details = String.format("%s Line:\n%s",
						LibraryMain.HAMMERSMITH_NAME, "Good Service");
			}

			// Get the line status for Circle line
			if ((cLine = linestatus.searchByLinename(LibraryMain.CIRCLE_NAME)) != null) {
				// Details are easy, just construct the String again, decide
				// whether to use details or description
				details = String.format("%s\n\n%s Line:\n%s", details,
						LibraryMain.CIRCLE_NAME,
						decideMessageChoice(cLine.details, cLine.description));

				// Determine which status description to use now
				if (!cLine.statusid.equals(LibraryMain.GOOD_SERVICE_CODE)
						&& !cLine.statusid.equals(statusid)) {
					/*
					 * If Circle line statusid isn't GS, and isn't the same as
					 * current statusid, likely to be worse than H&C, set it as
					 * statusid
					 */
					statusid = cLine.statusid;
					description = cLine.description;
				}
			} else {
				// If Circle line isn't found, assume Good Service, make up
				// details
				details = String.format("%s\n\n%s Line:\n%s", details,
						LibraryMain.CIRCLE_NAME, "Good Service");
			}
		}

		// Determine button colour to use depending upon service status ID
		if (statusid.equals(LibraryMain.GOOD_SERVICE_CODE)) {
			// If Good Service (GS code), button is green
			serviceStatus.setBackgroundResource(R.drawable.btn_green_basic);
		} else if (statusid.equals(LibraryMain.CLOSED_CODE)
				|| statusid.equals(LibraryMain.SEVERE_DELAYS_CODE)) {
			// If Closed (CS code), button is red
			serviceStatus.setBackgroundResource(R.drawable.btn_red_basic);
		} else {
			// Otherwise, button is orange, assume part-closure/delays/unknown
			serviceStatus.setBackgroundResource(R.drawable.btn_orange_basic);
		}
		// Set the status message either way
		serviceStatus.setText(description);
		// Setup the appropriate text colour
		serviceStatus.setTextColor(Color.WHITE);

		// Now setup the line status dialog's title and message
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).setLineStatusDialogText(title, details);
	}
	
	/**
	 * Convenience method to decide if details string is too short/is empty.<br/>
	 * If so, return the description string instead. Useful for line status dialog
	 * @param details - the line status details for a specific line
	 * @param description - the line status description for a specific line
	 * @return the string that should be displayed in the dialog
	 */
	public static String decideMessageChoice(String details, String description) {
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

	// onClick method for the line status button
	/**
	 * OnClick method for the serviceStatus Button, as defined in layout
	 * XML</br> Just shows the line status dialog
	 * 
	 * @param v - instance of View (generally will be the Button itself)
	 */
	public void showLineStatus(View v) {
		if (uiController instanceof UiControllerMain)
			((UiControllerMain) uiController).showLineStatusDialog();
	}
}
