package com.fdesousa.android.WheresMyTrain.Library.requests.StationStatus;

import java.net.URI;

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonReader;

public class StationStatusReader extends TflJsonReader<StationStatusContainer> {
	private final boolean incidentsOnly;
	
	/**
	 * Constructor for Station Status Reader (SSReader), sub-class
	 * of TflJsonReader of type SSContainer<SSContainer>
	 * @param incidentsOnly - true to only fetch lines with incidents, false to fetch all
	 */
	public StationStatusReader(final boolean incidentsOnly) {
		super();
		this.incidentsOnly = incidentsOnly;
	}
	
	/**
	 * Utility method to handle the request and parsing of JSON for Station Status.
	 * @return instance of SSContainer with the requested results
	 */
	@Override
	public StationStatusContainer get() {
		URI uri = makeUri(STATION_STATUS, null, null, incidentsOnly);
		jsonHandler = new StationStatusHandler(uri);
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

	/**
	 * Convenience method to refresh the station status, without making a completely new request
	 * @return instance of SSContainer with the requested results
	 */
	@Override
	public StationStatusContainer refresh() {
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}
}
