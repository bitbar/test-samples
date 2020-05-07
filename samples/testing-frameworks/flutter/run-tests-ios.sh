#!/bin/bash

# ios real device integration tests Bitbar cloud
# https://github.com/bitbar/test-samples/tree/master/samples/testing-frameworks/flutter

export LANG="en_US.UTF-8"

FLUTTER_PATH="/opt/flutter/bin"
PUB_CACHE_BIN="/opt/flutter/.pub-cache/bin"
DART_PATH="/opt/flutter/bin/cache/dart-sdk/bin"
export PATH=$PATH:$FLUTTER_PATH:$PUB_CACHE_BIN:$DART_PATH

echo "Extracting tests.zip..."
unzip tests.zip

echo "install cocoapods"
sudo gem install cocoapods
pod setup

echo "run flutter doctor"
flutter doctor -v

# unzip re-signed app (will be called application.ipa after re-sign process)
mv application.ipa application.zip
unzip application.zip

mv Payload/Runner.app Runner.app

# make sure build folder exists
mkdir -p my_app/build/ios/iphoneos

# move app to build folder
mv Runner.app my_app/build/ios/iphoneos/Runner.app

cd my_app

echo "run integration tests"
flutter drive -v --no-build --target=test_driver/main.dart > testconsole.log
scriptExitStatus=$?

while read line; do
  echo "$line"
  case "$line" in
 *setUpAll*)
    TEST_CLASS=$line
    ;;
 *)
    ;;
  esac
  case "'$line'" in
 *All*tests*passed*)
    TESTS_PASSED=true
    ;;
 *)
    ;;
  esac
  case "'$line'" in
 *Some*tests*failed*)
    TESTS_PASSED=false
    ;;
 *)
    ;;
  esac
done <testconsole.log

# parse results to TEST-all.xml
CLASS_NAME_TEST=`echo ${TEST_CLASS}  | awk -F"0m:" '/0m/{print $2}' | sed 's/[][]//g' | sed 's/(setUpAll)0m//'`

if [ "$TESTS_PASSED" = "true"  ]
then
    sed -i.bu "s/default-testsuitename/$CLASS_NAME_TEST/"       TEST-pass.xml
    sed -i.bu "s/default-testcase-class/$CLASS_NAME_TEST/"       TEST-pass.xml
    sed -i.bu "s/default-testcase-1/All tests passed/"       TEST-pass.xml
    sed -i.bu "s/default-testcase-result/All tests passed/"       TEST-pass.xml
    mv TEST-pass.xml TEST-all.xml
else
    sed -i.bu "s/default-testsuitename/$CLASS_NAME_TEST/"       TEST-fail.xml
    sed -i.bu "s/default-testcase-class/$CLASS_NAME_TEST/"       TEST-fail.xml
    sed -i.bu "s/default-testcase-1/Some tests failed/"       TEST-fail.xml
    sed -i.bu "s/default-testcase-result/Some tests failed/"       TEST-fail.xml
    for i in {1..4}
    do
       sed -i.bu "s/0/1/"       TEST-fail.xml
    done
    mv TEST-fail.xml TEST-all.xml
fi

rm TEST-fail.xml
rm TEST-pass.xml
rm TEST-all.xml.bu

cat /Users/testdroid/test/my_app/flutter_01.log

echo "screenshots stuff"
cd ..
mkdir -p screenshots
ls -la /tmp/screenshots/test
mv /tmp/screenshots/test/ screenshots
mv my_app/TEST-all.xml TEST-all.xml

exit $scriptExitStatus
