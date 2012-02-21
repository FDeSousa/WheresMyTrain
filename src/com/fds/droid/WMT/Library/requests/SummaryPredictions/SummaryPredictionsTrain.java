package com.fds.droid.WMT.Library.requests.SummaryPredictions;

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
 * <b>SPTrain</b>
 * <p>Instance of Train in Summary Predictions requests.<br/>
 * Stores an instance of Train from Summary Predictions request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class SummaryPredictionsTrain {
	public int trainnumber;
	public int tripno;
	public int destcode;
	public String destination;
	public String timeto;
	public String location;
}
