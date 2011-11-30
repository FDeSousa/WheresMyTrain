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

import java.util.ArrayList;
import java.util.Iterator;

public class Station implements Iterable<Platform> {
	/**	The unique station code string						*/
	public final String code;
	/**	The full station name								*/
	public final String name;
	/**	Time the service was run, format hh:MM:ss			*/
	public String curTime;
	
	/**	List of the platforms relevant to Station and Line	*/
	private final ArrayList<Platform> platforms;
	
	public Station(final String code, final String name, final String curTime) {
		this.code = code;
		this.name = name;
		this.curTime = curTime;
		platforms = new ArrayList<Platform>();
	}
	
	public void appendPlatform(final Platform platform) {
		platforms.add(platform);
	}

	@Override
	public Iterator<Platform> iterator() {
		return platforms.iterator();
	}
}