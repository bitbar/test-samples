#!/bin/bash

# Android real device integration tests Bitbar cloud

git --version
flutter --version
echo $ANDROID_HOME
android list target

echo "Extracting tests.zip..."
unzip tests.zip

echo "current folder"
pwd

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

flutter drive --target=test_driver/main.dart > testconsole.log

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
mkdir -p screenshots
ls -la /tmp/screenshots/test
mv /tmp/screenshots/test/ screenshots
mv my_app/TEST-all.xml TEST-all.xml
