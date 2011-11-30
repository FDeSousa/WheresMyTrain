package com.fdesousa.android.WheresMyTrain;

/*****************************************************************************************************
 *	Copyright (c) 2011 Filipe De Sousa
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *	associated documentation files (the "Software"), to deal in the Software without restriction,
 *	including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *	sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or
 *	substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *	NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *	DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************************/

import com.fdesousa.android.WheresMyTrain.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLLine;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLStation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

public class WheresMyTrain extends Activity {
	public static final String TAG = "WheresMyTrain";
	public static WheresMyTrain INSTANCE;
	private Spinner linesSpinner;
	private Spinner stationsSpinner;
	private ExpandableListView predictionsList;
	private SLLine line;
	private SLStation station;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		INSTANCE = this;

		final TflJsonReader mJsonR = new TflJsonReader(getCacheDir());
		
		/* Older code, previously for testing
		setContentView(R.layout.main);
		TextView tvMain = (TextView) findViewById(R.id.tvMain);
		tvMain.setMovementMethod(new ScrollingMovementMethod());

		DPContainer mList = mJsonR.getPredictionsDetailed('c', "bnk");

		tvMain.setText(mList.toString());
		//	Works. Prints out all of the strings from all of the nested classes and arrays
		*/

		//	Now try out the second interface layout. Two spinners, one expandable list
		setContentView(R.layout.detailed_predictions);

		SLContainer sList = mJsonR.getStationsList();

		//	Initialise the display widgets
		linesSpinner = (Spinner) findViewById(R.id.lines_spinner);
		stationsSpinner = (Spinner) findViewById(R.id.stations_spinner);
		predictionsList = (ExpandableListView) findViewById(R.id.platforms_list);
		
		//	Displays unstylized list of TfL lines
		LinesSpinnerAdapter mLineAdapter = new LinesSpinnerAdapter(sList.lines);
		linesSpinner.setAdapter(mLineAdapter);
		
		linesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the item selected by setting the second spinner with the correct stations
				line = (SLLine) parent.getItemAtPosition(pos);
				StationsSpinnerAdapter mStationAdapter = new StationsSpinnerAdapter(line.stations);
				stationsSpinner.setAdapter(mStationAdapter);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing
			}
		});
		
		stationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the selected item by getting detailed predictions for that line & station
				//+	choice, displaying that in the ExpandableListView
				station = (SLStation) parent.getItemAtPosition(pos);
				DPContainer sPredictions = mJsonR.getPredictionsDetailed(line.linecode, station.stationcode);
				//	Always get the first element in sPredictions.stations as tfl.php only includes 1 station
				//+ per Detailed Predictions request, but places it into an array. Future-proofing.
				PlatformsExpListAdapter platformsList = new PlatformsExpListAdapter(sPredictions.stations.get(0).platforms);
				predictionsList.setAdapter(platformsList);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing	
			}
		});
	}

	/**
	 * Simple, easy, dirty static method for showing Toast messages from any class
	 * @param message
	 */
	public final static void displayToast(final String message) {
		Toast.makeText(INSTANCE.getApplicationContext(), message, Toast.LENGTH_LONG);
	}
}