#!/bin/bash
#
# Create zip ready to be uploaded to Testdroid Cloud.
#

OUT_ZIP="android-test.zip"

cp run-tests_android.sh run-tests.sh
if [ -f "${OUT_ZIP}" ]; then
    rm "${OUT_ZIP}"
fi
zip -rq "${OUT_ZIP}" run-tests.sh BitbarSampleAppTest.py TestdroidAppiumTest.py requirements.txt
echo "${OUT_ZIP}"
