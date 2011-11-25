package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

import java.util.ArrayList;
import java.util.List;

public class DetailedPredictionsContainer {
	public String requesttype;
	public DetailedPredictionsInformation information;
	public List<DetailedPredictionsStation> stations = new ArrayList<DetailedPredictionsStation>();
}
