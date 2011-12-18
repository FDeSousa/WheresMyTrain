package com.fdesousa.android.WheresMyTrain.requests.StationStatus;

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

import java.net.URI;

import com.fdesousa.android.WheresMyTrain.json.TflJsonHandler;
import com.google.gson.Gson;

/**
 * <b>SSHandler : TflJsonHandler</b>
 * <p>Handler of Station Status requests.<br/>
 * Used to parse JSON with GSON in a manner specific to Station Status.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class SSHandler extends TflJsonHandler {
	private SSContainer stationsstatus;
	
	/**
	 * Constructor. Sets the URI of the request
	 * @param uri - the URI of the data to fetch
	 */
	public SSHandler(URI uri) {
		super(uri);
	}
	
	/**
	 * Simple getter, return the container the JSON was parsed into
	 * @return New SSContainer instance with the fetched data
	 */
	@Override
	public Object getContainer() {
		return stationsstatus;
	}

	@Override
	protected void parseJson() {
		stationsstatus = new Gson().fromJson(json, SSContainer.class);
	}
}
