#!/bin/bash

startAppium(){
	if [ "$(uname)" == "Darwin" ]; then
		startAppiumOSX
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
		startAppiumLinux
	else
		echo "Unknown OS system, exiting..."
		exit 1
	fi
}

startAppiumOSX(){
	if [ -z ${UDID} ] ; then
		export UDID=${IOS_UDID}
	fi
		echo "UDID is ${UDID}"
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
        echo "Starting Appium on Mac..."
        export AUTOMATION_NAME="XCUITest"
	appium -U ${UDID} --log-no-colors --log-timestamp
}

startAppiumLinux(){
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
        echo "Starting Appium on Linux..."
  # Use appium-X.X, to choose a specific Appium server version. More about supported versions at
  # https://support.smartbear.com/bitbar/docs/en/mobile-app-tests/automated-testing/appium-support.html
	appium --log-no-colors --log-timestamp
}

executeTests(){
	echo "Extracting tests.zip..."
	unzip tests.zip
	if [ "$(uname)" == "Darwin" ]; then
	   	echo "Running iOS Tests..."
		mvn clean test -Dtest=IosAppiumExampleTest -DexecutionType=serverside
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
	    echo "Running Android Tests..."
		mvn clean test -Dtest=AndroidAppiumExampleTest -DexecutionType=serverside
	fi
	echo "Finished Running Tests!"
	cp target/surefire-reports/junitreports/TEST-*.xml TEST-all.xml
}

startAppium
executeTests
