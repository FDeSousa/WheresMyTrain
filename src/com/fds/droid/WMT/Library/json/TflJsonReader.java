package com.fds.droid.WMT.Library.json;

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

import java.net.URI;
import java.net.URISyntaxException;
import android.util.Log;

import com.fds.droid.WMT.Library.LibraryMain;

/**
 * <b>TflJsonReader</b>
 * <p>Complete class to automate sending a request, parsing the response JSON.<br/>
 * Implemented are all of the types of requests that can be made to the standard tfl.php server,
 * which is used as the back-end, caching, fetching and parsing all requests into JSON.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public abstract class TflJsonReader<T> {
	//	Internal query values, only useful in this class, hence private
	/**	URL divider for beginning of Query arguments									*/
	protected static final char QUERY = '?';
	/**	URL divider of URL arguments, to be used after Query marker						*/
	protected static final char ARG_DIV = '&';
	/**	The base URL used for all TfL requests											*/
	protected static final String URL_HOST = "http://trains.desousa.com.pt";
	/*	The relative path to the PHP script to send requests to							*/
	protected static final String URL_PATH = "/tfl.php";
	/**	The base for the request type URL argument										*/
	protected static final String REQUEST_ARG = "request=";
	/**	The base for the line code URL argument											*/
	protected static final String LINE_ARG = "line=";
	/**	The base for the station code URL argument										*/
	private static final String STATION_ARG = "station=";
	/**	protected base for the incidents only URL argument								*/
	protected static final String INCIDENTS_ARG = "incidents=";
	/**	The URL add-on for acquiring detailed predictions of a line [station optional]	*/
	protected static final String PREDICTION_DETAILED = "predictiondetailed";
	/**	The URL add-on for acquiring summary predictions of a given line				*/
	protected static final String PREDICTION_SUMMARY = "predictionsummary";
	/**	The URL add-on for acquiring given station status [incidents only optional]		*/
	protected static final String STATION_STATUS = "stationstatus";
	/**	The URL add-on for acquiring given line status [incidents only optional]		*/
	protected static final String LINE_STATUS = "linestatus";
	/**	The URL add-on for acquiring the list of lines and stations and their codes		*/
	protected static final String STATIONS_LIST = "stationslist";
	/**	Useful String value that evaluates to true in PHP when type-casting to boolean	*/
	protected static final String PHP_TRUE_VALUE = "1";
	/**	Handler with specific type for all our subclasses								*/
	protected TflJsonHandler<T> jsonHandler;

	/**
	 * Convenience method to return a URL using the base_url and
	 * the supplied extension, handling possible exceptions too
	 * @param extension - the portion of the URL, after the base URL, required for the request
	 * @return a new URL instance based upon the base URL concatenated with the extension
	 */
	protected URI makeUri(final String request, final String line, final String station, final boolean incidentsOnly) {
		try {
			String arguments = REQUEST_ARG + request;
			//	If line is a letter, it's valid, add it to the arguments
			if (line != null) arguments += ARG_DIV + LINE_ARG + line;
			//	If station isn't null, it's probably valid, add it
			if (station != null) arguments += ARG_DIV + STATION_ARG + station;
			//	If incidentsOnly is true, add it
			if (incidentsOnly) arguments += ARG_DIV + INCIDENTS_ARG + PHP_TRUE_VALUE;
			//	Now make the URI with base and query elements
			return new URI(URL_HOST + URL_PATH + QUERY + arguments);
		} catch (URISyntaxException e) {
			Log.e(LibraryMain.TAG, e.getMessage());
			return null;
		}
	}

	/**
	 * Convenience method, simply joins to the handler's Thread, logs any
	 * interruption that might occur.
	 * @param handler - the TflJsonHandler instance to stop execution of
	 */
	protected void stopHandler(TflJsonHandler<T> handler) {
		//	I dislike using while(true), so use a boolean variable instead
		boolean running = true;
		while (running) {
			//	Wait for thread to finish before returning
			try {
				handler.join();
				running = false;
			} catch (InterruptedException e) {
				Log.e(LibraryMain.TAG, e.getMessage());
			}
		}
	}
	
	
	//	--------------------------------------------------
	//	Abstract methods that each sub-class will individually implement
	public abstract T get();
	public abstract T refresh();

}
