package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

import java.util.ArrayList;
import java.util.List;

public class DPPlatform {
	public String platformname;
	public String platformnumber;
	public List<DPTrain> trains = new ArrayList<DPTrain>();
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		
		out.append("\n\t\tplatformname:");
		out.append(platformname);
		out.append("\n\t\tplatformnumber:");
		out.append(platformnumber);
		out.append("\n\t\ttrains:{");
		
		for (DPTrain train : trains) {
			out.append(train.toString());
		}
		
		out.append("\n\t\t}");
		
		return out.toString();
	}
}
