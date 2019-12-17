#!/bin/bash

# integration tests

echo "Extracting tests.zip..."
unzip tests.zip

echo "download and install flutter"
wget -q https://storage.googleapis.com/flutter_infra/releases/stable/macos/flutter_macos_v1.12.13+hotfix.5-stable.zip
unzip -qq flutter_macos_v1.12.13+hotfix.5-stable.zip

export PATH="$PATH:`pwd`/flutter/bin"

echo "install cocoapods"
sudo gem install cocoapods
pod setup

echo "run flutter doctor"
flutter doctor

cd my_app

echo "run integration tests "
flutter drive -v --target=test_driver/main.dart

rm TEST-fail.xml
rm TEST-pass.xml

cat /Users/testdroid/test/my_app/flutter_01.log

echo "screenshots stuff"
cd ..
mkdir -p screenshots
ls -la /tmp/screenshots/test
mv /tmp/screenshots/test/ screenshots
