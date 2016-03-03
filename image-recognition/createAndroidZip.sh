#!/bin/bash

cp run-tests_android.sh run-tests.sh
rm android-test.zip
zip -r android-test.zip pom.xml run-tests.sh src akaze/linux akaze/LICENSE lib/testdroid-appium-driver-1.2.0.jar queryimages
