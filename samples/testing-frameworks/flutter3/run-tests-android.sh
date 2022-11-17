#!/bin/bash

# Android real device integration tests Bitbar cloud
# Already installed versions: 1.20.4, 2.10.5, 3.3.8
VERSION=3.3.8
fvm install $VERSION
fvm global $VERSION

git --version
flutter --version

echo "Extracting tests.zip..."
unzip tests.zip

echo "accept sdk licences"
yes | sdkmanager --licenses
yes | flutter doctor --android-licenses

echo "run flutter doctor"
flutter doctor
cd my_app

echo "clean project folder"
flutter clean
rm -r .packages
rm pubspec.lock

echo "devices"
flutter devices

ls -la

echo "run integration tests"

flutter drive --driver=test_driver/integration_test.dart --target=integration_test/main_test.dart > testconsole.log

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

echo "screenshots stuff"
cd ..
rm -rf screenshots
mv my_app/screenshots screenshots
mv my_app/TEST-all.xml TEST-all.xml
