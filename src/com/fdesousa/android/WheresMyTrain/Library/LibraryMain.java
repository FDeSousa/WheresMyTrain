package com.fdesousa.android.WheresMyTrain.Library;

/**
 * LibraryMain is used for storing all the data that just doesn't really fit into
 * just one single category
 * @author Filipe De Sousa
 */
public final class LibraryMain {
	//	Debug codes
	/**	TAG String for project logs	*/
	public static final String TAG = "WheresMyTrain-Library";

	//	Useful for displaying all line status information
	/**	DLR line name			*/
	public static final String DLR_NAME = "DLR";
	/**	Overground line name	*/
	public static final String OVERGROUND_NAME = "Overground";

	//	Codes for line service status
	/**	Good Service	*/
	public static final String GOOD_SERVICE_CODE = "GS";
	/**	Part Closure	*/
	public static final String PART_CLOSURE_CODE = "PC";
	/**	Severe Delays	*/
	public static final String SEVERE_DELAYS_CODE = "SD";
	/**	Line Closed		*/
	public static final String CLOSED_CODE = "CS";

	//	Out-of-service and no-trip trains carry one or both of these codes
	/**	Value representing "no destination", provided in train's destcode value	*/
	public static final int UNKNOWN_DESTINATION = 0;
	/**	Value representing "out of service", provided in train's destcode value	*/
	public static final int OUT_OF_SERVICE = 546;
	/**	Value representing "no trip", provided in train's tripno value			*/
	public static final int NO_TRIP = 255;

	//	Simple replacement strings for certain situations
	/**	Replacement string for condition of OUT_OF_SERVICE and NO_TRIP with destination "Unknown"	*/
	public static final String CHECK_FRONT = "Check front of train";
	/**	Replacement string for condition of empty Location string									*/
	public static final String NO_LOCATION = "Location unknown";
}
