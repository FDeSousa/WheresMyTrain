package com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions;

import android.app.ExpandableListActivity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ExpandableListView;

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.UiElements.PlatformsExpListAdapter;
import com.fdesousa.android.WheresMyTrain.UiElements.UiController;

public class DetailedPredictionsAsyncTask extends AsyncTask<Void, Void, DetailedPredictionsContainer> {
	private PlatformsExpListAdapter mPlatformAdapter;
	private TflJsonReader<DetailedPredictionsContainer> mJsonR;
	private ExpandableListActivity activity;
	private UiController uiController;

	/**
	 * Expandable List used for display Platforms and predicted Trains'
	 * destination and timing
	 */
	private ExpandableListView predictionsList;
	
	public DetailedPredictionsAsyncTask(ExpandableListActivity activity, UiController uiController,
			final String line, final String station) {

		this.activity = activity;
		this.uiController = uiController;
		predictionsList = (ExpandableListView) this.activity.findViewById(android.R.id.list);
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
		if (result == null) return;	//	Early return if it's null
		// Because of how tfl.php sends predictions data, there is only ever
		// ONE station in stations array
		mPlatformAdapter = new PlatformsExpListAdapter(result.stations.get(0).platforms,
				activity.getLayoutInflater(), uiController);
		// (Re)set the adapter onto the ExpandableListView
		activity.setListAdapter(mPlatformAdapter);
		// Show the expandable list view, to show new predictions
		predictionsList.setVisibility(View.VISIBLE);
	}
}
