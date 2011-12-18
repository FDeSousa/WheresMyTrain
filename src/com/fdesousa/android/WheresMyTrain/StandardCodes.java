package com.fdesousa.android.WheresMyTrain;

public final class StandardCodes {
	//	Short codes for all lines, useful for specific requests, sorting through Stations List
	/**	Bakerloo line short code		*/
	public static final String BAKERLOO_CODE = "b";
	/**	Central line short code			*/
	public static final String CENTRAL_CODE = "c";
	/**	District line short code		*/
	public static final String DISTRICT_CODE = "d";
	/**	Hammersmith & City and Circle lines short code	*/
	public static final String HAMMERSMITH_CODE = "h";
	/**	Jubile line short code			*/
	public static final String JUBILEE_CODE = "j";
	/**	Metropolitan line short code	*/
	public static final String METROPOLITAN_CODE = "m";
	/**	Northern line short code		*/
	public static final String NORTHERN_CODE = "n";
	/**	Piccadilly line short code		*/
	public static final String PICCADILLY_CODE = "p";
	/**	Victoria line short code		*/
	public static final String VICTORIA_CODE = "v";
	/**	Waterloo & City line short code	*/
	public static final String WATERLOO_CODE = "w";

	//	Full names for each line, useful for sorting through Line Status requests
	/**	Bakerloo line name				*/
	public static final String BAKERLOO_NAME = "Bakerloo";
	/**	Central line name				*/
	public static final String CENTRAL_NAME = "Central";
	/**	Circle line name				*/
	public static final String CIRCLE_NAME = "Circle";
	/**	District line name				*/
	public static final String DISTRICT_NAME = "District";
	/**	Hammersmith & City line name	*/
	public static final String HAMMERSMITH_NAME = "Hammersmith and City";
	/**	Jubilee line name				*/
	public static final String JUBILEE_NAME = "Jubilee";
	/**	Metropolitan line name			*/
	public static final String METROPOLITAN_NAME = "Metropolitan";
	/**	Northern line name				*/
	public static final String NORTHERN_NAME = "Northern";
	/**	Piccadilly line name			*/
	public static final String PICCADILLY_NAME = "Piccadilly";
	/**	Victoria line name				*/
	public static final String VICTORIA_NAME = "Victoria";
	/**	Waterloo & City line name		*/
	public static final String WATERLOO_NAME = "Waterloo and City";
	
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
