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

import com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DPPlatform;
import com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DPTrain;

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
		WheresMyTrain w = WheresMyTrain.INSTANCE;
		int textColour = w.getTextColour();
		DPTrain train = (DPTrain) getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) w.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_layout, null);
		}
		TextView tvDest = (TextView) convertView.findViewById(R.id.tvDestination);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvTimeTo);
		tvDest.setTextColor(textColour);
		tvTime.setTextColor(textColour);
		tvDest.setText(train.destination);
		if (train.timeto.equals("-")) {
			tvTime.setText("Waiting");
		} else {
			tvTime.setText(train.timeto);			
		}
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
		tv.setText(platform.platformname);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
