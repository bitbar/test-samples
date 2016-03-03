# Java Image Recognition Sample

This out of the box sample Java Project shows the use of OpenCV and
Akaze algorithm to run image recognition tests using Appium in
[Testdroid cloud](http://testdroid.com/). This is particularly suited
for running image recognition tests. This project can be used for local, Client
side and also Server side Appium testing. The main difference to each
is to modify testdroid.properties to fit the test type and launching the
project with correct mvn command.

The project will use the Bitbar Sample App, will install it on the
device, then check the Bitbar logo is displayed on the screen using
image recognition.  The reference Bitbar logo image is located at:
*queryimages/bitbar_logo.png*

The project uses:

- [Testdroid](http://testdroid.com) - web service for running tests on
  real mobile devices. You'll need to have an active account for running this test.

- testdroid-appium-driver - can be downloaded from
  [https://github.com/bitbar/testdroid-appium-driver.git](https://github.com/bitbar/testdroid-appium-driver.git)

- [Testdroid Akaze](https://github.com/bitbar/akaze) - fork of official Akaze repo with added json support.

- [OpenCV](http://opencv.org/)

- [jsoncpp](https://github.com/open-source-parsers/jsoncpp.git) -
used to add json support to the Akaze library. This is needed if compiling latest versions of Akaze and OpenCV.

- [Maven](https://maven.apache.org/) - Compiles and launches the test code.

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

   The OpenCV java libs can be found under *./lib/opencv/* folder
   inside the project. To install them in maven, run:

       mvn install:install-file -Dfile=lib/opencv/opencv-249.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.9 -Dpackaging=jar

   If a different (newer) version of OpenCV is used, OpenCV version number
   needs to also be updated in pom.xml. Note that Testdroid Cloud has currently
   version 2.4.9 pre-installed for Server side execution.

   To install OpenCV using *brew*:

         brew install opencv --with-java

1. Akaze

   The Akaze C++ implementation can be found and built from:
   [Testdroid Akaze fork](https://github.com/bitbar/akaze) The current
   project also contains the *./akaze/* folder with pre-built binaries for
   Linux, Mac and Windows. The sample uses only the akaze_match binary currently.

3. testdroid-appium-driver 1.2.0

   Pre-built jar can be found at ./lib/testdroid-appium-driver-1.2.0.jar and it
   is set to be used in the pom.xml

   The library can also be pulled from
   [https://github.com/bitbar/testdroid-appium-driver.git](https://github.com/bitbar/testdroid-appium-driver.git).
       git clone https://github.com/bitbar/testdroid-appium-driver.git

   To install the library after cloning, run:

         mvn install -DskipTests

   Everything else is fetched by Maven.


## Run Test from Command Line with Maven

Copy the testdroid.properties.sample.properties file into a testdroid.properties file in
the project root directory, uncommenting the lines depending on the type of
test you'll be running (Local or Client side).
Run the tests with maven using:

    mvn -Dtest=<TestClass> test

If OpenCV is not installed through 'brew' the 'java.library.path' might need to be provided on the command line:

    mvn -Djava.library.path=<java-lib-path> -Dtest=<TestClass> test

where java-lib-path is the directory where opencv library can be found
(for example: */usr/local/Cellar/opencv/2.4.9/share/OpenCV/java* if
you used brew to install).

For Client side execution, you'll need to edit in your Testdroid service info:

    testdroid.username=<ADD USERNAME HERE>
    testdroid.password=<ADD PASSWORD HERE>
    testdroid.project=<ADD PROJECT NAME HERE>

All the possible testdroid.properties variables can be found from the testdroid-appium-driver's [TestdroidAppiumClient.java](https://github.com/bitbar/testdroid-appium-driver/blob/master/src/main/java/com/testdroid/appium/TestdroidAppiumClient.java)

Note, quotes are not used around the testdroid.properties values.


**Choosing Cloud Device**

Set the testdroid.device parameter either in the testdroid.properties file or in the mvn command:

    mvn  -Dtestdroid.device="testdroid device name" -Dtest=<TestClass> test

Testdroid device names can be found from the cloud portal's [Device Groups page](https://cloud.testdroid.com/#service/devicegroups).

**Reports**

The reports, screenshots and everything else will be found under:
*./target/reports/deviceName/*

## Run Test as Server Side Appium Test

*NOTE!* Server side Appium test runs on Testdroid Cloud are available only
with BUSINESS plan. Please contact sales@bitbar.com for more information.

You will need a Testdroid project of type CALABASH ANDROID or CALABASH IOS,
which we have pre-configured for you to act as a Server side Appium project.

Once you have your Testdroid project available, use the provided scripts
to create the test zip from your project:

    ./createAndroidSample.sh
or

    ./createiOSSample.sh

Now that you have
your test zip and application file (this sample uses the Bitbar Sample Apps
available in this repository at [apps/builds](https://github.com/bitbar/testdroid-samples/tree/master/apps/builds)),
you're ready to begin creating a testrun in your project at
https://cloud.testdroid.com. Upload the app and test zip in the appropriate
pages, choose the device group and finally make sure you have set high enough
timeout for your test in the Advanced options (default is 10 minutes).

If you change the name of your Android or iOS test class, you will need to update
it to the run-tests_android.sh and run-tests_ios.sh TEST and JUNIT variables as appropriate:

    # Name of the desired test suite and optionally specific test case, eg: AndroidSample#mainPageTest
    TEST=${TEST:="AndroidSample#mainPageTest"}
    # JUnit file wont have the #caseName ending
    JUNIT="AndroidSample"

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

1. [Akaze](https://github.com/bitbar/akaze.git)

   ```
   git clone https://github.com/bitbar/akaze.git
   cd akaze
   # copy previously built versions of jsoncpp to 'src/lib/jsoncpp/', 'src/lib/json/' and src/lib/jsoncpp.cpp
   mkdir ./src/lib/jsoncpp/
   cp -r path/to/jsoncpp/dist/json ./src/lib/jsoncpp/
   cp -r path/to/jsoncpp/dist/jsoncpp.cpp ./src/lib/
   mkdir build
   cd build
   cmake ..  
   make
   cp bin/akaze_* path/to/image-recognition/akaze/bin/  
   ```

Up to date versions of used libraries are now installed and the sample test can be run using them from the image-recognition sample directory:

    mvn -Djava.library.path=/usr/local/share/OpenCV/java/ -Dtest=<TestClass> test
