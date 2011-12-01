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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import android.util.Log;

import com.fdesousa.android.WheresMyTrain.WheresMyTrain;

/**
 * Superclass of a JSON Handler.<br/>
 * Provides the basic utility and convenience methods for all the handlers,
 * allows easy beginning of Threads, fetching and parsing.
 * @author Filipe De Sousa
 * @param <T>
 *
 */
public abstract class TflJsonHandler extends Thread {
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
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Problem fetching the data");
		} catch (IOException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Problem opening the data");
		}
		
		String out = stringFromInputStream(in);
		
		try {
			in.close();
		} catch (IOException e) {
			// Can't close the InputStream, log it, silently ignore
			Log.e(WheresMyTrain.TAG, e.getMessage());
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
	public abstract Object getContainer(); 
	
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
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}

		return out.toString();
	}
}
