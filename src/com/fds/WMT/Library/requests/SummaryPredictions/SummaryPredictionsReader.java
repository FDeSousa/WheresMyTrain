package com.fds.WMT.Library.requests.SummaryPredictions;

import java.net.URI;

import com.fds.WMT.Library.json.TflJsonReader;

public class SummaryPredictionsReader extends TflJsonReader<SummaryPredictionsContainer> {
	private final String line;
	
	/**
	 * Constructor for Summary Predictions Reader (SPReader), sub-class
	 * of TflJsonReader of type SPContainer<SPContainer>
	 * @param line - the code for the Underground line to make a request for
	 */
	public SummaryPredictionsReader(final String line) {
		super();
		this.line = line;
	}

	/**
	 * Utility method to handle the request and parsing of JSON for Summary Predictions.
	 * @param line - the code for the Underground line to make a request for
	 * @return instance of SPContainer with the requested results
	 */
	public SummaryPredictionsContainer get() {
		//	Parses String "PredictionSummary/line"
		URI uri = makeUri(PREDICTION_SUMMARY, line, null, false);
		jsonHandler = new SummaryPredictionsHandler(uri);
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

	/**
	 * Convenience method to refresh the summary predictions, without making a completely new request
	 * @return instance of SPContainer with the requested results
	 */
	public SummaryPredictionsContainer refresh() {
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

}
