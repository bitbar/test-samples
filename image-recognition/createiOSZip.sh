#!/bin/bash
cp run-tests_ios.sh run-tests.sh
rm ios-test.zip
zip -r ios-test.zip pom.xml run-tests.sh src akaze/mac akaze/LICENSE lib/testdroid-appium-driver-1.2.0.jar queryimages
