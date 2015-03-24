Easy Game testing using Image Recognition sample
=============================

Sample Java Project to show use of OpenCV and Akaze algorithm to run image recognition tests using Appium.
Sample application can be found from https://github.com/bitbar/testdroid-samples/blob/master/apps/builds/Testdroid.apk


Dependencies
============

- Appium 1.0
- Android SDK 4.2 or newer
- Java JDK 7 or newer
- Apache Maven
- Akaze algorithm
- OpenCV 2.4.9
- testdroid-appium-driver from https://github.com/bitbar/testdroid-appium-driver.gi


Info on OpencCV and Akaze
=========================

The Akaze algorithm (https://github.com/pablofdezalc/akaze) is used to find the matching keypoints from two images and save them to a json file. 
The opencv (http://opencv.org/) java bindings to process the json file with keypoints and find the homography of the wanted image in a scene (screenshot).

The AkazeImageFinder.java class will run "akaze_match", then use the keypoints from json to try to identify a given image inside a scene and return the corners of the image found. In this case, it looks for query images from "queryimages" folder and try to match them on a screenshot taken from device. 


Installing dependencies
=======================

1. OpenCV
The OpenCV java libs can be found from project_root/opencv-2.4.9. To install them in maven, run:
    mvn install:install-file -Dfile=opencv/opencv-249.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.9 -Dpackaging=jar 

2. Akaze
The Akaze C++ implementation can be found and built from: https://github.com/bitbar/testdroid-image-recognition.
The project also contains the built scripts which can be found from project_root/akaze/bin/akaze_match.
No installation required for this project.

3. testdroid-appium-driver
Can be pulled from https://github.com/bitbar/testdroid-appium-driver.git
To install it, run:
mvn install -DskipTests

All other dependencies are fetched by Maven


Building and installing OpenCV (if want/need)
=================================================
To install OpenCV on a mac, use MacPorts:

sudo port install opencv @2.4.9

To build OpenCV locally (if MacPorts install doesn't work):

1. Download OpenCV 2.4.9 http://opencv.org/downloads.html
2. cd opencv-2.4.9
3. mkdir build
4. cd build/
5. cmake ..
6. make
7. make install


How to build and run Hobbit Kabam Sample
=======================================================

**Run from command line with Maven**

Change testdroid.properties file in the project root folder and DriverHandler.java file to contain:

**Appium from testdroid cloud**
- set isLocalhost = false in DriverHandler.java
- testdroid.username=<ADD USERNAME HERE>
- testdroid.password=<ADD PASSWORD HERE>

**Appium localhost**
- set isLocalhost = true in DriverHandler.java

**Build and Run the tests from maven using**

Run all tests: 

mvn clean test

Run individual test:

mvn clean test -Dtest=BugInvadersTest

**Reports**
- Screenshots for each steps are stored at project_root/screenshot locally
- Screenshots for image matching are stored at project_root/target/reports/deviceName/screenshots locally
- The reports, json file and everything else can be found from <project root>/target/reports/deviceName/
