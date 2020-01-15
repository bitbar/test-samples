#!/bin/bash

# ios real device integration tests Bitbar cloud

echo "Extracting tests.zip..."
unzip tests.zip

echo "check if flutter is installed"
flutter doctor

# in case flutter is not installed

#echo "download and install flutter"
#wget -q https://storage.googleapis.com/flutter_infra/releases/stable/macos/flutter_macos_v1.12.13+hotfix.5-stable.zip
#unzip -qq flutter_macos_v1.12.13+hotfix.5-stable.zip

#export PATH="$PATH:`pwd`/flutter/bin"

echo "install cocoapods"
sudo gem install cocoapods
pod setup

echo "run flutter doctor"
flutter doctor

# unzip resigned app
mv application.ipa application.zip
unzip application.zip

mv Payload/Runner.app Runner.app

# move app to build folder
#mkdir -p my_app/build/ios/iphoneos
mv Runner.app my_app/build/ios/iphoneos/Runner.app

cd my_app

# don't clean project in iOS real device test run

echo "run integration tests"
#flutter drive -v --target=test_driver/main.dart > testconsole.log
flutter drive -v --no-build --target=test_driver/main.dart > testconsole.log

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
