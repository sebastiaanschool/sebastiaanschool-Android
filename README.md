sebastiaanschool
================

This app is built for the Sebastiaanschool in Apeldoorn, Netherlands. It
provides up to date information to the students' parents. If you're a parent of
a Sebastiaanschool student, you can download the app from the Google Play
Store:

[![Android app on Google Play](http://developer.android.com/images/brand/en_app_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=nl.sebastiaanschool.contact.app)

There's also [an iOS version](https://github.com/sebastiaanschool/sebastiaanschool-iOS).

If you're interested in the source code, that's great, 'cause you're looking at
it.

## Building the Android app

You need to download the Parse.com Android library and put it into the `Android/Sebastiaanschool/libs/` directory. Same for the [Espresso Standalone][esp] library.

Your Parse.com credentials are merged into the Android app by means of a `ParseConfig.java` generated alongside `BuildConfig.java`.

## Terms of use

"sebastiaanschool" is copyright Jeroen Leenarts, except for the Android app which is copyright Barend Garvelink. Both parts can be used in accordance with the Creative Commons Attribution-NonCommercial 3.0 Unported License.

You should have received a copy of the license along with this work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.


All brand names and icons are trademarks of their respective owners.

[esp]: https://code.google.com/p/android-test-kit/
