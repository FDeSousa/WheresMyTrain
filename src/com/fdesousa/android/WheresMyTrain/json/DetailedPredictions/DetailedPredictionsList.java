package com.fdesousa.android.WheresMyTrain.json.DetailedPredictions;

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

public class DetailedPredictionsList {
	private DetailedPredictionsContainer detailedpredictions;

	public DetailedPredictionsContainer getDetailedPredictionsListFromUri(URI uri) {
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
		detailedpredictions = gson.fromJson(new InputStreamReader(is), DetailedPredictionsContainer.class);

		try {
			is.close();
		} catch (IOException e) {
			// Can't close the InputStream, log it, but ignore for now
			Log.e(WheresMyTrain.TAG, e.getMessage());
		}

		return detailedpredictions;
	}
}
