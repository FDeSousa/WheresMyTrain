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
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

public class WheresMyTrain extends Activity {
	public static final String TAG = "WheresMyTrain";
	public static WheresMyTrain INSTANCE;

	public Typeface book;
	public Typeface bold;

	private Spinner linesSpinner;
	private Spinner stationsSpinner;
	private ExpandableListView predictionsList;
	private SLLine line;
	private SLStation station;
	private int textColour;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detailed_predictions);

		INSTANCE = this;
		book = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Book.otf");
		bold = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Bold.otf");
		

		final TflJsonReader mJsonR = new TflJsonReader(getCacheDir());
		//	Send the request to prepare the JSON data
		mJsonR.prepareStationsList();

		//	Initialise the display widgets
		linesSpinner = (Spinner) findViewById(R.id.lines_spinner);
		stationsSpinner = (Spinner) findViewById(R.id.stations_spinner);
		predictionsList = (ExpandableListView) findViewById(R.id.platforms_list);

		//	Get the prepared JSON data now to fill the spinners
		SLContainer sList = mJsonR.getStationsList();
		//	Displays stylised list of TfL lines
		LinesSpinnerAdapter mLineAdapter = new LinesSpinnerAdapter(sList.lines);
		linesSpinner.setAdapter(mLineAdapter);

		//	On initialisation, when setting colours, Waterloo & City line colours are used first
		//+ as textColour is changed when filling the Lines spinner, reset colour here
		//+	For safety, since Bakerloo is shown first, use Bakerloo colours
		textColour = getResources().getColor(R.color.bakerloo_colour);

		linesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//	Handle the item selected by setting the second spinner with the correct stations
				line = (SLLine) parent.getItemAtPosition(pos);
				StationsSpinnerAdapter mStationAdapter = new StationsSpinnerAdapter(line.stations, line.linecode);
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
				mJsonR.preparePredictionsDetailed(line.linecode, station.stationcode);
				DPContainer sPredictions = mJsonR.getPredictionsDetailed();
				//	Always get the first element in sPredictions.stations as tfl.php only includes 1 station
				//+ per Detailed Predictions request, but places it into an array.
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

	public int getLineColour(String linecode) {
		Resources r = getResources();
		int colour = 0;
		
		if (linecode.equalsIgnoreCase("b"))
			colour = r.getColor(R.color.bakerloo_colour);
		else if (linecode.equalsIgnoreCase("c"))
			colour = r.getColor(R.color.central_colour);
		else if (linecode.equalsIgnoreCase("d"))
			colour = r.getColor(R.color.district_colour);
		else if (linecode.equalsIgnoreCase("h"))
			colour = r.getColor(R.color.hammersmith_colour);
		else if (linecode.equalsIgnoreCase("j"))
			colour = r.getColor(R.color.jubilee_colour);
		else if (linecode.equalsIgnoreCase("m"))
			colour = r.getColor(R.color.metropolitan_colour);
		else if (linecode.equalsIgnoreCase("n"))
			colour = r.getColor(R.color.northern_colour);
		else if (linecode.equalsIgnoreCase("p"))
			colour = r.getColor(R.color.piccadilly_colour);
		else if (linecode.equalsIgnoreCase("v"))
			colour = r.getColor(R.color.victoria_colour);
		else if (linecode.equalsIgnoreCase("w"))
			colour = r.getColor(R.color.waterloo_colour);
		
		return colour;
	}
	
	public int getTextColour() {
		return textColour;
	}

	public void setTextColour(int textColour) {
		this.textColour = textColour;
	}
}