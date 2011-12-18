package com.fdesousa.android.WheresMyTrain.UiElements;

/******************************************************************************
 * Copyright 2011 Filipe De Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

import java.util.ArrayList;
import java.util.List;

import com.fdesousa.android.WheresMyTrain.R;
import com.fdesousa.android.WheresMyTrain.StandardCodes;
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
 * <b>PlatformsExpListAdapter : BaseExpandableListAdapter</b>
 * <p>Adapter to handle using ExpandableList widget with:<ul>
 * <li>Group: DPPlatform</li>
 * <li>Child: DPTrain</li>
 * </ul>
 * cf. http://techdroid.kbeanie.com/2010/09/expandablelistview-on-android.html</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class PlatformsExpListAdapter extends BaseExpandableListAdapter {

	private List<DPPlatform> platforms;

	public PlatformsExpListAdapter(List<DPPlatform> platforms) {
		this.platforms = new ArrayList<DPPlatform>();
		for (DPPlatform platform : platforms) {
			this.platforms.add(platform.filterAndClone());
		}
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

		//	Check destcode does not match Unknown distination first
		if (train.destcode == StandardCodes.UNKNOWN_DESTINATION) {
			//	If so, advise to check front of the train instead
			tvDest.setText(StandardCodes.CHECK_FRONT);
		//	Just to make sure it's not too long, cut down length of the string to 25 characters
		} else if (train.destination.length() > 30) {
			tvDest.setText(train.destination.substring(0, 30));
		} else {
			//	Otherwise, just slam the string in anyway
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
		//	Make sure there is something to display first 
		if (train.location.length() > 0) {
			//	If so, display it
			tvLoc.setText(train.location);
		} else {
			//	If not, advise the user that location is unknown
			tvLoc.setText(StandardCodes.NO_LOCATION);
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
	
	public void clearList() {
		platforms.clear();
		notifyDataSetChanged();
	}
}
