package com.fdesousa.android.WheresMyTrain.json;

/*****************************************************************************************************
 *	Copyright (c) 2011 Filipe De Sousa
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *	associated documentation files (the "Software"), to deal in the Software without restriction,
 *	including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *	sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or
 *	substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *	NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *	DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************************/

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import android.util.Log;

import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.fdesousa.android.WheresMyTrain.information.Station;
import com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DPHandler;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.json.StationsList.SLHandler;

/**
 * Complete class to automate sending a request, parsing the response JSON.
 * Some of the items are not in use, but will be implemented later. This application
 * will possibly never use summary predictions, so the request is mostly ignored.
 * @author Filipe De Sousa
 * @version 0.1
 */
@SuppressWarnings("unused")
public class TflJsonReader {
	/**	Convenient definitions of the dividers for URL and its arguments				*/
	private static final char QUERY = '?';
	private static final char ARG_DIV = '&';

	/**	The base URL used for all TfL requests											*/
	private static final String BASE_URL = "http://trains.desousa.com.pt/tfl.php";
	/**	The base for the request type URL argument										*/
	private static final String REQUEST_ARG = "request=";
	/**	The base for the line code URL argument											*/
	private static final String LINE_ARG = "line=";
	/**	The base for the station code URL argument										*/
	private static final String STATION_ARG = "station=";
	/**	The base for the incidents only URL argument									*/
	private static final String INCIDENTS_ARG = "incidents=";
	/**	The URL add-on for acquiring detailed predictions of a line [station optional]	*/
	private static final String PREDICTION_DETAILED = "predictiondetailed";
	/**	The URL add-on for acquiring summary predictions of a given line				*/
	private static final String PREDICTION_SUMMARY = "predictionsummary";
	/**	The URL add-on for acquiring given station status [incidents only optional]		*/
	private static final String STATION_STATUS = "stationstatus";
	/**	The URL add-on for acquiring given line status [incidents only optional]		*/
	private static final String LINE_STATUS = "linestatus";
	/**	The URL add-on for acquiring the list of lines and stations and their codes		*/
	private static final String STATIONS_LIST = "stationslist";
	/**	Useful String value that evaluates to true in PHP when type-casting to boolean	*/
	private static final String PHP_TRUE_VALUE = "1";

	/**	Value representing "out of service", provided in destcode value					*/
	public static final String OUT_OF_SERVICE = "546";
	/**	Value representing "no trip", provided in tripno value							*/
	public static final String NO_TRIP = "255";
	
	/**	File pointing to the application's cache folder									*/
	private final File cacheDir;

	public TflJsonReader(File cacheDir) {
		this.cacheDir = cacheDir;
	}

	/**
	 * Convenience method to return a URL using the base_url and
	 * the supplied extension, handling possible exceptions too
	 * @param extension - the portion of the URL, after the base URL, required for the request
	 * @return a new URL instance based upon the base URL concatenated with the extension
	 */
	private URI makeUri(final String request, final String line, final String station,
			final boolean incidentsOnly) {
		try {
			String arguments = REQUEST_ARG + request;
			//	If line is a letter, it's valid, add it to the arguments
			if (line != null) arguments += ARG_DIV + LINE_ARG + line;
			//	If station isn't null, it's probably valid, add it
			if (station != null) arguments += ARG_DIV + STATION_ARG + station;
			//	If incidentsOnly is true, add it
			if (incidentsOnly) arguments += ARG_DIV + INCIDENTS_ARG + PHP_TRUE_VALUE;
			//	Now make the URI with base and query elements
			return new URI(BASE_URL + QUERY + arguments);
		} catch (URISyntaxException e) {
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
	public DPContainer getPredictionsDetailed(final String line, final String station) {
		//	Parses String "PredictionDetailed/line/station"
		URI uri = makeUri(PREDICTION_DETAILED, line, station, false);
		//	Instantiate, set the newest URI to fetch and parse
		DPHandler predictions = new DPHandler().setUri(uri);
		//	Start the thread for fetching and parsing
		predictions.start();
		//	Wait for thread to finish before returning
		try {
			predictions.join();
		} catch (InterruptedException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}
		
		return predictions.getDPContainer();
	}

	/**
	 * Method for handling getting summary predictions of trains on a specific line
	 * @param line - the desired London Underground line's unique identifier
	 * @return 
	 */
	public List<Station> getPredictionsSummary(final String line) {
		//	Parses String "PredictionSummary/line"
		URI uri = makeUri(PREDICTION_SUMMARY, line, null, false);

		List<Station> stations = null;
		
		return stations;
	}

	/**
	 * Method for handling getting station status for all London Underground stations,
	 * and optionally only for stations with incidents flagged
	 * @param incidentsOnly - true for status only from stations with incidents, false otherwise
	 * @return 
	 */
	public List<Station> getStationStatus(final boolean incidentsOnly) {
		//	Conditional assignment, parses String "StationStatus/IncidentsOnly" if incidentsOnly
		//+	is true, or parses String "StationStatus" if incidentsOnly is false
		URI uri = makeUri(STATION_STATUS, null, null, incidentsOnly);

		List<Station> stations = null;
		
		return stations;
	}

	/**
	 * Method for handling getting line status for a specific London Underground line,
	 * and optionally only for lines with incidents flagged
	 * @param line - the desired London Underground line's unique identifier
	 * @param incidentsOnly - true for status only from stations with incidents, false otherwise
	 * @return 
	 */
	public List<Station> getLineStatus(final boolean incidentsOnly) {
		//	Conditional assignment, parses String "LineStatus/IncidentsOnly" if incidentsOnly
		//+	is true, or parses String "LineStatus" if incidentsOnly is false
		URI uri = makeUri(LINE_STATUS, null, null, incidentsOnly);

		List<Station> stations = null;
		
		return stations;
	}
	
	public SLContainer getStationsList() {
		URI uri = makeUri(STATIONS_LIST, null, null, false);

		SLHandler stationslist = new SLHandler(cacheDir).setUri(uri);
		stationslist.start();
		try {
			stationslist.join();
		} catch (InterruptedException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}

		return stationslist.getSLContainer();
	}
}
