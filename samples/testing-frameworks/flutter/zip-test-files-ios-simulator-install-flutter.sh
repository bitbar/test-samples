#!/bin/bash

# ios version

rm ios-test-files-simulator-install-flutter.zip
rm run-tests.sh
cp run-tests-ios-simulator-install-flutter.sh run-tests.sh
zip -r ios-test-files-simulator-install-flutter run-tests.sh my_app
