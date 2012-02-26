package com.fds.WMT.UiElements;

import java.util.List;

import com.fdesousa.android.WheresMyTrain.R;
import com.fds.WMT.Library.requests.StationsList.StationsListStation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StationsArrayAdapter extends ArrayAdapter<StationsListStation> {
	private UiController uiController;
	private int colour;

	public StationsArrayAdapter(Context context, List<StationsListStation> objects,
			UiController uiController) {
		super(context, R.layout.spinner_row, objects);
		this.uiController = uiController;
		this.colour = uiController.getTextColour();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StationsListStation station = getItem(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.spinner_row, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.row);
		tv.setTextColor(colour);
		tv.setTypeface(uiController.book);
		tv.setText(station.stationname);

		return convertView;
	}

}
