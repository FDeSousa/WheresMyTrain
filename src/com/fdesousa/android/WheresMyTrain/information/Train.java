package com.fdesousa.android.WheresMyTrain.information;

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

/**
 * <h1>Train.java</h1>
 * A simple representation of a Train at a given Platform, which
 * in turn is represented at a given Station, all at one specific
 * point in time.
 * @author Filipe De Sousa
 * @version 0.1 - Initial version
 */
public class Train {
	//	All of the useful information per-Train from the XML feed is stored here, below
	/**	Unique ID for leading car for the train, useful for checking potential errors	*/
	public final int lcid;
	/**	Trip number of the train (for the day?), certain numbers can mean an error		*/
	public final int tripNo;
	/**	Value representing the 'time to station' for the train, in seconds, format SSS	*/
	public final int secondsTo;
	/**	Current location of the train													*/
	public final String location;
	/**	The name of the destination of the train										*/
	public final String destination;
	/**	Code representing the destination of the train, useful in identifying errors	*/
	public final int destCode;
	
	public Train(final int lcid, final int tripNo, final int secondsTo,
			final String location, final String destination, final int destCode) {
		this.lcid = lcid;
		this.tripNo = tripNo;
		this.secondsTo = secondsTo;
		this.location = location;
		this.destination = destination;
		this.destCode = destCode;
	}
	
	public boolean validate() {
		if (destCode == 0) {
			return true;
		} else {
			return false;
		}
	}
}