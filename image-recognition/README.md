# Java Image Recognition Sample

This out of the box sample Java Project shows the use of OpenCV and
Akaze algorithm to run image recognition tests using Appium in
[Testdroid cloud](http://testdroid.com/). This is particularly suited
for running image recognition tests.

The project will use the BitbarSampleApp.apk, will install it on the
device, then check the Bitbar logo is displayed on the screen using
image recognition.  The reference Bitbar logo image is located at:
*queryimages/bitbar_logo.png*

The project uses:

- [Testdroid](http://testdroid.com) - web service for running tests on
  real mobile devices. You'll need to have an active account for running this test.

- testdroid-appium-driver from
  [https://github.com/bitbar/testdroid-appium-driver.git](https://github.com/bitbar/testdroid-appium-driver.git)

- [https://github.com/bitbar/testdroid-image-recognition](https://github.com/bitbar/testdroid-image-recognition) - this project
  contains the C++ implementation for Akaze algorithm

- [OpenCV](http://opencv.org/)


# Info on OpenCV and Akaze

We use [Akaze algorithm](https://github.com/pablofdezalc/akaze) to find
the matching keypoints from two images and save them to a json file.
We use the [opencv](http://opencv.org/) java bindings to process the
json file with keypoints and find the homography of the wanted image
in a scene (screenshot).

The AkazeImageFinder.java class will run `akaze_match`, then use the
keypoints from json to try to identify a given image inside a scene
and return the corners of the image found. In our case, we look for
specific images from *./queryimages/* folder and try to find them on a
screenshot taken from device.

Once the image is located, we can:

- tap it
- drag it
- swipe the screen with it
- etc

All these methods are implemented in the
TestdroidImageRecognition.java class. This class has different
functions for working with the images found on the screen and for
looking for them. These are then used inside the test classes for the
game tests.

Some of the functions:

- `public Point[] findImage(String image, String scene)`: returns the
  corners of the image if found, or `null` if no image was found
  
- `public Point[] findImageOnScreen(String image)`: takes a
  screenshots, then tries to find the image on it and return the
  corners

- `public void tapImageOnScreen(String image)`: takes a screenshot,
  tries to find the image on the screen, then tap the middle of it

- `public void swipeScreenWithImage(String image, int repeats)`: takes
  a screenshot, finds the image on the screen, taps and holds it, then
  swipes the screen while still holding it

- `public void dragImage(String image, double x_offset, double
  y_offset)`: takes a screenshot, tries to find the image, taps it and
  drags it to a certain position given as x and y offsets from middle
  of the screen


# Installing dependencies


1. OpenCV

   The OpenCV java libs can be found under *./opencv/* folder
   inside the project. To install them in maven, run:

           mvn install:install-file -Dfile=opencv/opencv-249.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.9 -Dpackaging=jar 

2. Akaze

   The Akaze C++ implementation can be found and built from:
   [https://github.com/bitbar/testdroid-image-recognition](https://github.com/bitbar/testdroid-image-recognition)
   The current project also contains the *./akaze/* folder with built
   binaries found under *./akaze/bin/akaze_match*. Note these are Mac
   binaries and work only on Mac.

3. testdroid-appium-driver

   Can be pulled from
   [https://github.com/bitbar/testdroid-appium-driver.git](https://github.com/bitbar/testdroid-appium-driver.git). To install it, run:

           mvn install -DskipTests

Everything else is fetched by Maven


## Building and installing OpenCV 


To install OpenCV on a mac, use MacPorts:

    sudo port install opencv @2.4.9

Or use Homebrew:

    brew install homebrew/science/opencv --with-java  
    mvn install:install-file -Dfile=/usr/local/Cellar/opencv/2.4.9/share/OpenCV/java/opencv-249.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.9 -Dpackaging=jar


If you have the latest brew version you probably need to pull 2.4.9
version manually. Go to dir where formulas are installed:

    cd /usr/local/Library/Taps/homebrew/homebrew-science


Checkout version 2.4.9

    git checkout 05ab591 opencv.rb

unlink older version:

    brew unlink opencv

and finally install 2.4.9

    brew install opencv

To build OpenCV locally (if MacPorts install or Homebrew doesn't work):

1. Download OpenCV 2.4.9 http://opencv.org/downloads.html
1. cd opencv-2.4.9
1. mkdir build
1. cd build/
1. cmake -G "Unix Makefiles" ..
1. make -j8


Once you have the opencv jar file (you can use the one in this repo
under opencv directory), install it to maven using:

    mvn install:install-file -Dfile=opencv/opencv-249.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.9 -Dpackaging=jar 


## Build and run the Java Image Recognition Sample


**Run from command line with Maven**

Create a testdroid.properties file in the project root folder, containing this info:

    testdroid.username=<ADD USERNAME HERE>
    testdroid.password=<ADD PASSWORD HERE>
    appium.automationName=Appium
    testdroid.project=Sample Project
    appium.appFile=src/test/resources/BitbarSampleApp.apk

Note, you shouldn't use quotes around username and password.

Run the tests from maven using:

    mvn test

**Cloud device**

Set the parameters in the testdroid.properties file, then run:

    mvn  -Dtestdroid.device="testdroid device name"  -Djava.library.path=<java-lib-path> test  

where java-lib-path is the directory where opencv 2.4.9 library can be
found (for example: */usr/local/Cellar/opencv/2.4.9/share/OpenCV/java*
if you used brew to install).


**Reports**

The reports, screenshots and everything else will be found under:
*./target/reports/deviceName/*

## Additional info

Note that the binaries under *./akaze/bin/* are Mac binaries and
cannot be used under other operating systems.

