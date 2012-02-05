package com.fdesousa.android.WheresMyTrain.Library.requests.SummaryPredictions;

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

import com.fdesousa.android.WheresMyTrain.Library.json.TflJsonHandler;
import com.fdesousa.google.gson.Gson;

/**
 * <b>SPHandler : TflJsonHandler</b>
 * <p>Handler of Summary Predictions requests.<br/>
 * Used to parse JSON with GSON in a manner specific to Summary Predictions.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class SPHandler extends TflJsonHandler<SPContainer> {
	private SPContainer summarypredictions;

	/**
	 * Constructor. Sets the URI of the request
	 * @param uri - the URI of the data to fetch
	 */
	public SPHandler(URI uri) {
		super(uri);
	}

	/**
	 * Simple getter, return the container the JSON was parsed into
	 * @return New SPContainer instance with the fetched data
	 */
	@Override
	public SPContainer getContainer() {
		return summarypredictions;
	}

	@Override
	protected void parseJson() {
		summarypredictions = new Gson().fromJson(json, SPContainer.class);
	}
}
