package com.fdesousa.android.WheresMyTrain.Library.requests.LineStatus;

import java.net.URI;

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;

public class LSReader extends TflJsonReader<LSContainer> {
	private final boolean incidentsOnly;

	/**
	 * Constructor for Line Status Reader (LSReader), sub-class
	 * of TflJsonReader of type LSContainer<LSContainer>
	 * @param incidentsOnly - true to only fetch lines with incidents, false to fetch all
	 */
	public LSReader(final boolean incidentsOnly) {
		super();
		this.incidentsOnly = incidentsOnly;
	}

	/**
	 * Utility method to handle the request and parsing of JSON for Line Status.
	 * @return instance of LSContainer with the requested results
	 */
	@Override
	public LSContainer get() {
		URI uri = makeUri(LINE_STATUS, null, null, incidentsOnly);
		jsonHandler = new LSHandler(uri);
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

	/**
	 * Convenience method to refresh the line status, without making a completely new request
	 * @return instance of LSContainer with the requested results
	 */
	@Override
	public LSContainer refresh() {
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

}
