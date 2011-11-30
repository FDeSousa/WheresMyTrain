package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.google.gson.Gson;

public class DPHandler extends Thread {
	private DPContainer detailedpredictions;
	private URI uri;

	public DPContainer getDPContainer() {
		return detailedpredictions;
	}
	
	/**
	 * Convenience method to set the URI. Can use in method chaining
	 * @param uri - the URI of the data to fetch
	 * @return this instance - for method chaining
	 */
	public DPHandler setUri(URI uri) {
		this.uri = uri;
		return this;
	}

	/**
	 * Fetches the JSON from the server, parses that into DPContainer
	 * As this can take a little while, decided to place into a thread
	 */
	@Override
	public void run() {
		InputStream is = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);
			HttpResponse httpresponse = httpclient.execute(httppost);
			HttpEntity entity = httpresponse.getEntity();
			is = entity.getContent();
		} catch (IllegalStateException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Problem fetching the data");
		} catch (IOException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Problem opening the data");
		}
		
		Gson gson = new Gson();
		detailedpredictions = gson.fromJson(new InputStreamReader(is), DPContainer.class);

		try {
			is.close();
		} catch (IOException e) {
			// Can't close the InputStream, log it, silently ignore
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}
	}
}
