sebastiaanschool
================

This app is built for the Sebastiaanschool in Apeldoorn, Netherlands. It
provides up to date information to the students' parents. If you're a parent of
a Sebastiaanschool student, you can download the app from the Google Play
Store:

[![Android app on Google Play][aop]][pls]

There's also [an iOS version][sio].

If you're interested in the source code, that's great, 'cause you're looking in
the right place.

## Building the Android app

You need to download the Parse.com Android library and put it into the
`Android/Sebastiaanschool/libs/` directory. Same for the [Espresso
Standalone][esp] library. After that, you can simply launch the Gradle build
with the included wrapper:

```bash
$ ./gradlew clean assembleDebug installDebug
```

*Note:* Your Parse.com credentials are included in the Android app by means of
buildConfigFields written into `BuildConfig.java`. To make this work, you need
to export some variables into your shell environment (look at
`setEnv-example.sh` for more info). If you fail to do so, the app will crash
at startup. This is intentional.

The required backend code is in the [sebastiaanschool-CloudCode][scc] project.

## Terms of use

"sebastiaanschool" is copyright Jeroen Leenarts, except for the Android app
which is copyright Barend Garvelink. Both parts can be used in accordance with
the Creative Commons Attribution-NonCommercial 3.0 Unported License.

You should have received a copy of the license along with this work.  If not,
see [http://creativecommons.org/licenses/by-nc/3.0/][cc3].

## Acknowledgements

All brand names and icons are trademarks of their respective owners. This app
uses Parse.com as its back-end. Some of the graphics assets have been obtained
from [Font-Awesome][fas], [ActionBar Style Generator][asg] or [Android Asset
Studio][aas].

[aop]: http://developer.android.com/images/brand/en_app_rgb_wo_45.png
[pls]: https://play.google.com/store/apps/details?id=nl.sebastiaanschool.contact.app
[sio]: https://github.com/sebastiaanschool/sebastiaanschool-iOS
[esp]: https://code.google.com/p/android-test-kit/
[scc]: https://github.com/sebastiaanschool/sebastiaanschool-CloudCode
[cc3]: http://creativecommons.org/licenses/by-nc/3.0/
[fas]: http://fortawesome.github.io/Font-Awesome/
[asg]: http://jgilfelt.github.io/android-actionbarstylegenerator/
[aas]: http://romannurik.github.io/AndroidAssetStudio/
