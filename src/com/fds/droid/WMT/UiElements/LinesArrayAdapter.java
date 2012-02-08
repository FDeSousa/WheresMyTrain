package com.fds.droid.WMT.UiElements;

import java.util.List;

import com.fdesousa.android.WheresMyTrain.R;
import com.fds.droid.WMT.Library.requests.StationsList.SLLine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LinesArrayAdapter extends ArrayAdapter<SLLine> {
	private UiController uiController;

	public LinesArrayAdapter(Context context, List<SLLine> objects, UiController uiController) {
		super(context, R.layout.spinner_row, objects);
		this.uiController = uiController;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SLLine line = getItem(position);

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
