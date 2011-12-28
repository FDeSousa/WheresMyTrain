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
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
	 * @return InputStream containing the response of the request
	 * @throws IllegalStateException - in case of a problem, or if connection was aborted
	 * @throws IOException - if the stream could not be created
	 */
	public static InputStream fetchNewJson(URI uri) throws IllegalStateException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(uri);
		HttpResponse httpresponse = httpclient.execute(httppost);
		HttpEntity entity = httpresponse.getEntity();

		return entity.getContent();
	}
}
