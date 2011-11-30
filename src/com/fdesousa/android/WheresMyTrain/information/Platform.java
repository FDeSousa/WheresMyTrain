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

/**
 * <h1>Platform.java</h1>
 * A simple representation of a Platform at a given Station,
 * with a number of Trains related to it, at one given point
 * in time.
 * @author Filipe De Sousa
 * @version 0.1 - Initial version
 */
public class Platform implements Iterable<Train>{
	/**	Name of the platform	*/
	public final String name;
	/**	The platform number		*/
	public final int number;
	/**	Data storage for trains	*/
	private final ArrayList<Train> trains;
	
	public Platform(final String name, final int number) {
		this.name = name;
		this.number = number;
		trains = new ArrayList<Train>();
	}
	
	/**
	 * Append a Train instance to the data structure,
	 * for later retrieval
	 * @param train - a Train instance to append
	 */
	public void appendTrain(final Train train) {
		trains.add(train);
	}

	/**
	 * Returns an Iterator for the ArrayList of Trains,
	 * to allow displaying them in order elsewhere
	 */
	@Override
	public Iterator<Train> iterator() {
		return trains.iterator();
	}
}
