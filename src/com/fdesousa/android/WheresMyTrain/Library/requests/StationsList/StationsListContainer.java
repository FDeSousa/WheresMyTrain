package com.fdesousa.android.WheresMyTrain.Library.requests.StationsList;

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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>SLContainer</b>
 * <p>Container of Stations List.<br/>
 * Used when parsing JSON with GSON.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class StationsListContainer implements Parcelable {
	public String requesttype;
	public List<StationsListLine> lines;
	
	//	Need a Creator for reading from a Parcel at some point
	public static final Parcelable.Creator<StationsListContainer> CREATOR = new Parcelable.Creator<StationsListContainer>() {
		@Override
		public StationsListContainer createFromParcel(Parcel source) {
			return new StationsListContainer(source);
		}
		@Override
		public StationsListContainer[] newArray(int size) {
			return new StationsListContainer[size];
		}
	};
	
	public StationsListContainer() {
		lines = new ArrayList<StationsListLine>();
	}
	
	public StationsListContainer(Parcel in) {
		this();
		this.readFromParcel(in);
	}
	
	private void readFromParcel(Parcel in) {
		this.requesttype = in.readString();
		in.readTypedList(lines, StationsListLine.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(requesttype);
		dest.writeTypedList(lines);
	}
	
	/**
	 * Utility method to find the right StationsListLine by the
	 * passed-in linecode value
	 * @param linecode - String linecode value to match against
	 * @return StationsListLine instance that corresponds with linecode
	 */
	public StationsListLine getLineByLineCode(String linecode) {
		for (StationsListLine s : lines) {
			if (s.linecode.equals(linecode)) return s;
		}
		return null;
	}
}
