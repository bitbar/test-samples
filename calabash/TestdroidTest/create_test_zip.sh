#!/bin/bash
#
# Create test zip file which can be uploaded to Testdroid Cloud.
#
# Author: Jarno Tuovinen <jarno.tuovinen@bitbar.com>
#

TEST_ZIP=TestdroidTests.zip 

if [ -d "${TEST_ZIP}" ]; then
    echo "Delete old ${TEST_ZIP}"
    rm "${TEST_ZIP}"
fi

echo "Create ${TEST_ZIP}"
zip -r "${TEST_ZIP}" features/ config/

