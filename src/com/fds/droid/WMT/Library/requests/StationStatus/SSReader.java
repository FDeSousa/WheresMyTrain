package com.fds.droid.WMT.Library.requests.StationStatus;

import java.net.URI;

import com.fds.droid.WMT.Library.json.TflJsonReader;

public class SSReader extends TflJsonReader<SSContainer> {
	private final boolean incidentsOnly;
	
	/**
	 * Constructor for Station Status Reader (SSReader), sub-class
	 * of TflJsonReader of type SSContainer<SSContainer>
	 * @param incidentsOnly - true to only fetch lines with incidents, false to fetch all
	 */
	public SSReader(final boolean incidentsOnly) {
		super();
		this.incidentsOnly = incidentsOnly;
	}
	
	/**
	 * Utility method to handle the request and parsing of JSON for Station Status.
	 * @return instance of SSContainer with the requested results
	 */
	@Override
	public SSContainer get() {
		URI uri = makeUri(STATION_STATUS, null, null, incidentsOnly);
		jsonHandler = new SSHandler(uri);
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

	/**
	 * Convenience method to refresh the station status, without making a completely new request
	 * @return instance of SSContainer with the requested results
	 */
	@Override
	public SSContainer refresh() {
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}
}
