package com.fds.droid.WMT.Library.requests.StationsList;

import java.io.File;
import java.net.URI;

import com.fds.droid.WMT.Library.json.TflJsonReader;

public class SLReader extends TflJsonReader<SLContainer> {
	private File cacheDir;

	/**
	 * Constructor for Stations List Reader (SLReader), sub-class
	 * of TflJsonReader of type SLContainer<SLContainer></br>
	 * This is the only sub-class that caches its JSON responses
	 * @param cacheDir - File instance preferably pointing to application's cache directory
	 */
	public SLReader(File cacheDir) {
		super();
	}

	/**
	 * Utility method to handle the request and parsing of JSON for Stations List.
	 * @return instance of SLContainer with the requested results
	 */
	@Override
	public SLContainer get() {
		URI uri = makeUri(STATIONS_LIST, null, null, false);
		jsonHandler = new SLHandler(cacheDir, uri);
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

	/**
	 * Convenience method to refresh the stations list, without making a completely new request
	 * @return instance of SLContainer with the requested results
	 */
	@Override
	public SLContainer refresh() {
		jsonHandler.start();
		stopHandler(jsonHandler);
		return jsonHandler.getContainer();
	}

}
