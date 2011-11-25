package com.fdesousa.android.WheresMyTrain.information;

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
