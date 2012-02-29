package com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions;

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

import java.util.ArrayList;
import java.util.List;

/**
 * <b>DPStation</b>
 * <p>Instance of Station in Detailed Predictions requests.<br/>
 * Stores an instance of Station from Detailed Predictions request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class DetailedPredictionsStation {
	public String stationcode;
	public String stationname;
	public List<DetailedPredictionsPlatform> platforms = new ArrayList<DetailedPredictionsPlatform>();
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		
		out.append("\n\tstationcode:");
		out.append(stationcode);
		out.append("\n\tstationname:");
		out.append(stationname);
		out.append("\n\tplatforms:{");
		
		for (DetailedPredictionsPlatform platform : platforms) {
			out.append(platform.toString());
		}
		
		out.append("\n\t}");
		
		return out.toString();
	}
}
