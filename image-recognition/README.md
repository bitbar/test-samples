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

- testdroid-appium-driver - can be downloaded from
  [https://github.com/bitbar/testdroid-appium-driver.git](https://github.com/bitbar/testdroid-appium-driver.git)

- [Testdroid Akaze](https://github.com/aknackiron/akaze) - fork of official Akaze repo with added json support.

- [OpenCV](http://opencv.org/)

- [jsoncpp](https://github.com/open-source-parsers/jsoncpp.git) - used to add json support to the Akaze library. This is needed if compiling latest versions of Akaze and OpenCV.

# Info on OpenCV and Akaze

We use [Akaze algorithm](https://github.com/pablofdezalc/akaze) to find
the matching keypoints from two images and save them to a json file.
We use the [OpenCV](http://opencv.org/) java bindings to process the
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


## Installing dependencies

1. OpenCV

   The OpenCV java libs can be found under *./opencv/* folder
   inside the project. To install them in maven, run:

         mvn install:install-file -Dfile=opencv/opencv-249.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.9 -Dpackaging=jar

1. Akaze

   The Akaze C++ implementation can be found and built from:
   [Testdroid Akaze fork](https://github.com/bitbar/akaze) The current
   project also contains the *./akaze/* folder with built binaries
   found under *./akaze/bin/akaze_match*.

   **Note** these are Mac specific binaries and work only on Mac.

1. testdroid-appium-driver v1.1.3

   Can be pulled from
   [https://github.com/bitbar/testdroid-appium-driver.git](https://github.com/bitbar/testdroid-appium-driver.git). To install the version v1.1.3, which this sample uses, first clone and checkout correct version:
   
         git clone https://github.com/bitbar/testdroid-appium-driver.git
         git checkout v1.1.3

   To install the plugin, run:

         mvn install -DskipTests

   Everything else is fetched by Maven


## Run Test from Command Line with Maven

Create a testdroid.properties file in the project root folder, containing this info:

    testdroid.username=<ADD USERNAME HERE>
    testdroid.password=<ADD PASSWORD HERE>
    appium.automationName=Appium
    testdroid.project=Sample Project
    appium.appFile=src/test/resources/BitbarSampleApp.apk

Note, quotes are not used around *username* and *password*. If
a different (newer) version of OpenCV is used, OpenCV version number needs to be updated.

Run the tests from maven using:

    mvn test

If OpenCV is not installed through 'brew' the 'java.library.path' might need to be provided on the command line:

    mvn -Djava.library.path=/usr/local/share/OpenCV/java/ test


**Cloud Device**

Set the parameters in the testdroid.properties file, then run:

    mvn  -Dtestdroid.device="testdroid device name"  -Djava.library.path=<java-lib-path> test  

where java-lib-path is the directory where opencv library can be found
(for example: */usr/local/Cellar/opencv/2.4.9/share/OpenCV/java* if
you used brew to install).


**Reports**

The reports, screenshots and everything else will be found under:
*./target/reports/deviceName/*


## Building Latest Versions of Libraries

This guide has been tested to work on Mac Yosemite. Same steps should
also work as such also on major Linux distros.

First we'll need to compile up to date version of jsoncpp (needed by
Akaze later on). At least on Mac, jsoncpp installed through brew gave
linking errors while linking Akaze.

1. [jsoncpp](https://github.com/open-source-parsers/jsoncpp)

   Python 2.6 is needed to extract amalgamated source and header files:

   ```
   git clone https://github.com/open-source-parsers/jsoncpp.git
   cd jsoncpp
   python amalgamate.py  
   ```

   By default, the following files are generated and are needed to compile new Akaze binaries.

   * `dist/jsoncpp.cpp`: source file that needs to be added to your project.
   * `dist/json/json.h`: corresponding header file for use in your project. It is equivalent to including json/json.h in non-amalgamated source. This header only depends on standard headers.
   * `dist/json/json-forwards.h`: header that provides forward declaration of all JsonCpp types.

   You will also need to build and copy the libjsoncpp.a library to your local lib directory:
   ```
   mkdir build
   cd build
   cmake ..
   make
   cp src/lib_json/libjsoncpp.a /usr/local/lib/
   ```


1. [OpenCV](https://github.com/Itseez/opencv)

   ```
   git clone https://github.com/Itseez/opencv.git  
   cd opencv
   git checkout 2.4  
   mkdir build  
   cd build/  
   cmake -G "Unix Makefiles" ..  
   make -j8
   make install  
   ```

   Creates Java jars for image recognition sample. Latest jars are installed by running:

         mvn install:install-file -Dfile=path/to/opencv/build/bin/opencv-2412.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.12 -Dpackaging=jar  

   *pom.xml* file needs to be updated with installed OpenCV version (2.4.12 in this example).

1. [Akaze](https://github.com/aknackiron/akaze)

   ```
   git clone https://github.com/aknackiron/akaze.git
   cd akaze
   # copy previously built versions of jsoncpp to 'src/lib/jsoncpp/', 'src/lib/json/' and src/lib/jsoncpp.cpp
   cp -r path/to/jsoncpp/dist/json ./src/lib/
   cp -r path/to/jsoncpp/dist/jsoncpp.cpp ./src/lib/
   mkdir build
   cd build
   cmake ..  
   make
   cp bin/akaze_* path/to/image-recognition/akaze/bin/  
   ```

Up to date versions of used libraries are now installed and the sample test can be run using them from the image-recognition sample directory:

    mvn -Djava.library.path=/usr/local/share/OpenCV/java/ test
