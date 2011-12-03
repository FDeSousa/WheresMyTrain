package com.fdesousa.android.WheresMyTrain.requests.LineStatus;

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
 * <b>LSLine</b>
 * <p>Instance of Line in Line Status requests.<br/>
 * Stores an instance of Line from Line Status request JSON syntax.</p>
 * @author Filipe De Sousa
 * @version 0.7
 */
public class LSLine {
	public String id;
	public String details;
	public String lineid;
	public String linename;
	public String statusid;
	public String status;
	public String description;
	public String active;
}
