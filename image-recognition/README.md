# Java Image Recognition Sample

This Java project shows how to create Akaze image recognition tests together with Appium mobile automation framework for use in [Bitbar Testing](http://bitbar.com/testing) mobile device cloud. This project is particularly suited for automated tests on mobile apps that don't have platform specific native elements.

The project uses the Bitbar Sample App, will install it on the device, then check that the Bitbar logo is displayed on the screen using image recognition. The reference Bitbar logo image is located at: *queryimages/bitbar_logo.png*

The project uses:

- [mobile-opencv-image-recognition-library](https://github.com/bitbar/opencv-library) - Library that handles OpenCV related logic

- [Bitbar Testing](http://bitbar.com/testing) - Web service for automated testing on
  real mobile devices. You'll need to have an active account for launching the tests in cloud.

- [OpenCV](http://opencv.org/) - Open Source Computer Vision

- [A-KAZE Features](https://github.com/bitbar/akaze) - Image recognition programs using OpenCV.

- [Maven](https://maven.apache.org/) - Compiles and launches the test code.

# Info on OpenCV and Akaze

We use the akaze-match program from [A-KAZE Features](https://github.com/pablofdezalc/akaze) to find the matching keypoints from two images and save them to a json file. [OpenCV](http://opencv.org/) java bindings are used for processing the json file with keypoints and finding the homography of the wanted image in a scene (screenshot).

The AkazeImageFinder.java class found within the [mobile-opencv-image-recognition-library](https://github.com/bitbar/opencv-library) will run `akaze_match`, then use the keypoints from json to try to identify a given image inside a scene and return the corners of the image found. In our case, we look for specific images from *./queryimages/* folder and try to find them on a screenshot taken from device.

Once the image is located, we can:

- tap it
- drag it
- swipe the screen with it
- etc

All image recognition related methods are implemented in the TestdroidImageRecognition.java class. The class has different functions for first finding the coordinates of the image and then injecting gestures on the device screen. Appium mobile automation framework is used for the automated gestures.

## Installing dependencies

1. A-KAZE Features

   The A-KAZE C++ implementation can optionally be found and built from: [Bitbar Akaze fork](https://github.com/bitbar/akaze). This project already contains the *./lib/\<platform\>/akaze/* folder with pre-built binaries for Linux, OS X and Windows. Only the akaze_match binary is currently in use.


All other dependencies are fetched by Maven automatically.


## Run tests locally from Command Line with Maven

The project uses Bitbar Sample apps, which are available in this repository at [apps/builds](https://github.com/bitbar/testdroid-samples/tree/master/apps/builds). By default, the tested application is assumed to be found at the root of the project with name `application.apk` or `application.ipa` depending on the tested platform. The BitbarIOSSample.ipa application file will also need to be first re-signed for your own test devices, if you'd like to use try it out locally.

You'll also need to have an Appium server launched. For iOS, the UDID of the device should be pre-set in the server settings.

Run the tests with maven using:

    mvn -Dtest=<TestClass> test

For example, to run only the test `mainPageTest` from test class AndroidSample.java you would use:

    mvn -Dtest=AndroidSample#mainPageTest test

To first clean all the previous test result files, add keyword `clean` to the command:

    mvn -Dtest=AndroidSample#mainPageTest clean test

You can also use an IDE to launch the tests. Make sure the project is correctly imported as a Maven project and that the `pom.xml` file has been discovered for Maven's dependency management.

**Reports**

The reports, screenshots and everything else will be found under:
*./target/reports/*

## Run tests as Server Side Appium in [Bitbar Testing](http://bitbar.com/testing)

You will need a Bitbar Testing cloud project of type `Appium Android server side` or `Appium iOS server side` according to the target platform.

Once you have your cloud project created, use the following scripts to create the test zip from your project:

    ./createAndroidZip.sh
or

    ./createiOSZip.sh

Now that you have your test zip and the application file (this sample uses the Bitbar Sample Apps available at [apps/builds](https://github.com/bitbar/testdroid-samples/tree/master/apps/builds), you're ready to create a testrun in your project at the [test cloud](https://cloud.bitbar.com). Upload the app and test zip in the appropriate pages, choose the device group and finally make sure you have set high enough timeout for your tests in the Advanced options (default is 10 minutes).

If you change the name of your Android or iOS test class, you will need to update it to the run-tests_android.sh and run-tests_ios.sh TEST and TEST_CASE variables as appropriate:

    # Name of the desired test class and optionally specific test case, eg: AndroidSample#mainPageTest
    TEST=${TEST:="AndroidSample"}
    # OPTIONAL: add the name of TestCases to be used with the `mvn test` command
    # Leave blank to test the whole class!
    TEST_CASE="#mainPageTest"
