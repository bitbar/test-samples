#!/bin/bash
#
# Create zip ready to be uploaded to Testdroid Cloud.
#

OUT_ZIP="android-test.zip"

cp run-tests_android.sh run-tests.sh
if [ -f "${OUT_ZIP}" ]; then
    rm "${OUT_ZIP}"
fi
zip -r "${OUT_ZIP}" --exclude=lib/linux/opencv/java8/* pom.xml run-tests.sh src lib/linux queryimages
