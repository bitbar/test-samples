#!/bin/bash

installMaven(){
	if [ "$(uname)" == "Darwin" ]; then
		echo 'Maven is already installed on mac..'
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
		echo "Installing maven..."
		curl -O http://mirrors.sonic.net/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
		tar xvf apache-maven-3.3.9-bin.tar.gz
		export M3_HOME=/home/ubuntu/apache-maven-3.2.1
		export M3=$M3_HOME/bin
		export PATH=$M3:$PATH
		mvn -version
		echo "Maven installation complete!"
	fi
}

startAppium(){
	if [ "$(uname)" == "Darwin" ]; then
		startAppiumOSX
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
		startAppiumLinux
	else
		echo "Operating system not supported, exiting..."
		exit 1
	fi

	sleep 10
	ps -ef|grep appium
}

startAppiumOSX(){
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
    echo "Starting Appium on Mac..." 
	node /opt/appium/bin/appium.js -U ${UDID} --log-no-colors --log-timestamp --show-ios-log --screenshot-dir "${PWD}/.screenshots" >appium.log 2>&1 & 
}

startAppiumLinux(){
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
    echo "Starting Appium on Linux..."
	/opt/appium/appium/bin/appium.js --log-no-colors --log-timestamp --screenshot-dir "${PWD}/.screenshots" >appium.log 2>&1 &
}

executeTests(){
	echo "Extracting tests.zip..."
	unzip tests.zip
	if [ "$(uname)" == "Darwin" ]; then
	   	echo "Running iOs Tests..."
		mvn clean test -Dtest=IosAppiumExampleTest -DexecutionType=serverside 
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
	    echo "Running Android Tests..."
		mvn clean test -Dtest=AndroidAppiumExampleTest -DexecutionType=serverside 
	fi
	echo "Finished Running Tests!"
	cp target/surefire-reports/junitreports/TEST-*.xml TEST-all.xml
}

installMaven
startAppium
executeTests

