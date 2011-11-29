package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

public class DPTrain {
	public String lcid;
	public String secondsto;
	public String timeto;
	public String location;
	public String destination;
	public String destcode;
	public String tripno;
	
	@Override
	public String toString() {
		return "\n\t\t\tlcid:" + lcid
				+ "\n\t\t\tsecondsto:" + secondsto
				+ "\n\t\t\ttimeto:" + timeto
				+ "\n\t\t\tlocation:" + location
				+ "\n\t\t\tdestination:" + destination
				+ "\n\t\t\tdestcode:" + destcode
				+ "\n\t\t\ttripno:" + tripno;
	}
}
