***flutter-bitbar-cloud***
---------------------------

*Unit tests*
-------------

- does not require device connected
- add in `pubspec.yaml`
  - in `dev_dependencies:`
   - test: any
- create `test` directory
- (optional) create directory for unit tests
- create dart file ending with `_ test`
- add in that file:
  - import app
    - **import 'package:my_app/main.dart';**
- run unit tests **flutter test test/unit/main_test.dart**


*Widget tests*
-------------

- does not require device connected
- add in `pubspec.yaml`
  - in `dev_dependencies:`
   - test: any
- create `test` directory
- (optional) create directory for widget tests
- create dart file ending with `_ test`
- add in that file:
  - import app
    - **import 'package:my_app/main.dart';**
- run widget tests **flutter test test/widget/main_test.dart**


*JUnit Report*
-------------

- https://pub.dev/packages/junitreport
- converts results of dart tests to JUnit xml format
- works only with unit and widget tests
- make sure these are in path:
  - `flutter/.pub-cache/bin`
  - `flutter/bin/cache/dart-sdk/bin`
- install:
  - `flutter pub global activate junitreport`
- add in **pubspec.yaml**
  - in `dev_dependencies:`
    - *junitreport: ^1.0.2*
- run tests:
  - `flutter test --machine | tojunit > TEST-all.xml`


*Integration tests*
--------------------

Flutter driver
- Application runs in separate process from the test itself
- Android (Espresso)
- IOS (Earl Grey)
- requires connected device (or simulator)
- add flutter driver to project: https://api.flutter.dev/flutter/flutter_driver/flutter_driver-library.html
- add in **pubspec.yaml**
  - in `dev_dependencies:`
   - flutter_driver:
    - sdk: flutter
- run `flutter packages upgrade`
- create `test_driver` directory
  - add `main.dart` and 'main_test.dart' (or similar)

In main.dart file, enable flutter driver with main app (add these in lib/main.dart if using appium driver)
- `import 'package:flutter_driver/driver_extension.dart';`
- `enableFlutterDriverExtension();`

In main_test.dart, use driver commands (for example)
- `import 'package:flutter_driver/flutter_driver.dart';`
- `driver = await FlutterDriver.connect();`
- `await driver.tap(buttonFinder);`

Check installation
- run `flutter doctor` it will show if everything is installed

Run test:
- **flutter drive --target=test_driver/main.dart**

Integration test results must be converted to some other format.
There is a (crude) sample how to display at least that all tests passed or some failed.
Look more info from "run-tests-*.sh files".


*screenshots*
-------------

- https://pub.dev/packages/screenshots
- screenshots for integration tests
- create `screenshots.yaml` file in project folder
  - it should contain:
    - tests: test class `test_driver/main.dart`
    - staging: screenshot folder, screenshots go here
    - devices: ios: or android: (or both) and device name
- add in main_test.dart:
  - `import 'package:screenshots/screenshots.dart';`
  - `final config = Config();`
  - `await screenshot(driver, config, 'myscreenshot');`
- add in **pubspec.yaml**
  - in `dev_dependencies:`
    - screenshots: 2.0.2+1 (or newer version)


**How to make test run in Bitbar Cloud**

Create a zip-file containing the app directory (flutter app and tests, in case of this sample “my_app” directory) and “run-tests.sh” file (use shell scripts below). Upload this .zip file to “Appium Server Side” type project in Bitbar Cloud. Also upload some dummy .apk file (or .ipa), for example bitbarSample.apk from Bitbar github samples. “Appium Server Side” type project expects an .apk (or .ipa) file to be uploaded to test run. The actual flutter app is built during test run. Note that the uploaded app needs to be able to install on real device, otherwise test will fail.
IOS simulator test run spends actual real device test time, real iOS device is idling while simulator is running tests (upload real device ipa also on simulator run, no simulator builds).

These are scripts for creating test package:
- zip-test-files-android.sh
  - creates android integration test package
- zip-test-files-android-unit-widget.sh
  - creates android unit and widget test package
- zip-test-files-ios.sh (real device)
  - check `Steps for Bitbar cloud iOS real devices test runs` before creating test package
  - creates ios integration test package
  - upload ipa not apk file with this
- zip-test-files-ios-simulator.sh
  - creates ios simulator integration test package
  - upload ipa not apk file with this
- zip-test-files-ios-simulator-install-flutter.sh
  - creates ios simulator integration test package (install flutter, some cloud VMs might not yet have Flutter installed)
  - upload ipa not apk file with this

**Steps for Bitbar cloud iOS real devices test runs**

- No guarantee that this will work because of complicated iOS app signing.
- Open "Runner.xcworkspace" in XCode from "ios" folder and set your correct app signing "Team" (Runner target) (don't build)
- Close XCode
- Run app locally with command: `flutter run`
- Run tests locally with command: `flutter drive --target=test_driver/main.dart`
- Delete "build/ios/Debug-iphoneos/" -folder (and the Runner.app inside this folder)
- Create ipa from the built app (Runner.app) from "build/ios/iphoneos/" -folder
  - To create ipa:
    - Create "Payload" folder
    - move "Runner.app" to "Payload" folder
    - Zip "Payload" folder and rename the zip as ipa (for example app.ipa)
- Shell script `create-real-device-ipa.sh` can also be used to create the ipa
- Upload this ipa to cloud and use it as the app to install on the device
- Remove the Runner.app file from "build/ios/iphoneos/" -folder
- In "run-tests-ios.sh" script, move cloud re-signed app to "build/ios/iphoneos/" -folder
- (the uploaded app (ipa) will be re-signed in cloud and it will be called application.ipa)
<pre>
# unzip re-signed app
mv application.ipa application.zip
unzip application.zip

# move app to build folder
mkdir -p my_app/build/ios/iphoneos
mv Payload/Runner.app my_app/build/ios/iphoneos/Runner.app
</pre>
- In "run-tests-ios.sh" script, run test with "--no-build" -flag: `flutter drive --no-build --target=test_driver/main.dart`
- Use "zip-test-files-ios.sh" script to create test zip file and upload it to cloud as the test file ("use to run the test")

![xcode signing Team](xcode.png)
![create ipa](ipa.png)
![files uploaded to cloud](cloud-files.png)
