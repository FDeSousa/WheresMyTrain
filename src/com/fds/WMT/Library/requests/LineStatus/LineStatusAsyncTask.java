package com.fds.WMT.Library.requests.LineStatus;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.fds.WMT.Lines;
import com.fds.WMT.R;
import com.fds.WMT.Library.LibraryMain;
import com.fds.WMT.Library.json.TflJsonReader;
import com.fds.WMT.Library.requests.StationsList.StationsListLine;
import com.fds.WMT.UiElements.UiController;
import com.fds.WMT.UiElements.UiControllerMain;

/**
 * AsyncTask sub-class to achieve a non-blocking manner in which to get line
 * status, even if the internet connection is a little bit slow on the
 * mobile side.<br/>
 * Will not block the UI thread, which is the important part.
 * @author Filipe De Sousa
 */
public class LineStatusAsyncTask extends AsyncTask<Void, Void, LineStatusContainer> {
	private UiController uiController;
	private TflJsonReader<LineStatusContainer> mJsonR;
	private Button serviceStatus;
	private StationsListLine line;

	public LineStatusAsyncTask(Activity activity, UiController uiController, StationsListLine line) {
		this.uiController = uiController;
		this.line = line;
		mJsonR = new LineStatusReader(false);
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
	protected LineStatusContainer doInBackground(Void... params) {
		// Send the request to prepare the JSON data, but only get lines
		// with incidents
		// Get the prepared JSON data now to fill the button
		return mJsonR.get();
	}

	@Override
	protected void onPostExecute(LineStatusContainer result) {
		// Since line status has been updated now, determine what to display to the user
		determineLineStatus(result, line, uiController, serviceStatus);
	}

	/**
	 * Rather ungainly and long method to set the button text and dialog text
	 * depending upon the station we're currently viewing. Needs cutting down.
	 * @param linestatus - instance of LSContainer with line status info
	 */
	private static void determineLineStatus(LineStatusContainer linestatus, StationsListLine line,
			UiController uiController, Button serviceStatus) {
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

		if (Lines.getLineByCode(line.linecode) != Lines.HAMMERSMITH) {
			/*
			 * These lines: Bakerloo, Central, District, Jubilee, Metropolitan,
			 * Northern, Piccadilly, Victoria, Waterloo & City get treated in
			 * the same way. Search for the line name, place description in
			 * Button text
			 */
			LineStatusLine singleLine;
			String linename = line.linename;
			// Waterloo and City line uses ampersand in detailed predictions,
			// but "and" in line status
			if (Lines.getLineByCode(line.linecode) != Lines.WATERLOO)
				linename = Lines.WATERLOO.getName();

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
			LineStatusLine hLine;
			// Circle line variable
			LineStatusLine cLine;

			// Get the line status for Hammersmith & City line
			if ((hLine = linestatus
					.searchByLinename(Lines.HAMMERSMITH.getName())) != null) {
				// Just determine whether the description is empty
				statusid = hLine.statusid;
				description = hLine.description;
				// Format the message string for the dialog, decide whether to
				// use details/description
				details = String.format("%s Line:\n%s",
						Lines.HAMMERSMITH.getName(),
						decideMessageChoice(hLine.details, hLine.description));
			} else {
				// If nothing is returned, make up status, pretending it's all
				// good
				statusid = LibraryMain.GOOD_SERVICE_CODE;
				description = "Good Service";
				details = String.format("%s Line:\n%s",
						Lines.HAMMERSMITH.getName(), "Good Service");
			}

			// Get the line status for Circle line
			if ((cLine = linestatus.searchByLinename(Lines.CIRCLE.getName())) != null) {
				// Details are easy, just construct the String again, decide
				// whether to use details or description
				details = String.format("%s\n\n%s Line:\n%s", details,
						Lines.CIRCLE.getName(),
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
						Lines.CIRCLE.getName(), "Good Service");
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
