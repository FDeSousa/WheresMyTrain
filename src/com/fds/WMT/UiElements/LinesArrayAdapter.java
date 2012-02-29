package com.fds.WMT.UiElements;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fds.WMT.R;
import com.fds.WMT.Library.requests.StationsList.StationsListLine;

public class LinesArrayAdapter extends ArrayAdapter<StationsListLine> {
	private UiController uiController;

	public LinesArrayAdapter(Context context, List<StationsListLine> objects, UiController uiController) {
		super(context, R.layout.spinner_row, objects);
		this.uiController = uiController;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StationsListLine line = getItem(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.spinner_row, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.row);
		int colour = uiController.getLineColour(line.linecode);
		tv.setTextColor(colour);
		tv.setTypeface(uiController.book);
		tv.setText(line.linename);

		return convertView;
	}
}
