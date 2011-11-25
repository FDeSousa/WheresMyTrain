package com.fdesousa.android.WheresMyTrain.xml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.fdesousa.android.WheresMyTrain.information.Station;

public class TflXmlReader {
	/**	Just a simple definition of the divider for URLs, for convenience				*/
	private static final char DIVIDER = '/';

	/**	The base URL used for all TfL requests											*/
	private final String base_url;
	/**	The URL add-on for acquiring detailed predictions of a line [station optional]	*/
	private final String prediction_detailed;
	/**	The URL add-on for acquiring summary predictions of a given line				*/
	private final String prediction_summary;
	/**	The URL add-on for acquiring given station status [incidents only optional]		*/
	private final String station_status;
	/**	The URL add-on for acquiring given line status [incidents only optional]		*/
	private final String line_status;
	/**	Optional parameter for line/station status to only get incidents				*/
	private final String incidents_only;

	/**	Informational, value representing an out of service train, provided in D value	*/
	public final String out_of_service;
	/**	Informational, value representing a train without a trip, provided in L value	*/
	public final String no_trip;
	
	public TflXmlReader(String base_url, String prediction_detailed,
			String prediction_summary, String station_status, String line_status,
			String incidents_only, String out_of_service, String no_trip) {

		this.base_url = base_url;
		this.prediction_detailed = prediction_detailed;
		this.prediction_summary = prediction_summary;
		this.station_status = station_status;
		this.line_status = line_status;
		this.incidents_only = incidents_only;
		this.out_of_service = out_of_service;
		this.no_trip = no_trip;
	}

	/**
	 * Convenience method to return a URL using the base_url and
	 * the supplied extension, handling possible exceptions too
	 * @param extension - the portion of the URL, after the base URL, required for the request
	 * @return a new URL instance based upon the base URL concatenated with the extension
	 */
	private URL setUrl(final String extension) {
		try {
			return new URL(base_url + extension);
		} catch (MalformedURLException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Entered URL is not valid");
			return null;
		}
	}

	/**
	 * Method for handling getting detailed predictions of trains at a specific
	 * station on a specific line
	 * @param line - the desired London Underground line's unique identifier
	 * @param station - the desired London Underground station's unique identifier
	 */
	public ArrayList<Station> getPredictionsDetailed(final char line, final String station) {
		//	Parses String "PredictionDetailed/line/station"
		URL url = setUrl(prediction_detailed + DIVIDER + line + DIVIDER + station);
		XmlParser xml = XmlParser.newInstance(url, XmlParser.PREDICTION_DETAILED);
		ArrayList<Station> stations = xml.parse();
		
		return stations;
	}

	/**
	 * Method for handling getting summary predictions of trains on a specific line
	 * @param line - the desired London Underground line's unique identifier
	 * @return 
	 */
	public ArrayList<Station> getPredictionsSummary(final char line) {
		//	Parses String "PredictionSummary/line"
		URL url = setUrl(prediction_summary + DIVIDER + line);
		XmlParser xml = XmlParser.newInstance(url, XmlParser.PREDICTION_SUMMARY);
		ArrayList<Station> stations = xml.parse();
		
		return stations;
	}

	/**
	 * Method for handling getting station status for all London Underground stations,
	 * and optionally only for stations with incidents flagged
	 * @param incidentsOnly - true for status only from stations with incidents, false otherwise
	 * @return 
	 */
	public ArrayList<Station> getStationStatus(final boolean incidentsOnly) {
		//	Conditional assignment, parses String "StationStatus/IncidentsOnly" if incidentsOnly
		//+	is true, or parses String "StationStatus" if incidentsOnly is false
		URL url = setUrl(incidentsOnly ? (station_status + DIVIDER + incidents_only) : station_status);
		XmlParser xml = XmlParser.newInstance(url, incidentsOnly ?
				XmlParser.STATION_STATUS_INCIDENTS : XmlParser.STATION_STATUS);
		ArrayList<Station> stations = xml.parse();
		
		return stations;
	}

	/**
	 * Method for handling getting line status for a specific London Underground line,
	 * and optionally only for lines with incidents flagged
	 * @param line - the desired London Underground line's unique identifier
	 * @param incidentsOnly - true for status only from stations with incidents, false otherwise
	 * @return 
	 */
	public ArrayList<Station> getLineStatus(final char line, final boolean incidentsOnly) {
		//	Conditional assignment, parses String "LineStatus/IncidentsOnly" if incidentsOnly
		//+	is true, or parses String "LineStatus" if incidentsOnly is false
		URL url = setUrl(line_status + DIVIDER + line + (incidentsOnly ? (DIVIDER + incidents_only) : ""));
		XmlParser xml = XmlParser.newInstance(url, incidentsOnly ?
				XmlParser.LINE_STATUS_INCIDENTS : XmlParser.LINE_STATUS);
		ArrayList<Station> stations = xml.parse();
		
		return stations;
	}
}
