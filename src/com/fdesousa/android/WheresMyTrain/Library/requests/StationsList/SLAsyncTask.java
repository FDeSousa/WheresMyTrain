package com.fdesousa.android.WheresMyTrain.Library.requests.StationsList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Spinner;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.UiElements.LinesSpinnerAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;

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
public class SLAsyncTask extends AsyncTask<Void, Void, SLContainer> {
	private Activity activity;
	private UiController uiController;
	private TflJsonReader<SLContainer> mJsonR;
	private LinesSpinnerAdapter mLineAdapter;
	private Spinner linesSpinner;

	public SLAsyncTask(Activity activity, UiController uiController) {
		this.activity = activity;
		this.uiController = uiController;
		this.mJsonR = new SLReader(this.activity.getCacheDir());
		linesSpinner = (Spinner) this.activity.findViewById(R.id.lines_spinner);
	}
	
	@Override
	protected SLContainer doInBackground(Void... params) {
		// Send the request to prepare the JSON data while other stuff goes on
		// Get the prepared JSON data now to fill the spinners
		return mJsonR.get();
	}

	@Override
	protected void onPostExecute(SLContainer result) {
		// Initialise the Lines Spinner adapter
		mLineAdapter = new LinesSpinnerAdapter(result.lines, activity.getLayoutInflater(), uiController);
		// Set the adapter onto the Lines Spinner
		linesSpinner.setAdapter(mLineAdapter);
	}
}
