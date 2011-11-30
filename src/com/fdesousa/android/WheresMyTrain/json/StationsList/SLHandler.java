package com.fdesousa.android.WheresMyTrain.json.StationsList;

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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.google.gson.Gson;

public class SLHandler extends Thread {
	private SLContainer stationslist;
	private URI uri;
	private File cacheFile;

	public SLHandler(File cacheDir) {
		cacheFile = new File(cacheDir, "stationslist.json");
	}

	public SLContainer getSLContainer() {
		return stationslist;
	}

	/**
	 * Convenience method to set the URI. Can use in method chaining
	 * @param uri - the URI of the data to fetch
	 * @return this instance - for method chaining
	 */
	public SLHandler setUri(URI uri) {
		this.uri = uri;
		return this;
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
		String json = "";
		InputStreamReader in = null;
		//	Check the cache file exists and is recent enough first (7 days in milliseconds: 604800000)
		if (cacheFile.exists() && System.currentTimeMillis() - cacheFile.lastModified() < 604800000) {
			try {
				//	Get an InputStreamReader for the file input, get the JSON string from there
				in = new InputStreamReader(new FileInputStream(cacheFile));
				json = stringFromInputStream(in);
			} catch (FileNotFoundException e) {
				//	Exception? How odd, we checked the file exists. Log it, silently ignore
				Log.e(WheresMyTrain.TAG, e.getMessage());
			}
		//	If not, grab the newest copy from the server for later use
		} else {
			try {
				//	Get the response from the request into InputStreamReader
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(uri);
				HttpResponse httpresponse = httpclient.execute(httppost);
				HttpEntity entity = httpresponse.getEntity();
				in = new InputStreamReader(entity.getContent());
			} catch (IllegalStateException e) {
				Log.e(WheresMyTrain.TAG, e.getMessage());
				WheresMyTrain.displayToast("Problem fetching the data");
			} catch (IOException e) {
				Log.e(WheresMyTrain.TAG, e.getMessage());
				WheresMyTrain.displayToast("Problem opening the data");
			} finally {
				//	Get the InputStreamReader into String
				json = stringFromInputStream(in);
				try {
					//	Write out a new cache, since the old one wasn't valid
					final BufferedWriter out = new BufferedWriter(new FileWriter(cacheFile), 2048);
					out.write(json);
					out.close();
				} catch (IOException e) {
					//	Exception? Log it, silently ignore it
					Log.e(WheresMyTrain.TAG, e.getMessage());
				}
			}
		}
		try {
			//	Try to safely close the InputStream now
			in.close();
		} catch (IOException e) {
			// Can't close the InputStreamReader, log it, silently ignore
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}
		
		//	We have the String instantiated now with the response JSON, so parse it
		stationslist = new Gson().fromJson(json, SLContainer.class);
	}
	
	private String stringFromInputStream(InputStreamReader in) {
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
