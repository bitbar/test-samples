#!/bin/bash

#
# Copyright(C) 2024 Bitbar Technologies Oy
#
# NOTE: contributions are welcome
#

TEST_FILE="bitbar-sample-cypress-test.zip"

echo "Creating test file"
zip -r "${TEST_FILE}" app/ cypress/ scripts/ run-test* *.json *.js
echo "You should now upload test file '${TEST_FILE}' to Bitbar Cloud"
