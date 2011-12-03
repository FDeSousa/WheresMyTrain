# Where's My Train?!

## What is this? What does it do?
Really simple Android app to show detailed predictions and service status for almost all Transport for London Underground (TfLU) lines.  
Currently sends requests exclusively to [tfl.php](trains.desousa.com.pt), and relays the JSON response to the user in a minimalist, functional UI.  
DLR and Overground lines and stations are not currently included, due to Transport for London's data feeds. They will be added as soon as TfL adds support for them, as the entire system is being upgraded.

## What does it run on?
Android 1.6+  
There's some iffy workings with the back button pre-Android 2.1, as this overrides onBackPressed, which was added in the Android 2.1 SDK, but otherwise it seems to work well even on an older Android OS version.  
I have no Android 1.5 or below devices, so I can't test on-device, though these are so rare now, I doubt it's a particularly big issue.

## What features does it have?
Current list of stations and lines for almost all TfLU lines (missing DLR and Overground, as stated above), and detailed predictions for all stations showing the trains arriving within the next 30 minutes (some stations haven't been upgraded to display the next 30 minutes, just 2-5 minutes predictions so far).  
The UI is minimalist and functional, but hopefully with some flair, though I'm certainly no designer. Colour-coded elements are to aide in identifying train lines (though I'd be interested in getting opinion from someone colour-blind), and add to the visual flair.  
Line status presented on a colour-coded button. The button will eventually open up a dialog that will display more information on status, but for now, simply states the description supplied by TfL. The background is green for good service, orange otherwise. Hopefully will find the code for closed line to add a red background too.  
Surprisingly fast, thanks to the use of asynchronous tasks, a thoroughly underused server, and a fast Internet connection. Not to mention some server-side caching to help speed things up even more.

### Future development goals

*	Status dialog to display more information on line and station status
*	Station status display (along with the line status already included)
*	Shorten list of predictions based on screen resolution (more for convenience)
*	Removal of trains with unknown destinations and destination codes  
	This means trains that don't pass by the platform. They appear in predictions, but generally show "Unknown" destination, and a destination code reserved for use by out-of-service trains
*	Pull-to-refresh (there are a few good libraries, but have yet to try implementing)
*	Auto-refresh after user-chosen time
*	Settings activity
*	Default station saving and retrieving (attempted to implement, but did not work at the time)

## How can I use it?
Good question. For the minute, the only way to use it is to build it.  
Clone this repo via git, import into Eclipse or some other Java IDE that has the Android SDK, then build.  
This is not quite ready for the prime, so do expect it may spit out bugs.

## License
Copyright 2011 Filipe De Sousa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.