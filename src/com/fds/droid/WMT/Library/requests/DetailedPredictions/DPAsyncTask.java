package com.fds.droid.WMT.Library.requests.DetailedPredictions;

import android.app.ExpandableListActivity;
import android.os.AsyncTask;
import android.view.View;

import com.fdesousa.android.WheresMyTrain.R;
import com.fds.droid.WMT.Library.json.TflJsonReader;
import com.fds.droid.WMT.UiElements.PlatformsExpListAdapter;
import com.fds.droid.WMT.UiElements.UiController;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

public class DPAsyncTask extends AsyncTask<Void, Void, DPContainer> {
	private PlatformsExpListAdapter mPlatformAdapter;
	private TflJsonReader<DPContainer> mJsonR;
	private ExpandableListActivity activity;
	private UiController uiController;

	/**
	 * Expandable List used for display Platforms and predicted Trains'
	 * destination and timing
	 */
	private PullToRefreshExpandableListView predictionsList;
	
	public DPAsyncTask(ExpandableListActivity activity, UiController uiController, final String line, final String station) {
		this.activity = activity;
		this.uiController = uiController;
		predictionsList = (PullToRefreshExpandableListView) this.activity.findViewById(R.id.platforms_list);
		mJsonR = new DPReader(line, station);
	}
	
	@Override
	protected void onPreExecute() {
		if (mPlatformAdapter instanceof PlatformsExpListAdapter) {
			// Clean out the list when refreshing, to rid ourselves of any
			// old data hanging about
			mPlatformAdapter.clearList();
		}
	}

	@Override
	protected DPContainer doInBackground(Void... params) {
		// Send the request to prepare the JSON data while other stuff goes
		// on
		// Get the prepared JSON data now to fill the spinners
		return mJsonR.get();
	}

	@Override
	protected void onPostExecute(DPContainer result) {
		// Because of how tfl.php sends predictions data, there is only ever
		// ONE station in stations array
		mPlatformAdapter = new PlatformsExpListAdapter(result.stations.get(0).platforms, activity.getLayoutInflater(), uiController);
		// Show the expandable list view, to show new predictions
		predictionsList.setVisibility(View.VISIBLE);
		// (Re)set the adapter onto the ExpandableListView
		activity.setListAdapter(mPlatformAdapter);

		// Tell the Pull-To-Refresh library we're done with the task
		predictionsList.onRefreshComplete();

		super.onPostExecute(result);
	}
}
