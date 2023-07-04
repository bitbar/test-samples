#!/bin/bash
#
# Create zip ready to be uploaded to BitBar Cloud.
#

OUT_ZIP="android-test.zip"

cp run-tests_android.sh run-tests.sh
if [ -f "${OUT_ZIP}" ]; then
    rm "${OUT_ZIP}"
fi
zip -rq "${OUT_ZIP}" pom.xml run-tests.sh src
echo "${OUT_ZIP}"
