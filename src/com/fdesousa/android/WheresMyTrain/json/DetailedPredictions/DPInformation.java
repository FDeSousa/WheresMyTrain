package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

public class DPInformation {
	public String created;
	public String linecode;
	public String linename;

	@Override
	public String toString() {
		String out = "\n\tcreated:" + created
				+ "\n\tlinecode:" + linecode
				+ "\n\tlinename:" + linename;
		return out;
	}
}
