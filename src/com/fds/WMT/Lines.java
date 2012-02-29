package com.fds.WMT;

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
	BAKERLOO("b", "Bakerloo", R.color.bakerloo_colour, R.drawable.widget_title_shape_bakerloo),
	CENTRAL("c", "Central", R.color.central_colour, R.drawable.widget_title_shape_central),
	CIRCLE("crl", "Circle", R.color.circle_colour, R.drawable.widget_title_shape_hammersmith),
	DISTRICT("d", "District", R.color.district_colour, R.drawable.widget_title_shape_district),
	DLR("dlr", "DLR", R.color.dlr_colour, R.drawable.widget_title_shape_default),
	HAMMERSMITH("h", "Hammersmith and City", R.color.hammersmith_colour, R.drawable.widget_title_shape_hammersmith),
	JUBILEE("j", "Jubilee", R.color.jubilee_colour, R.drawable.widget_title_shape_jubilee),
	METROPOLITAN("m", "Metropolitan", R.color.metropolitan_colour, R.drawable.widget_title_shape_metropolitan),
	NORTHERN("n", "Northern", R.color.northern_colour, R.drawable.widget_title_shape_northern),
	OVERGROUND("ovr", "Overground", R.color.overground_colour, R.drawable.widget_title_shape_default),
	PICCADILLY("p", "Piccadilly", R.color.piccadilly_colour, R.drawable.widget_title_shape_piccadilly),
	VICTORIA("v", "Victoria", R.color.victoria_colour, R.drawable.widget_title_shape_victoria),
	WATERLOO("w", "Waterloo and City", R.color.waterloo_colour, R.drawable.widget_title_shape_waterloo),
	UNKNOWN("", "Unknown line", R.color.northern_colour, R.drawable.widget_title_shape_default);

	public static Resources resources;
	private final String code;
	private final String name;
	private final int colourId;
	private final int shapeId;
	private int colourCode;

	private Lines(String code, String name, int colourId, int shapeId) {
		this.code = code;
		this.name = name;
		this.colourId = colourId;
		this.shapeId = shapeId;
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
		return UNKNOWN;
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

	/**
	 * Returns the shape ID that relates to this lines' specific shape
	 * used in the title of the widget.
	 * @return integer value, the ID of the relevant widget shape
	 */
	public int getShapeCode() {
		return shapeId;
	}
}
