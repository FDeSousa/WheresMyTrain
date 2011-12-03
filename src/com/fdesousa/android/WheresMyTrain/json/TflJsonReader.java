package com.fdesousa.android.WheresMyTrain.json;

/******************************************************************************
 * Copyright 2011 Filipe De Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
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
 * <b>TflJsonReader</b>
 * <p>Complete class to automate sending a request, parsing the response JSON.<br/>
 * Implemented are all of the types of requests that can be made to the standard tfl.php server,
 * which is used as the back-end, caching, fetching and parsing all requests into JSON.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
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

	//	Private instances
	/**	File pointing to the application's cache folder									*/
	private final File cacheDir;

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
	 * @param handler - the TflJsonHandler instance to stop execution of
	 */
	private void stopHandler(TflJsonHandler handler) {
		//	Wait for thread to finish before returning
		try {
			handler.join();
		} catch (InterruptedException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}
	}

	//	--------------------------------------------------
	//	Detailed Predictions methods
	/**	TflJsonHandler specifically for Detailed Predictions. Allows for other simultaneous requests	*/
	private TflJsonHandler dp_handler;

	/**
	 * Utility method to handle the request and parsing of JSON for Detailed Predictions.
	 * @param line - the code for the Underground line to make a request for
	 * @param station - the code for the Underground station to make a request for
	 * @return instance of DPContainer with the requested results
	 */
	public DPContainer getDetailedPredictions(final String line, final String station) {
		//	Parses String "PredictionDetailed/line/station"
		URI uri = makeUri(PREDICTION_DETAILED, line, station, false);
		//	Instantiate, set the newest URI to fetch and parse
		dp_handler = new DPHandler(uri);
		//	Start the thread for fetching and parsing
		dp_handler.start();
		stopHandler(dp_handler);
		return (DPContainer) dp_handler.getContainer();
	}

	/**
	 * Convenience method to refresh the detailed predictions, without making a completely new request
	 * @return instance of DPContainer with the requested results
	 */
	public DPContainer refreshDetailedPredictions() {
		dp_handler.start();
		stopHandler(dp_handler);
		return (DPContainer) dp_handler.getContainer();
	}

	//	--------------------------------------------------
	//	Summary Predictions methods
	/**	TflJsonHandler specifically for Summary Predictions. Allows for other simultaneous requests	*/
	private TflJsonHandler sp_handler;

	/**
	 * Utility method to handle the request and parsing of JSON for Summary Predictions.
	 * @param line - the code for the Underground line to make a request for
	 * @return instance of SPContainer with the requested results
	 */
	public SPContainer getSummaryPredictions(final String line) {
		//	Parses String "PredictionSummary/line"
		URI uri = makeUri(PREDICTION_SUMMARY, line, null, false);
		sp_handler = new SPHandler(uri);
		sp_handler.start();
		stopHandler(sp_handler);
		return (SPContainer) sp_handler.getContainer();
	}

	/**
	 * Convenience method to refresh the summary predictions, without making a completely new request
	 * @return instance of SPContainer with the requested results
	 */
	public SPContainer refreshSummaryPredictions() {
		sp_handler.start();
		stopHandler(sp_handler);
		return (SPContainer) sp_handler.getContainer();
	}

	//	--------------------------------------------------
	//	Station Status methods
	/**	TflJsonHandler specifically for Station Status. Allows for other simultaneous requests	*/
	private TflJsonHandler ss_handler;

	/**
	 * Utility method to handle the request and parsing of JSON for Station Status.
	 * @param incidentsOnly - true to only fetch stations with incidents, false to fetch all
	 * @return instance of SSContainer with the requested results
	 */
	public SSContainer getStationStatus(final boolean incidentsOnly) {
		URI uri = makeUri(STATION_STATUS, null, null, incidentsOnly);
		ss_handler = new SSHandler(uri);
		ss_handler.start();
		stopHandler(ss_handler);
		return (SSContainer) ss_handler.getContainer();
	}

	/**
	 * Convenience method to refresh the station status, without making a completely new request
	 * @return instance of SSContainer with the requested results
	 */
	public SSContainer refreshStationStatus() {
		ss_handler.start();
		stopHandler(ss_handler);
		return (SSContainer) ss_handler.getContainer();
	}

	//	--------------------------------------------------
	//	Line Status methods
	/**	TflJsonHandler specifically for Line Status. Allows for other simultaneous requests	*/
	private TflJsonHandler ls_handler;

	/**
	 * Utility method to handle the request and parsing of JSON for Line Status.
	 * @param incidentsOnly - true to only fetch lines with incidents, false to fetch all
	 * @return instance of LSContainer with the requested results
	 */
	public LSContainer getLineStatus(final boolean incidentsOnly) {
		URI uri = makeUri(LINE_STATUS, null, null, incidentsOnly);
		ls_handler = new LSHandler(uri);
		ls_handler.start();
		stopHandler(ls_handler);
		return (LSContainer) ls_handler.getContainer();
	}

	/**
	 * Convenience method to refresh the line status, without making a completely new request
	 * @return instance of LSContainer with the requested results
	 */
	public LSContainer refreshLineStatus() {
		ls_handler.start();
		stopHandler(ls_handler);
		return (LSContainer) ls_handler.getContainer();
	}

	//	--------------------------------------------------
	//	Stations List methods
	/**	TflJsonHandler specifically for Stations List. Allows for other simultaneous requests	*/
	private TflJsonHandler sl_handler;

	/**
	 * Utility method to handle the request and parsing of JSON for Stations List.
	 * @return instance of SLContainer with the requested results
	 */
	public SLContainer getStationsList() {
		URI uri = makeUri(STATIONS_LIST, null, null, false);
		sl_handler = new SLHandler(cacheDir, uri);
		sl_handler.start();
		stopHandler(sl_handler);
		return (SLContainer) sl_handler.getContainer();
	}

	/**
	 * Convenience method to refresh the stations list, without making a completely new request
	 * @return instance of SLContainer with the requested results
	 */
	public SLContainer refreshStationsList() {
		sl_handler.start();
		stopHandler(sl_handler);
		return (SLContainer) sl_handler.getContainer();
	}
}
