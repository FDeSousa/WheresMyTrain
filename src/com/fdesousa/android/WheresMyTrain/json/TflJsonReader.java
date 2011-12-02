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
import com.fdesousa.android.WheresMyTrain.requests.DetailedPredictions.DPContainer;
import com.fdesousa.android.WheresMyTrain.requests.DetailedPredictions.DPHandler;
import com.fdesousa.android.WheresMyTrain.requests.LineStatus.LSContainer;
import com.fdesousa.android.WheresMyTrain.requests.LineStatus.LSHandler;
import com.fdesousa.android.WheresMyTrain.requests.StationStatus.SSContainer;
import com.fdesousa.android.WheresMyTrain.requests.StationStatus.SSHandler;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLContainer;
import com.fdesousa.android.WheresMyTrain.requests.StationsList.SLHandler;
import com.fdesousa.android.WheresMyTrain.requests.SummaryPredictions.SPContainer;
import com.fdesousa.android.WheresMyTrain.requests.SummaryPredictions.SPHandler;

/**
 * Complete class to automate sending a request, parsing the response JSON.
 * Some of the items are not in use, but will be implemented later. This application
 * will possibly never use summary predictions, so the request is mostly ignored.
 * @author Filipe De Sousa
 * @version 0.1
 */
@SuppressWarnings("unused")
public class TflJsonReader {
	//	Diagnostic values, useful for other classes, hence public
	/**	Value representing "out of service", provided in destcode value					*/
	public static final String OUT_OF_SERVICE = "546";
	/**	Value representing "no trip", provided in tripno value							*/
	public static final String NO_TRIP = "255";

	//	Internal query values, only useful in this class, hence private
	/**	URL divider for beginning of Query arguments									*/
	private static final char QUERY = '?';
	/**	URL divider of URL arguments, to be used after Query marker						*/
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

	//	Private instances that should not be revealed directly to other classes
	/**	File pointing to the application's cache folder									*/
	private final File cacheDir;
	/**	Instance of TflJsonHandler for fetching and parsing JSON requests				*/
	private TflJsonHandler handler;

	/**
	 * Very basic constructor, just sets the cache directory's location
	 * @param cacheDir - File instance pointing to application's cache directory
	 */
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
	 * Convenience method, simply joins to the handler's Thread, logs any
	 * interruption that might occur.
	 */
	private void stopHandler() {
		//	Wait for thread to finish before returning
		try {
			handler.join();
		} catch (InterruptedException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}
	}

	//	Detailed Predictions methods
	/**
	 * Utility method to begin preparations of Detailed Predictions.<br/>
	 * Makes the URI, instantiates the handler, starts new Thread.
	 * @param line - the unique line code for the request
	 * @param station - the unique station code for the request
	 */
	public void preparePredictionsDetailed(final String line, final String station) {
		//	Parses String "PredictionDetailed/line/station"
		URI uri = makeUri(PREDICTION_DETAILED, line, station, false);
		//	Instantiate, set the newest URI to fetch and parse
		handler = new DPHandler(uri);
		//	TODO: Replace with AsyncTask for fetching, parsing the data
		//	Start the thread for fetching and parsing
		handler.start();
	}

	/**
	 * Method to return the results obtained from the request.
	 * Type-casted for convenience.
	 */
	public DPContainer getPredictionsDetailed() {
		stopHandler();
		return (DPContainer) handler.getContainer();
	}

	//	Summary Predictions methods
	/**
	 * Utility method to begin preparations of Detailed Predictions.<br/>
	 * Makes the URI, instantiates the handler, starts new Thread.
	 * @param line - the unique line code for the request
	 * @param station - the unique station code for the request
	 */
	public void preparePredictionsSummary(final String line) {
		//	Parses String "PredictionSummary/line"
		URI uri = makeUri(PREDICTION_SUMMARY, line, null, false);
		handler = new SPHandler(uri);
		handler.start();
	}

	/**
	 * Method to return the results obtained from the request.
	 * Type-casted for convenience.
	 */
	public SPContainer getPredictionsSummary(final String line) {
		stopHandler();
		return (SPContainer) handler.getContainer();
	}

	//	Station Status methods
	/**
	 * Utility method to begin preparations of Station Status.<br/>
	 * Makes the URI, instantiates the handler, starts new Thread.
	 */
	public void prepareStationStatus(final boolean incidentsOnly) {
		URI uri = makeUri(LINE_STATUS, null, null, incidentsOnly);
		handler = new SSHandler(uri);
		handler.start();
	}
	/**
	 * Method to return the results obtained from the request.
	 * Type-casted for convenience.
	 */
	public SSContainer getStationStatus() {
		stopHandler();
		return (SSContainer) handler.getContainer();
	}

	//	Line Status methods
	/**
	 * Utility method to begin preparations of Line Status.<br/>
	 * Makes the URI, instantiates the handler, starts new Thread.
	 */
	public void prepareLineStatus(final boolean incidentsOnly) {
		URI uri = makeUri(LINE_STATUS, null, null, incidentsOnly);
		handler = new LSHandler(uri);
		handler.start();
	}
	/**
	 * Method to return the results obtained from the request.
	 * Type-casted for convenience.
	 */
	public LSContainer getLineStatus() {
		stopHandler();
		return (LSContainer) handler.getContainer();
	}

	//	Stations List methods
	/**
	 * Utility method to begin preparations of Stations List.<br/>
	 * Makes the URI, instantiates the handler, starts new Thread.
	 */
	public void prepareStationsList() {
		URI uri = makeUri(STATIONS_LIST, null, null, false);
		handler = new SLHandler(cacheDir, uri);
		handler.start();		
	}

	/**
	 * Method to return the results obtained from the request.
	 * Type-casted for convenience.
	 */
	public SLContainer getStationsList() {
		stopHandler();
		return (SLContainer) handler.getContainer();
	}
}
