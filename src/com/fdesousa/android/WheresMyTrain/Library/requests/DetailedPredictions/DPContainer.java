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
 * <b>DPContainer</b>
 * <p>Container of Detailed Predictions.<br/>
 * Used when parsing JSON with GSON.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class DPContainer {
	public String requesttype;
	public DPInformation information;
	public List<DPStation> stations = new ArrayList<DPStation>();

	/**
	 * Using for early-on diagnostics, just to avoid the slow ADB debugger
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("requesttype:");
		sb.append(requesttype);
		sb.append("\ninformation:{");
		sb.append(information.toString());
		sb.append("\nstations:{");

		for (DPStation station : stations) {
			sb.append(station.toString());
		}

		sb.append("\n}");
		return sb.toString();
	}
}
