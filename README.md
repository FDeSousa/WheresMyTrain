# Where's My Train?!
## Introduction
An Android application that shows you the next few trains for almost any line and station part of the Transport for London underground network (due to limitations from TfL, DLR and Overground are not included in predictions. Upgrades are being made, and these two lines have yet to be added).  
Uses [tfl.php](http://trains.desousa.com.pt/) for handling requests for predictions and status information.

## Where can I get it?
I don't yet have an Android Market account, so currently the only way to get the app is by either compiling it yourself, or shooting me an email (my email address is in the page footer, just edit the silly part out of it before sending) and I'll send the latest working version.

## Status
Unfinished. Some GUI tweaks are still needed, also still needs to have some connectivity checks to avoid some crashing, etc.  
Still needed before an actual release:

*	Connectivity checks before HTTP requests are made
*	Code clean-up (probably heavier than it needs to be at the minute)
*	Auto-refresh (and adjustment of auto-refresh timing)
*	Fixing of some GUI quirks still existant
*	Application icon (of extreme importance!)

Would like to add:

*	Default line & station saving, ability to load defaults on start-up
*	Variable number of trains in predictions (and option for maximum number)
*	Individual station status fetching
*	Separate activity for displaying status of all lines
*	Journey planner - including per-station status
*	Interactive Underground map
*	Use of GPS to find current location and station

## Platform
Android 1.6+  
There's some iffy workings with the back button pre-Android 2.1, as this overrides onBackPressed, which was added in the Android 2.1 SDK, but otherwise it seems to work well even on an older Android OS version.  
I have no Android 1.5 or below devices, so I can't test on-device, though these are so rare now, I doubt it's a particularly big issue.

## Where can I find out more about the project?
This Github page, but also on the mini-site and project home page: [wmt.desousa.com.pt](http://wmt.desousa.com.pt).

## License
Copyright 2011 Filipe De Sousa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.