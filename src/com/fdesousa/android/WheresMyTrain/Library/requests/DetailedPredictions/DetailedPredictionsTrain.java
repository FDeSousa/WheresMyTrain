package com.fdesousa.android.WheresMyTrain.Library.requests.DetailedPredictions;

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
 * <b>DPTrain</b>
 * <p>Instance of Train in Detailed Predictions requests.<br/>
 * Stores an instance of Train from Detailed Predictions request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class DetailedPredictionsTrain {
	public String lcid;
	public String secondsto;
	public String timeto;
	public String location;
	public String destination;
	public int destcode;
	public int tripno;
	
	@Override
	public String toString() {
		return "\n\t\t\tlcid:" + lcid
				+ "\n\t\t\tsecondsto:" + secondsto
				+ "\n\t\t\ttimeto:" + timeto
				+ "\n\t\t\tlocation:" + location
				+ "\n\t\t\tdestination:" + destination
				+ "\n\t\t\tdestcode:" + destcode
				+ "\n\t\t\ttripno:" + tripno;
	}
}
