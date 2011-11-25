package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

import java.util.ArrayList;
import java.util.List;

public class DetailedPredictionsStation {
	String stationcode;
	String stationname;
	List<DetailedPredictionsPlatform> platforms = new ArrayList<DetailedPredictionsPlatform>();
}
