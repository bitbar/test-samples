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
- **flutter drive --target=test_driver/main_test.dart**

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

Create a zip-file containing app directory (app and tests, in my case “my_app” directory) and “run-tests.sh” file (use shell scripts below). Upload this .zip file to “Appium Server Side” type project in Bitbar Cloud. Also upload some .apk file (or .ipa), for example bitbarSample.apk. “Appium Server Side” type project expects an .apk file to be uploaded to test run.

Note: iOS real device test run is not guaranteed to work in cloud.

These are scripts for creating test package:
- zip-test-files-android.sh
  - creates android integration test package
- zip-test-files-android-unit-widget.sh
  - creates android unit and widget test package
- zip-test-files-ios.sh
  - creates ios integration test package
  - upload ipa not apk file with this
- zip-test-files-ios-simple.sh
  - creates ios integration test package (install flutter, no test result stuff)
  - upload ipa not apk file with this
- zip-test-files-ios-simulator.sh
  - creates ios simulator integration test package
  - upload ipa not apk file with this
- zip-test-files-ios-simulator-install-flutter.sh
  - creates ios simulator integration test package (install flutter)
  - upload ipa not apk file with this
