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

import java.net.URI;

import com.fdesousa.android.WheresMyTrain.json.TflJsonHandler;
import com.google.gson.Gson;

/**
 * <b>LSHandler : TflJsonHandler</b>
 * <p>Handler of Line Status requests.<br/>
 * Used to parse JSON with GSON in a manner specific to Line Status.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class LSHandler extends TflJsonHandler {
	private LSContainer linesstatus;

	/**
	 * Constructor. Sets the URI of the request
	 * @param uri - the URI of the data to fetch
	 */
	public LSHandler(URI uri) {
		super(uri);
	}

	/**
	 * Simple getter, return the container the JSON was parsed into
	 * @return New SPContainer instance with the fetched data
	 */
	public Object getContainer() {
		return linesstatus;
	}

	@Override
	protected void parseJson() {
		linesstatus = new Gson().fromJson(json, LSContainer.class);
	}
}
