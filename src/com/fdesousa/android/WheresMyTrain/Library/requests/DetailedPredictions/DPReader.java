package com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions;

import java.net.URI;

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;

public class DPReader extends TflJsonReader<DPContainer> {
	private final String line;
	private final String station;

	/**
	 * Constructor for Detailed Predictions Reader (DPReader), sub-class
	 * of TflJsonReader of type DPContainer<DPContainer>
	 * @param cacheDir - File instance pointing to application's cache directory
	 * @param line - the code for the Underground line to make a request for
	 * @param station - the code for the Underground station to make a request for
	 */
	public DPReader(final String line, final String station) {
		super();
		this.line = line;
		this.station = station;
	}

	/**
	 * Utility method to handle the request and parsing of JSON for Detailed Predictions
	 * @return instance of DPContainer with the requested results
	 */
	@Override
	public DPContainer get() {
		//	Parses String "PredictionDetailed/line/station"
		URI uri = makeUri(PREDICTION_DETAILED, line, station, false);
		//	Instantiate, set the newest URI to fetch and parse
		jsonHandler = new DPHandler(uri);
		//	Start the thread for fetching and parsing
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

	/**
	 * Convenience method to refresh the detailed predictions, without making a completely new request
	 * @return instance of DPContainer with the requested results
	 */
	@Override
	public DPContainer refresh() {
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}
}