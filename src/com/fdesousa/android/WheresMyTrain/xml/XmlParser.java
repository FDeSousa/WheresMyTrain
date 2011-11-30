package com.fdesousa.android.WheresMyTrain.xml;

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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.fdesousa.android.WheresMyTrain.information.Station;

public abstract class XmlParser extends DefaultHandler {
	public static final int DEFAULT = 0;

	public static final int PREDICTION_DETAILED = 1;
	public static final int PREDICTION_SUMMARY = 2;

	public static final int LINE_STATUS = 3;
	public static final int LINE_STATUS_INCIDENTS = 4;

	public static final int STATION_STATUS = 5;
	public static final int STATION_STATUS_INCIDENTS = 6;

	private final ArrayList<Station> stations;

	protected XmlParser() {
		this.stations = new ArrayList<Station>();
	}

	public final static XmlParser newInstance(URL url, int type) {
		XmlParser mXmlParser = null; // TODO: Create subclasses to instantiate with
		try {
			SAXParserFactory mSaxParserFactory = SAXParserFactory.newInstance();
			SAXParser mSaxParser = mSaxParserFactory.newSAXParser();
			XMLReader mXmlReader = mSaxParser.getXMLReader();
			mXmlReader.setContentHandler(mXmlParser);
			InputSource mInputSource = new InputSource(url.openStream());

			//	To later be able to parse, give mXmlParser instances of reader and source
			mXmlParser.setParser(mXmlReader, mInputSource);

		} catch (ParserConfigurationException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Could not process station data");
		} catch (SAXException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Problem reading in data");
		} catch (IOException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Issue encountered in data");
		}
		return mXmlParser;
	}

	private XMLReader reader;
	private InputSource source;

	private final void setParser(XMLReader reader, InputSource source) {
		this.reader = reader;
		this.source = source;
	}

	public final ArrayList<Station> parse() {
		try {
			reader.parse(source);
		} catch (IOException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Issue encountered in data");
		} catch (SAXException e) {
			Log.e(WheresMyTrain.TAG, e.getMessage());
			WheresMyTrain.displayToast("Problem reading in data");
		}
		return stations;			
	}

	@Override
	public abstract void startDocument() throws SAXException;
	@Override
	public abstract void endDocument() throws SAXException;

	@Override
	public abstract void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException;
	@Override
	public abstract void endElement(String uri, String localName, String qName) throws SAXException;

	@Override
	public abstract void characters(char[] ch, int start, int length) throws SAXException;

}
