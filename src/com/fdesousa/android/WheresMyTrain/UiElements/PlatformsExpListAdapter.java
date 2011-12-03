package com.fdesousa.android.WheresMyTrain.UiElements;

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

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.WheresMyTrain;
import com.fdesousa.android.WheresMyTrain.requests.DetailedPredictions.DPPlatform;
import com.fdesousa.android.WheresMyTrain.requests.DetailedPredictions.DPTrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * cf. http://techdroid.kbeanie.com/2010/09/expandablelistview-on-android.html
 * @author Filipe De Sousa
 *
 */
public class PlatformsExpListAdapter extends BaseExpandableListAdapter {

	private List<DPPlatform> platforms;

	public PlatformsExpListAdapter(List<DPPlatform> platforms) {
		this.platforms = platforms;
		notifyDataSetChanged();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return platforms.get(groupPosition).trains.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		int textColour = WheresMyTrain.UI_CONTROLLER.getTextColour();
		DPTrain train = (DPTrain) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) WheresMyTrain.INSTANCE.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.child_layout, null);
		}
		//	Show the destination name
		TextView tvDest = (TextView) convertView.findViewById(R.id.tvDestination);
		tvDest.setTextColor(textColour);
		tvDest.setTypeface(WheresMyTrain.UI_CONTROLLER.book);
		//	Just to make sure it's not too long, cut down length of the string to 25 characters
		if (train.destination.length() > 25) {
			tvDest.setText(train.destination.substring(0, 25));			
		} else {
			tvDest.setText(train.destination);
		}

		//	Show how long is predicted until train arrives
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvTimeTo);
		tvTime.setTextColor(textColour);
		tvTime.setTypeface(WheresMyTrain.UI_CONTROLLER.bold);
		if (train.timeto.equals("-")) {
			//	Indicates "At platform", so show nothing
			tvTime.setText("");
		} else {
			tvTime.setText(train.timeto);			
		}
		
		//	Show the location of the train
		TextView tvLoc = (TextView) convertView.findViewById(R.id.tvLocation);
		tvLoc.setTextColor(textColour);
		tvLoc.setTypeface(WheresMyTrain.UI_CONTROLLER.book);
		tvLoc.setText(train.location);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return platforms.get(groupPosition).trains.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return platforms.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return platforms.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		DPPlatform platform = (DPPlatform) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) WheresMyTrain.INSTANCE.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.group_layout, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
		tv.setTypeface(WheresMyTrain.UI_CONTROLLER.bold);
		tv.setText(platform.platformname);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
