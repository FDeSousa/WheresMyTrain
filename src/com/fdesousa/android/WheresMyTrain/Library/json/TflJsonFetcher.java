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
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fdesousa.android.WheresMyTrain.WheresMyTrain;

/**
 * <b>TflJsonFetcher</b>
 * <p>Class intended to allow an easy manner in which to fetch JSON from the server<br/>
 * Provides just one static utility method which gets a stream from the server</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class TflJsonFetcher {

	/**
	 * Utility method for fetching an InputStream from a designated URI
	 * and returning it for processing.<br/>
	 * Static method to aide in easy, wide-spread use
	 * @param uri - the URI of the data to fetch
	 * @return InputStream containing the response of the request or null if there is none
	 * @throws IllegalStateException - in case of a problem, or if connection was aborted
	 * @throws IOException - if the stream could not be created
	 */
	public static InputStream fetchNewJson(URI uri) throws IllegalStateException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(uri);
		HttpResponse httpresponse = httpclient.execute(httppost);
		HttpEntity entity = httpresponse.getEntity();

		return entity != null ? entity.getContent() : null;
	}

	/**
	 * Utility method to ascertain whether connectivity is available, and
	 * whether or not the server is reachable
	 * @param context - Context instance used for getting connectivity service
	 * @return true if server is reachable, false otherwise
	 */
	public static boolean isReachable(WheresMyTrain context) {
		//	First, check we have any sort of connectivity
		final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			//	Some sort of connection is open, check if server is reachable
			try {
				URL url = new URL(TflJsonReader.URL_HOST);
				HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
				urlc.setRequestProperty("User-Agent", "Android Application");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(10 * 1000); // Ten seconds timeout in milliseconds
				urlc.connect();
				if (urlc.getResponseCode() == 200) {
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				Log.e(WheresMyTrain.TAG + ".TflJsonFetcher", e.getMessage());
				return false;
			}
		} else {
			return false;
		}
	}
}
