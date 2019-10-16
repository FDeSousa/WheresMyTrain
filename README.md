# Where's My Train?! hacktober
## Introduction
An Android application that shows you the predicted trains arriving for almost any line* and station part of the Transport for London underground network within the next 30 minutes**.  
Uses [tfl.php](http://trains.desousa.com.pt/) for handling requests for predictions and status information.

#### Why the asterisks?
\* TfL does not run the C2C, DLR, Overground or Tramlink lines, hence their exclusion from the list.

** Some older lines (and others that just have yet to be upgraded) do not have the facilities for predictions that newer lines have. For example, at stations on the Jubilee, predictions are particularly good, but don’t expect the District line to always give accurate predictions.

## Where can I get it?
If you’re feeling adventurous, pull the source from GitHub, import into Eclipse, and you should hopefully be able to build the project first time. Included in the lib folder is an essential library (explained below).  
If you’d just like to install it right away, and have access to the Android Market, then [the link you want is right here.](https://market.android.com/details?id=com.fdesousa.android.WheresMyTrain)  
Please do leave (hopefully constructive) comments, and email if you have bigger queries/suggestions while using it.

## Status
Unfinished, but working admirably in my own out-and-about tests, with low mobile signal and all. Despite being somewhat biased, it is particularly stable and fast at loading data.  
Still needed before v1.0 release:

*	Better connectivity checking before attempting to fetch data
*	Code clean-up - some classes do too much work
*	Auto-refresh and a visible refresh button
*	Better guidance for use, as it doesn’t explain where to press at first

Would like to add:

*	Default line & station saving, ability to load defaults on start-up
*	Variable number of trains in predictions
*	Individual station status fetching
*	Separate activity for displaying status of all lines
*	Journey planner - including per-station status
*	Interactive Underground map
*	Use of GPS to find current location and station

## Platform
Android 2.1+
There's some iffy workings with the back button pre-Android 2.1, as this overrides onBackPressed, which was added in the Android 2.1 SDK. If you’d like to test and confirm to me whether Android 1.5/1.6 can be supported, then that would be very useful, as I currently have no older test devices.

## Where can I find out more about the project?
The source is [hosted on GitHub.](https://github.com/FDeSousa/WheresMyTrain)  
General information is on the [project page on my site.](http://desousa.com.pt/wmt)  
The application itself [on Android Market.](https://market.android.com/details?id=com.fdesousa.android.WheresMyTrain)  

## License
Copyright 2012 Filipe De Sousa

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  
You may obtain a copy of the License at  
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.
