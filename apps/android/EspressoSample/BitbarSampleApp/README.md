## Testdroid Espresso Sample
##### Testdroid sample Android App used as base to demonstrate Espresso

**Use Android Studio:**

clean, rebuild.
run `"BitbarSampleTests"`

or

**From command line:**

```./gradlew clean connectedAndroidTest```

if you get error:

```
You have not accepted the license agreements of the following SDK components:
[Android SDK Build-Tools 26.0.2, Android SDK Platform 14].
```

Run:
```
yes | $ANDROID_HOME/tools/bin/sdkmanager "build-tools;26.0.2"
```

Apks to upload to testdroid cloud are in:

`/BitbarSampleApp/app/build/outputs/apk`

***To run Espresso tests in cloud:***

<pre>
Create new <b>"Android Instrumentation"</b> project.
Upload <b>""app-debug.apk"</b> or "app-release.apk" as the <b>""application"</b> file.
Upload <b>""app-debug-androidTest.apk"</b> or "app-release-androidTest.apk" as the <b>""test"</b> file.
Set this as screenshots directory: <b>/sdcard/Pictures/Screenshots</b> 
</pre>


"debug" or "release" is determined by running either "./gradlew clean connectedDebugAndroidTest" or "./gradlew clean connectedReleaseAndroidTest".
This project only supports "./gradlew clean connectedDebugAndroidTest" or "./gradlew clean connectedAndroidTest"


These commands can be set in `"Use test cases from:"` option in "4. Advanced options:" section in a new cloud test run, if `"class"` is selected:

run tests in class:
```
com.bitbar.testdroid.sampletests.BitbarSampleTests
```

run a test in a class:
```
com.bitbar.testdroid.sampletests.BitbarSampleTests#wrongAnswerTest
```

run 2 specific tests in a class:
```
com.bitbar.testdroid.sampletests.BitbarSampleTests#wrongAnswerTest,com.bitbar.testdroid.sampletests.BitbarSampleTests#rightAnswerTest
```

same commands for local testing use (install "app-debug.apk" and "app-debug-androidTest.apk" with "apk install" command to local device):

run tests in class:
```
adb shell am instrument -w -e class com.bitbar.testdroid.sampletests.BitbarSampleTests com.bitbar.testdroid.sampletests.test/android.support.test.runner.AndroidJUnitRunner
```

run a test in a class:
```
adb shell am instrument -w -e class com.bitbar.testdroid.sampletests.BitbarSampleTests#wrongAnswerTest com.bitbar.testdroid.sampletests.test/android.support.test.runner.AndroidJUnitRunner
```

run 2 specific tests in a class:
```
adb shell am instrument -w -e class com.bitbar.testdroid.sampletests.BitbarSampleTests#wrongAnswerTest,com.bitbar.testdroid.sampletests.BitbarSampleTests#rightAnswerTest com.bitbar.testdroid.sampletests.test/android.support.test.runner.AndroidJUnitRunner
```

## local run specific commands
Check which instrumentation packages have been installed:
```
adb shell pm list instrumentation
```

run tests in package:
```
adb shell am instrument -w com.bitbar.testdroid.sampletests.test/android.support.test.runner.AndroidJUnitRunner
```
