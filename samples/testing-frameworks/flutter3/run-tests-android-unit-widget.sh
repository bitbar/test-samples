#!/bin/bash

# Android unit and widget tests Bitbar cloud
# Already installed versions: 1.20.4, 2.10.5, 3.3.8
VERSION=3.3.8
fvm install $VERSION
fvm global $VERSION

git --version
flutter --version

echo "Extracting tests.zip..."
unzip tests.zip

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

# unit + widget to junit format
echo "run unit and widget tests"
flutter test --machine | tojunit > TEST-all.xml

ls -la

rm TEST-fail.xml
rm TEST-pass.xml
rm TEST-all.xml.bu

cd ..
mv my_app/TEST-all.xml TEST-all.xml
