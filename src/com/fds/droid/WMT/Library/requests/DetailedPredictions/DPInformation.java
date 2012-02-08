package com.fds.droid.WMT.Library.requests.DetailedPredictions;

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
 * <b>DPInformation</b>
 * <p>Instance of Information from Detailed Predictions requests<br/>
 * Stores an instance of Information from Detailed Predictions request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class DPInformation {
	public String created;
	public String linecode;
	public String linename;

	@Override
	public String toString() {
		String out = "\n\tcreated:" + created
				+ "\n\tlinecode:" + linecode
				+ "\n\tlinename:" + linename;
		return out;
	}
}
