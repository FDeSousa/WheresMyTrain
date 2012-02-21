package com.fds.droid.WMT.Library.requests.StationsList;

import android.os.Parcel;
import android.os.Parcelable;

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

/**
 * <b>SLStation</b>
 * <p>Instance of Station in Station List requests.<br/>
 * Stores an instance of Station from Station List request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class StationsListStation implements Parcelable {
	public String stationcode;
	public String stationname;

	public StationsListStation() {
	}

	public StationsListStation(Parcel in) {
		this();
		readFromParcel(in);
	}

	public static final Parcelable.Creator<StationsListStation> CREATOR = new Parcelable.Creator<StationsListStation>() {
		@Override
		public StationsListStation createFromParcel(Parcel source) {
			return new StationsListStation(source);
		}
		@Override
		public StationsListStation[] newArray(int size) {
			return new StationsListStation[size];
		}
	};
	
	public void readFromParcel(Parcel in) {
		this.stationcode = in.readString();
		this.stationname = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(stationcode);
		dest.writeString(stationname);
	}
}