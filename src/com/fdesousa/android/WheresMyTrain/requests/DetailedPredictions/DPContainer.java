package com.fdesousa.android.WheresMyTrain.requests.DetailedPredictions;

/*****************************************************************************************************
 *	Copyright (c) 2011 Filipe De Sousa
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *	associated documentation files (the "Software"), to deal in the Software without restriction,
 *	including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *	sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or
 *	substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *	NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *	DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************************/

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
