package com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.UiElements.PlatformsExpListAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;

public class DetailedPredictionsAsyncTask extends AsyncTask<Void, Void, DetailedPredictionsContainer> {
	private PlatformsExpListAdapter mPlatformAdapter;
	private TflJsonReader<DetailedPredictionsContainer> mJsonR;
	private Activity activity;
	private UiController uiController;

	/**
	 * Expandable List used for display Platforms and predicted Trains'
	 * destination and timing
	 */
	private ExpandableListView predictionsList;
	
	public DetailedPredictionsAsyncTask(Activity activity, ExpandableListView predictionsList, UiController uiController,
			final String line, final String station) {

		this.activity = activity;
		this.uiController = uiController;
		this.predictionsList = predictionsList;
		mJsonR = new DetailedPredictionsReader(line, station);
	}
	
	@Override
	protected void onPreExecute() {
		if (mPlatformAdapter instanceof PlatformsExpListAdapter) {
			// Clean out the list when refreshing, to rid ourselves of any
			// old data hanging about
			mPlatformAdapter.clearList();
			predictionsList.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected DetailedPredictionsContainer doInBackground(Void... params) {
		// Send the request to prepare the JSON data while other stuff goes on
		// Get the prepared JSON data now to fill the spinners
		return mJsonR.get();
	}

	@Override
	protected void onPostExecute(DetailedPredictionsContainer result) {
		if (result == null || result.stations.isEmpty()) {
			Toast.makeText(activity, "Please pick a line and station first.", Toast.LENGTH_LONG).show();
			return;
		}
		// Because of how tfl.php sends predictions data, there is only ever
		// ONE station in stations array
		mPlatformAdapter = new PlatformsExpListAdapter(result.stations.get(0).platforms,
				activity.getLayoutInflater(), uiController);
		// (Re)set the adapter onto the ExpandableListView
		predictionsList.setAdapter(mPlatformAdapter);
		// Show the expandable list view, to show new predictions
		predictionsList.setVisibility(View.VISIBLE);
	}
}
