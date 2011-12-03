package com.fdesousa.android.WheresMyTrain.requests.LineStatus;

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

public class LSContainer {
	public String requesttype;
	public ArrayList<LSLine> lines = new ArrayList<LSLine>();
	
	/**
	 * Very simple, slow, arraylist search. Loops through each line to find
	 * the line with the matching linename
	 * @param linename - the name of the line to search for
	 * @return instance of matching line, or null if not found
	 */
	public LSLine searchByLinename(String linename) {
		//	If there's nothing to search through, exit early
		if (lines == null || lines.isEmpty()) return null;
		//	Search with foreach for the right line
		for (LSLine line : lines) {
			if (line.linename.equals(linename)) return line;
		}
		//	If nothing has been found, return null
		return null;
	}
}
