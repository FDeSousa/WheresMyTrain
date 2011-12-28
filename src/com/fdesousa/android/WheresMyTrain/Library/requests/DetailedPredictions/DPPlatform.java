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

import com.fdesousa.android.WheresMyTrain.Library.LibraryMain;

/**
 * <b>DPPlatform</b>
 * <p>Instance of Platform in Detailed Predictions requests.<br/>
 * Stores an instance of Platform from Detailed Predictions request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class DPPlatform {
	public String platformname;
	public int platformnumber;
	public List<DPTrain> trains = new ArrayList<DPTrain>();

	/**
	 * Utility method to make a shallow clone of this Platform, and filter its List
	 * of the unnecessary trains information (out of service/no trip trains)
	 * @return copy of this object, with filtered trains List
	 */
	public DPPlatform filterAndClone() {
		DPPlatform platform = new DPPlatform();
		platform.platformname = this.platformname;
		platform.platformnumber = this.platformnumber;
		
		//	Copied basic information, now filter list of trains
		for (DPTrain train : trains) {
			if (train.destcode != LibraryMain.OUT_OF_SERVICE &&
					train.tripno != LibraryMain.NO_TRIP) {
				platform.trains.add(train);
			}
		}
		return platform;
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		out.append("\n\t\tplatformname:");
		out.append(platformname);
		out.append("\n\t\tplatformnumber:");
		out.append(platformnumber);
		out.append("\n\t\ttrains:{");

		for (DPTrain train : trains) {
			out.append(train.toString());
		}

		out.append("\n\t\t}");

		return out.toString();
	}
}
