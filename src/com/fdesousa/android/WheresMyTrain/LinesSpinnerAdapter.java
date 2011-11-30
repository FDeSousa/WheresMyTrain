package com.fdesousa.android.WheresMyTrain;

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

import java.util.List;

import com.fdesousa.android.WheresMyTrain.json.StationsList.SLLine;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Simple class to handle use a list of SLLine with Android Spinner widget
 * cf. http://stackoverflow.com/questions/6562236/
 * @author Filipe De Sousa
 */
public class LinesSpinnerAdapter implements SpinnerAdapter {

	private List<SLLine> lines;

	public LinesSpinnerAdapter(List<SLLine> lines) {
		this.lines = lines;
	}

	@Override
	public int getCount() {
		return lines.size();
	}

	@Override
	public Object getItem(int position) {
		return lines.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return android.R.layout.simple_spinner_dropdown_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WheresMyTrain w = WheresMyTrain.INSTANCE;
		SLLine line = (SLLine) getItem(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) w.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_row, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.row);
		int colour = w.getLineColour(line.linecode);
		tv.setTextColor(colour);
		tv.setText(line.linename);
		
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return android.R.layout.simple_spinner_dropdown_item;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return this.getView(position, convertView, parent);
	}
}
