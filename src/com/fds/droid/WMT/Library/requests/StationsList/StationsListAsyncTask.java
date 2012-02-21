package com.fds.droid.WMT.Library.requests.StationsList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Spinner;

import com.fdesousa.android.WheresMyTrain.R;
import com.fds.droid.WMT.Library.json.TflJsonReader;
import com.fds.droid.WMT.UiElements.LinesSpinnerAdapter;
import com.fds.droid.WMT.UiElements.UiController;

/**
 * AsyncTask sub-class to achieve a non-blocking manner in which to get the
 * stations list, even if the Internet connection is a little bit slow on
 * the mobile side.<br/>
 * Stations List can sometimes take several seconds to process on the
 * server-side alone, for which amount of time, the UI thread would be
 * blocked otherwise.<br/>
 * Will not block the UI thread, which is the important part.
 * @author Filipe De Sousa
 */
public class StationsListAsyncTask extends AsyncTask<Void, Void, StationsListContainer> {
	private Activity activity;
	private UiController uiController;
	private TflJsonReader<StationsListContainer> mJsonR;
	private LinesSpinnerAdapter mLineAdapter;
	private Spinner linesSpinner;

	public StationsListAsyncTask(Activity activity, UiController uiController) {
		this.activity = activity;
		this.uiController = uiController;
		this.mJsonR = new StationsListReader(this.activity.getCacheDir());
		linesSpinner = (Spinner) this.activity.findViewById(R.id.lines_spinner);
	}
	
	@Override
	protected StationsListContainer doInBackground(Void... params) {
		// Send the request to prepare the JSON data while other stuff goes on
		// Get the prepared JSON data now to fill the spinners
		return mJsonR.get();
	}

	@Override
	protected void onPostExecute(StationsListContainer result) {
		// Initialise the Lines Spinner adapter
		mLineAdapter = new LinesSpinnerAdapter(result.lines, activity.getLayoutInflater(), uiController);
		// Set the adapter onto the Lines Spinner
		linesSpinner.setAdapter(mLineAdapter);
	}
}
