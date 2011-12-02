package com.fdesousa.android.WheresMyTrain.requests.StationsList;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import android.util.Log;
import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.fdesousa.android.WheresMyTrain.json.TflJsonHandler;
import com.google.gson.Gson;

public class SLHandler extends TflJsonHandler {
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
	public Object getContainer() {
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
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}

		return stringFromInputStream(in);
	}

	private void writeJsonToCache() {
		try {
			/*
			 *	Write out a new cache, since the old one wasn't valid
			 *	This is the reason why two identical lines exist:
			 *	json = stringFromInputStream(in);
			 *	If there was no need to save the json string into cache,
			 *	this line would need calling only once
			 */
			final BufferedWriter out = new BufferedWriter(new FileWriter(cacheFile), 2048);
			out.write(json);
			out.close();
		} catch (IOException e) {
			//	Exception? Log it, silently ignore it
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}
	}
}
