package com.fdesousa.android.WheresMyTrain.information;

import java.util.ArrayList;
import java.util.Iterator;

public class Station implements Iterable<Platform> {
	/**	The unique station code string						*/
	public final String code;
	/**	The full station name								*/
	public final String name;
	/**	Time the service was run, format hh:MM:ss			*/
	public String curTime;
	
	/**	List of the platforms relevant to Station and Line	*/
	private final ArrayList<Platform> platforms;
	
	public Station(final String code, final String name, final String curTime) {
		this.code = code;
		this.name = name;
		this.curTime = curTime;
		platforms = new ArrayList<Platform>();
	}
	
	public void appendPlatform(final Platform platform) {
		platforms.add(platform);
	}

	@Override
	public Iterator<Platform> iterator() {
		return platforms.iterator();
	}
}