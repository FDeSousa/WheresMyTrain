package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

import java.util.ArrayList;
import java.util.List;

public class DPStation {
	public String stationcode;
	public String stationname;
	public List<DPPlatform> platforms = new ArrayList<DPPlatform>();
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		
		out.append("\n\tstationcode:");
		out.append(stationcode);
		out.append("\n\tstationname:");
		out.append(stationname);
		out.append("\n\tplatforms:{");
		
		for (DPPlatform platform : platforms) {
			out.append(platform.toString());
		}
		
		out.append("\n\t}");
		
		return out.toString();
	}
}
