package com.fds.WMT;

import com.fdesousa.android.WheresMyTrain.R;

import android.content.res.Resources;

/**
 * <h1>Lines.java</h1>
 * <p>
 * Enumeration of the TfL Lines (including two that are not technically
 * part of the TfL network; DLR and Overground). <br />
 * @author Filipe De Sousa
 * @version 0.8
 */
public enum Lines {
	BAKERLOO("b", "Bakerloo", R.color.bakerloo_colour),
	CENTRAL("c", "Central", R.color.central_colour),
	CIRCLE("crl", "Circle", R.color.circle_colour),
	DISTRICT("d", "District", R.color.district_colour),
	DLR("dlr", "DLR", R.color.dlr_colour),
	HAMMERSMITH("h", "Hammersmith & City", R.color.hammersmith_colour),
	JUBILEE("j", "Jubilee", R.color.jubilee_colour),
	METROPOLITAN("m", "Metropolitan", R.color.metropolitan_colour),
	NORTHERN("n", "Northern", R.color.northern_colour),
	OVERGROUND("ovr", "Overground", R.color.overground_colour),
	PICCADILLY("p", "Piccadilly", R.color.piccadilly_colour),
	VICTORIA("v", "Victoria", R.color.victoria_colour),
	WATERLOO("w", "Waterloo & City", R.color.waterloo_colour);

	public static Resources resources;
	private final String code;
	private final String name;
	private final int colourId;
	private int colourCode;

	private Lines(String code, String name, int colourId) {
		this.code = code;
		this.name = name;
		this.colourId = colourId;
		this.colourCode = 0;
	}

	/**
	 * Static setter for setting Resources instance
	 * @param resources - Resources instance
	 */
	public static void setResources(Resources resources) {
		Lines.resources = resources;
	}

	/**
	 * Static method for determining which Line is being referenced
	 * by its Line code String.
	 * @param code - String to check against code for right enum type
	 * @return Lines instance of the relevant enum type, or null if
	 *		none match the given code
	 */
	public static Lines getLineByCode(String code) {
		for (Lines l : Lines.values()) {
			if (l.code.equals(code)) return l;
		}
		return null;
	}

	/**
	 * Basic getter method that returns the code of the line
	 * @return String of the line code
	 */
	public String getCode() {
		return this.code;
	}
	
	/**
	 * Basic getter method that returns the name of the Line
	 * @return String of the Line name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Utility method to get the colour code, requiring Resources
	 * to have been set beforehand. Throws NullPointerException if
	 * Resources have not been set.
	 * @return integer colour code value for this Line
	 * @throws NullPointerException if Resources has not been set
	 *		via setResources(Resources) method
	 */
	public int getColourCode() {
		if (Lines.resources == null) {
			throw new NullPointerException();
		} else if (colourCode == 0) {
			colourCode = Lines.resources.getColor(colourId);
		}
		return colourCode;
	}

}
