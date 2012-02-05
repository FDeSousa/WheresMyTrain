package com.fdesousa.android.WheresMyTrain.Library.requests.StationsList;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import android.util.Log;

import com.fdesousa.android.WheresMyTrain.Library.LibraryMain;
import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonHandler;
import com.fdesousa.google.gson.Gson;

/**
 * <b>SLHandler : TflJsonHandler</b>
 * <p>Handler of Stations List requests.<br/>
 * Used to parse JSON with GSON in a manner specific to Stations List.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class SLHandler extends TflJsonHandler<SLContainer> {
	private SLContainer stationslist;
	private File cacheFile;

	/**
	 * Constructor. Sets the URI of the request, and the cache file location
	 * @param cacheDir - File instance for the application's cache directory
	 * @param uri - the URI of the data to fetch
	 */
	public SLHandler(File cacheDir, URI uri) {
		super(uri);
		cacheFile = new File(cacheDir, "stationslist.json");
	}

	/**
	 * Simple getter, return the container the JSON was parsed into
	 * @return New SLContainer instance with the fetched data
	 */
	@Override
	public SLContainer getContainer() {
		return stationslist;
	}

	/**
	 * Fetches the JSON from the server, parses that into SLContainer
	 * As this can take a little while, decided to place into a thread
	 * To save time with request and download, cache the string if it
	 * does not exist. Caches of stations list are valid on the server
	 * for 7 days, so only do a check for new stations list in 7 days
	 */
	@Override
	public void run() {
		if (cacheFile.exists() && System.currentTimeMillis() - cacheFile.lastModified() < 604800000) {
			//	Check the cache file exists and is recent enough first (7 days in milliseconds: 604800000)
			json = fetchJsonFromCache();
		} else {
			//	If not, grab the newest copy from the server for parsing
			json = fetchJson();
			//	Save that JSON to a cache file
			writeJsonToCache();
		}
		//	Time to parse that JSON!
		parseJson();
	}

	/**
	 * Automates parsing of JSON string into an object
	 */
	@Override
	protected void parseJson() {
		stationslist = new Gson().fromJson(json, SLContainer.class);
	}

	private String fetchJsonFromCache() {
		InputStreamReader in = null;

		try {
			//	Get an InputStreamReader for the file input, get the JSON string from there
			in = new InputStreamReader(new FileInputStream(cacheFile));
		} catch (FileNotFoundException e) {
			//	Exception? How odd, we checked the file exists. Log it, silently ignore
			Log.e(LibraryMain.TAG, e.getMessage());
		}

		return stringFromInputStream(in);
	}

	/**
	 * Writes out JSON string to cache
	 */
	private void writeJsonToCache() {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(cacheFile), 2048);
			out.write(json);
			out.close();
		} catch (IOException e) {
			//	Exception? Log it, silently ignore it
			Log.e(LibraryMain.TAG, e.getMessage());
		}
	}
}
