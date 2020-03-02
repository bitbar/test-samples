#!/bin/bash

# Android unit and widget tests Bitbar cloud

git --version
flutter --version
echo $ANDROID_HOME
android list target

echo "Extracting tests.zip..."
unzip tests.zip

FLUTTER_PATH="/opt/testdroid/flutter/bin"
PUB_CACHE_BIN="/opt/testdroid/flutter/.pub-cache/bin"
DART_PATH="/opt/testdroid/flutter/bin/cache/dart-sdk/bin"
export PATH=$PATH:$FLUTTER_PATH:$PUB_CACHE_BIN:$DART_PATH

echo "accept sdk licences"
yes | sdkmanager --licenses
yes | flutter doctor --android-licenses

echo "run flutter doctor"
flutter doctor
cd my_app

echo "clean project folder"
flutter clean
rm -r .packages
rm pubspec.lock

echo "install junit report"
flutter pub global activate junitreport
tojunit --help

echo "devices"
flutter devices

# unit tests (run this to get id of)
# FormatException: Unexpected character (at character 1) error
flutter test test/unit/main_test.dart

# widget tests
#flutter test test/widget/main_test.dart

# unit + widget to junit format
echo "run unit and widget tests"
flutter test --machine | tojunit > TEST-all.xml

ls -la

rm TEST-fail.xml
rm TEST-pass.xml
rm TEST-all.xml.bu

cd ..
mv my_app/TEST-all.xml TEST-all.xml
