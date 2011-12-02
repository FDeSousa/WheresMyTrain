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

public class DPTrain {
	public String lcid;
	public String secondsto;
	public String timeto;
	public String location;
	public String destination;
	public String destcode;
	public String tripno;
	
	@Override
	public String toString() {
		return "\n\t\t\tlcid:" + lcid
				+ "\n\t\t\tsecondsto:" + secondsto
				+ "\n\t\t\ttimeto:" + timeto
				+ "\n\t\t\tlocation:" + location
				+ "\n\t\t\tdestination:" + destination
				+ "\n\t\t\tdestcode:" + destcode
				+ "\n\t\t\ttripno:" + tripno;
	}
}
