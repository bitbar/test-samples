#!/bin/bash

#
# Copyright(C) 2019 Bitbar Technologies Oy
#
# NOTE: contributions are welcome
#

TEST_FILE="bitbar-sample-test.zip"

echo "Creating test file"
zip -r "${TEST_FILE}" requirements.txt *py run-tests.sh
echo "You should now upload test file '${TEST_FILE}' to Bitbar Cloud"
