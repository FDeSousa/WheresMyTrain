package com.fdesousa.android.WheresMyTrain.Library.requests.StationsList;

import android.os.AsyncTask;

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;

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
	private TflJsonReader<StationsListContainer> mJsonR;

	public StationsListAsyncTask(TflJsonReader<StationsListContainer> mJsonR) {
		this.mJsonR = mJsonR;
	}

	@Override
	protected StationsListContainer doInBackground(Void... params) {
		// Send the request to prepare the JSON data while other stuff goes on
		// Get the prepared JSON data now to fill the spinners
		return mJsonR.get();
	}

}
