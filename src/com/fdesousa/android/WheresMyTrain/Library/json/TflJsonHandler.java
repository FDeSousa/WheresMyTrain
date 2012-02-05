package com.fdesousa.android.WheresMyTrain.Library.json;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import com.fdesousa.android.WheresMyTrain.Library.LibraryMain;

import android.util.Log;

/**
 * <b><i>TflJsonHandler</i></b>
 * <p>Superclass of a JSON Handler.<br/>
 * Provides the basic utility and convenience methods for all the handlers,
 * allows easy beginning of Threads, fetching and parsing.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public abstract class TflJsonHandler<T> extends Thread {
	protected URI uri;
	protected String json;

	/**
	 * Constructor. Sets the URI of the request
	 * @param uri - the URI of the data to fetch
	 */
	public TflJsonHandler(URI uri) {
		this.uri = uri;
		this.json = "";
	}
	
	@Override
	public void run() {
		json = fetchJson();
		parseJson();
	}
	
	/**
	 * Utility method that handles the HTTP request to the supplied URI, and its response.<br/>
	 * @return String instance of the response JSON
	 */
	protected String fetchJson() {
		InputStream in = null;

		try {
			in = TflJsonFetcher.fetchNewJson(uri);
		} catch (IllegalStateException e) {
			Log.e(LibraryMain.TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(LibraryMain.TAG, e.getMessage());
		}
		
		String out = stringFromInputStream(in);
		
		try {
			in.close();
		} catch (IOException e) {
			// Can't close the InputStream, log it, silently ignore
			Log.e(LibraryMain.TAG, e.getMessage());
		}
		
		return out;
	}
	
	/**
	 * Utility method that handles the parsing of the JSON string to objects
	 */
	protected abstract void parseJson();
	
	/**
	 * Simple getter, returns the Container as a basic Object
	 * @return instance of Container as basic Object
	 */
	public abstract T getContainer();
	
	/**
	 * Utility method to convert an InputStream into a String
	 * @param in - instance of InputStream to convert to String
	 * @return String representing the InputStream's contents
	 */
	protected String stringFromInputStream(InputStream in) {
		return stringFromInputStream(new InputStreamReader(in));
	}
	
	/**
	 * Utility method to convert an InputStreamReader into a String
	 * @param in - instance of InputStreamReader to convert to String
	 * @return String representing the InputStreamReader's contents
	 */
	protected String stringFromInputStream(InputStreamReader in) {
		int BUFFER_SIZE = 8192;
		char[] buffer = new char[BUFFER_SIZE];
		StringBuilder out = new StringBuilder(BUFFER_SIZE);

		try {
			for (int read = in.read(buffer, 0, BUFFER_SIZE); read != -1;
					read = in.read(buffer, 0, BUFFER_SIZE)) {
				out.append(buffer, 0, read);
			}
		} catch (IOException e) {
			// Silently ignore, just log the error
			Log.e(LibraryMain.TAG, e.getMessage());
		}

		return out.toString();
	}
}
