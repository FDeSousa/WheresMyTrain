package com.fdesousa.android.WheresMyTrain;

import java.util.List;

import com.fdesousa.android.WheresMyTrain.json.StationsList.SLStation;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Simple class to handle use a list of SLStation with Android Spinner widget
 * cf. http://stackoverflow.com/questions/6562236/
 * @author Filipe De Sousa
 */
public class StationsSpinnerAdapter implements SpinnerAdapter {

	private List<SLStation> stations;
	
	public StationsSpinnerAdapter(List<SLStation> stations) {
		this.stations = stations;
	}
	
	@Override
	public int getCount() {
		return stations.size();
	}

	@Override
	public Object getItem(int position) {
		return stations.get(position);
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
		TextView v = new TextView(WheresMyTrain.INSTANCE);
        v.setTextColor(Color.BLACK);
        v.setText(stations.get(position).stationname);
        return v;
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
