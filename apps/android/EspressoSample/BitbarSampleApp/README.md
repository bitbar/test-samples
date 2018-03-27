## Android Espresso Sample
##### Sample Android App to demonstrate Espresso tests

**Use Android Studio:**

clean, rebuild.
run `"BitbarSampleTests"`

or

**From command line:**

```./gradlew clean connectedAndroidTest```


Apks to upload to testdroid cloud will be in (after project is built):

`/BitbarSampleApp/app/build/outputs/apk`

***To run Espresso tests in cloud:***

<pre>
Create new <b>"Android Instrumentation"</b> project.
Upload <b>""app-debug.apk"</b> as the <b>""application"</b> file.
Upload <b>""app-debug-androidTest.apk"</b> as the <b>""test"</b> file.
Set as "Custom test runner" value: <b>android.support.test.runner.AndroidJUnitRunner</b>
Set this as screenshots directory: <b>/sdcard/Pictures/Screenshots</b> 
</pre>


These commands can be set in `"Use test cases from:"` option in "4. Advanced options:" section in a new cloud test run, if `"class"` is selected:

run tests in class:
```
com.bitbar.testdroid.sampletests.BitbarSampleTests
```

run a test in a class:
```
com.bitbar.testdroid.sampletests.BitbarSampleTests#wrongAnswerTest
```
