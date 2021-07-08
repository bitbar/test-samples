## :no_entry: Deprecated :no_entry:

Detox is not officially supported on BitBar device cloud. We'll check
again when the Detox project brings proper real device support for
both iOS and Android.


Sample app / project to demonstrate Detox in Bitbar cloud. Project is based on `demo-react-native` example from Wix/detox repo:
https://github.com/wix/detox/tree/master/examples/demo-react-native

Note that only Android works on real devices at the moment (android 5.0 and up), iOS can only be run with simulators.

Detox
-------------


Install (with Homebrew) (for local use / test developement)
-------------

source: https://github.com/wix/detox/blob/master/docs/Introduction.GettingStarted.md

	- For simulator (iOS)
	Install appleSimUtils:

	1. brew tap wix/brew
	2. brew install applesimutils

	Install Detox command line tools (detox-cli)
	1. npm install -g detox-cli



Build project and run test locally in short
-------------------- 


	1. clone repo, go to detox/sample-react-native-bitbar -folder
	
	2. Install dependencies
 		- `npm install`
	
	3. Start detox server (for Android real device)
		- `"${PWD}/node_modules/.bin/detox" run-server &`
	
	4. Connect Android device to detox server port
		- `adb reverse tcp:8099 tcp:8099` 
	
	5. Build app
		- change device id in package.json `android.device.release.local` configuration
		- (Android) `"${PWD}/node_modules/.bin/detox" build --configuration android.device.release.local`
		- (iOS) `"${PWD}/node_modules/.bin/detox" build --configuration ios.sim.release`
	
	6. Run test
		- (Android) `"${PWD}/node_modules/.bin/detox" test --configuration android.device.release.local`
		- (iOS) `"${PWD}/node_modules/.bin/detox" test --configuration ios.sim.release`



How to make `demo-react-native` project run on Bitbar cloud (Android)
-----------------------------------------------------

modified content to add to vanilla project (`demo-react-native` example) is highlighted.


**add to package.json:**

mocha-jenkins-reporter:
for test result xml creation.

<pre>
"devDependencies": {
    "detox": "^7.2.0",
    "mocha": "^4.0.1",
    <b>"mocha-jenkins-reporter": "^0.3.10"</b>
  },
</pre>


build configuration for Android real device:

device type:

<pre>
"type": "android.<b>attached</b>" = real device
"type": "android.<b>emulator</b>" = emulator
</pre>


address to connect the device (with sessionId):

```
"session": {
          "server": "ws://localhost:8099",
          "sessionId": "test"
        }
```

<pre>
"detox": {
    "test-runner": "mocha",
    "specs": "e2e",
    "runner-config": "e2e/mocha.opts",
    "configurations": {
      "android.emu.debug": {
        "binaryPath": "android/app/build/outputs/apk/debug/app-debug.apk",
        "build": "pushd android && ./gradlew assembleDebug assembleAndroidTest -DtestBuildType=debug && popd",
        "type": "android.emulator",
        "name": "Nexus_5X_API_24_-_GPlay"
      },
      "android.emu.release": {
        "binaryPath": "android/app/build/outputs/apk/release/app-release.apk",
        "build": "pushd android && ./gradlew assembleRelease assembleAndroidTest -DtestBuildType=release && popd",
        "type": "android.emulator",
        "name": "Nexus_5X_API_24_-_GPlay"
      }<b>,
      "android.device.release": {
        "binaryPath": "android/app/build/outputs/apk/release/app-release.apk",
        "build": "pushd android && ./gradlew assembleRelease assembleAndroidTest -DtestBuildType=release && popd",
        "session": {
          "server": "ws://localhost:8099",
          "sessionId": "test"
        },
        "type": "android.attached",
        "name": "ADD_DEVICE_ID_HERE"
      }"</b>
</pre>


note:
`"name": "ADD_DEVICE_ID_HERE"`
value for this is fetched from `run-tests-android.sh` file and if it is changed manually here it will stop working. 
For local machine runs, add a new configuration and use it for local runs (just copy "android.device.release" block and rename it "android.device.release.local" or something)


**e2e/mocha.opts:**

<pre>
--recursive
--timeout 60000
--bail
<b>--reporter mocha-jenkins-reporter
--reporter-options junit_report_path=test-report.xml</b>
</pre>

`--bail` makes all tests to fail (not run) when a test fails, to continue running tests after a failure, remove it from mocha.opts.


**run-tests.sh:**

cloud expects to see this file included in test files, contents of it will replaced with "run-tests-android.sh" (use "zip-test-files-android.sh" script)

**run-tests-android.sh:**

screenshots will be stored in project root/screenshots folder

```
screenshots folder creation:
export SCREENSHOTSFOLDER=$PWD/screenshots
rm -rf "$SCREENSHOTSFOLDER"
mkdir -p "$SCREENSHOTSFOLDER"
```

<pre>
install dependencies:
<b>npm install</b>

launch detox server:
<b>"${PWD}/node_modules/.bin/detox" run-server &</b>

connect device to detox server port:
<b>adb reverse tcp:8099 tcp:8099</b>

pass device id to package.json:
<b>sed -i.bu "s/ADD_DEVICE_ID_HERE/$UDID/"       package.json</b>

run detox test:
<b>"${PWD}/node_modules/.bin/detox" test --configuration android.device.release --loglevel verbose > detox.log 2>&1</b>
</pre>


when the project is built, apks are in this location:
`android/app/build/outputs/apk/`

if modifications to tests are made, project needs to be rebuilt with this command (locally):
`"${PWD}/node_modules/.bin/detox" build --configuration android.device.release`
(use `release` build, debug build needs "react-native packager" running at the machine and it is not configured in this sample project)


How to create an Android cloud test run:
-------------------------------

- use `"zip-test-files-android.sh"` script to create a test package to upload to Bitbar cloud

- create "Appium Android server side" project (test does not use Appium, no detox type project yet available)

- upload apk-file to the cloud project (apk can be whatever because this uploaded apk is a dummy apk just to make the cloud happy (the apk will be installed on the device so it must be a working apk but it is not used in testing))

- upload `"android-test-files.zip"` zip-file created with "zip-test-files-android.sh" script to the cloud project



How to make `demo-react-native` project run on Bitbar cloud (iOS)
-----------------------------------------------------

iOS runs will be simulator runs, not real device. Device name must be manually set in package.json:


<pre>
{
      "ios.sim.release": {
        "binaryPath": "ios/build/Build/Products/Release-iphonesimulator/sampleproject.app",
        "build": "export RCT_NO_LAUNCH_PACKAGER=true && xcodebuild -project ios/sampleproject.xcodeproj -scheme sampleproject -configuration Release -sdk iphonesimula$
        "type": "ios.simulator",
         <b>"name": "iPhone 7 Plus"</b>
      }
</pre>


when the project is built (release), app will be in this location:
`ios/build/Build/Products/Release-iphonesimulator/sampleproject.app`

if modifications to tests are made, project needs to be rebuilt with this command (locally):
`"${PWD}/node_modules/.bin/detox" build --configuration ios.sim.release`
(use `release` build, debug build needs "react-native packager" running at the machine and it is not configured in this sample project)


How to create an iOS simulator cloud test run:
-------------------------------

- use `"zip-test-files-ios.sh"` script to create a test package to upload to Bitbar cloud

- create "Appium iOS server side" project (test does not use Appium, no detox type project yet available)

- upload ipa-file to the cloud project (ipa can be whatever because this uploaded ipa is a dummy ipa just to make the cloud happy (the ipa will be installed on the device so it must be a working ipa but it is not used in testing))

- upload `"ios-test-files.zip"` zip-file created with "zip-test-files-ios.sh" script to the cloud project
