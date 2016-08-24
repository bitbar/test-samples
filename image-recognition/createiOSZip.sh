#!/bin/bash
#
# Create zip ready to be uploaded to Testdroid Cloud.
#

OUT_ZIP="ios-test.zip"

cp run-tests_ios.sh run-tests.sh
if [ -f "${OUT_ZIP}" ]; then
    rm "${OUT_ZIP}"
fi
zip -r "${OUT_ZIP}" pom.xml run-tests.sh src akaze/mac akaze/LICENSE lib/testdroid-appium-driver-1.2.0.jar queryimages
