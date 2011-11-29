package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

import java.util.ArrayList;
import java.util.List;

public class DPContainer {
	public String requesttype;
	public DPInformation information;
	public List<DPStation> stations = new ArrayList<DPStation>();
	
	/**
	 * Using for early-on diagnostics, just to avoid the slow ADB debugger
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("requesttype:");
		sb.append(requesttype);
		sb.append("\ninformation:{");
		sb.append(information.toString());
		sb.append("\nstations:{");
		
		for (DPStation station : stations) {
			sb.append(station.toString());
		}
		
		sb.append("\n}");
		return sb.toString();
	}
}
