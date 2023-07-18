#!/bin/bash

#
# Copyright(C) 2023 SmartBear Software
#
# NOTE: contributions are welcome
#
# __author__ = 'Niko Cankar <niko.cankar@bitbar.com>'


# Print how script is used and exit
function print_help_and_die() {
    echo "Usage: $0 <ios|android>"
    exit 0
}

if [[ "$#" -eq 1 ]]; then
    ENV="$1"
else
    print_help_and_die
fi

TEST_FILE="test-package_${ENV}.zip"

echo "Creating test file for environment: ${ENV}"
cp run-tests_${ENV}.sh run-tests.sh && zip -r "${TEST_FILE}" requirements.txt *py run-tests.sh
echo "
You should now upload test file '${TEST_FILE}' to BitBar Cloud"
