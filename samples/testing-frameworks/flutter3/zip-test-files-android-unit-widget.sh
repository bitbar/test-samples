#!/bin/bash

# android version

rm android-test-files-unit-widget.zip
rm run-tests.sh
cp run-tests-android-unit-widget.sh run-tests.sh
zip -r android-test-files-unit-widget run-tests.sh my_app
