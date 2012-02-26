package com.fds.WMT.Library.requests.StationsList;

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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>SLLine</b>
 * <p>Instance of Line in Stations List requests.<br/>
 * Stores an instance of Line from Stations List request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class StationsListLine implements Parcelable {
	public String linecode;
	public String linename;
	public ArrayList<StationsListStation> stations;

	//	Need a Creator for reading from a Parcel at some point
	public static final Parcelable.Creator<StationsListLine> CREATOR = new Parcelable.Creator<StationsListLine>() {
		@Override
		public StationsListLine createFromParcel(Parcel source) {
			return new StationsListLine(source);
		}
		@Override
		public StationsListLine[] newArray(int size) {
			return new StationsListLine[size];
		}
	};

	public StationsListLine() {
		stations = new ArrayList<StationsListStation>();
	}

	public StationsListLine(Parcel in) {
		this();
		this.readFromParcel(in);
	}

	private void readFromParcel(Parcel in) {
		this.linecode = in.readString();
		this.linename = in.readString();
		in.readTypedList(stations, StationsListStation.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(linecode);
		dest.writeString(linename);
		dest.writeTypedList(stations);
	}

}
