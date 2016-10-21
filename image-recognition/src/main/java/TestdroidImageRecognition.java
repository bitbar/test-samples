import io.appium.java_client.TouchAction;
import org.opencv.core.Point;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by testdroid on 22/07/16.
 */
public class TestdroidImageRecognition extends AbstractAppiumTest {

    public Logger logger = LoggerFactory.getLogger(TestdroidImageRecognition.class);
    private final int DEFAULT_RETRIES = 5;
    private final int DEFAULT_RETRY_WAIT = 0;
    private final double DEFAULT_TOLERANCE = 0.6;
    private final boolean DEFAULT_WITH_ASSERT = true;
    private final boolean DEFAULT_TAKE_SCREENSHOT = true;
    private final boolean DEFAULT_CROP = false;
    AkazeImageFinder imageFinder = new AkazeImageFinder();

    private String queryimageFolder = "";
    private static long startTime;
    long timeDifferenceStartTest;
    public boolean found = false;


    //If this method is called inside a test the script will check if the device has a resolution lower than 500x500 and if so will use
    // a different set of images when trying to find a image. These images are located in /queryimages/low_res
    public void setQueryImageFolder() {
        Dimension size = driver.manage().window().getSize();
        log("Screen size: " + size.toString());
        if ((size.getHeight() <= 500) || (size.getWidth() <= 500)) {
            queryimageFolder = "low_res/";
        }
    }

    /**
     * ======================================================================================
     * FINDING AN IMAGE IN ANOTHER IMAGE
     * ======================================================================================
     */

    public Point[] findImage(String image, String scene) throws Exception {
        return findImage(image, scene, DEFAULT_TOLERANCE);
    }

    //This method calls on the Akaze scripts to find the coordinates of a given image in another image.
    //The "image" parameter is the image that you are searching for
    //The "scene" parameter is the image in which we are looking for "image"
    // "tolerance" sets the required accuracy for the image recognition algorithm.
    public Point[] findImage(String image, String scene, double tolerance) throws Exception {
        Point[] imgRect = new Point[0];
        Point[] imgRectScaled;

        // queryImageFolder is "", unless set by setQueryImageFolder()
        String queryImageFile = "queryimages/" + queryimageFolder + image;
        log("Searching for " + queryImageFile);
        log("Searching in " + searchedImage);
        try {
            imgRect = imageFinder.findImage(queryImageFile, searchedImage, tolerance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (imgRect != null) {
            Dimension size = getScreenSizeADB();

            if (platformName.equalsIgnoreCase("iOS")) {
                //for retina devices we need to recalculate coordinates
                double sceneHeight = imageFinder.getSceneHeight();
                double sceneWidth = imageFinder.getSceneWidth();

                int screenHeight = size.getHeight();
                int screenWidth = size.getWidth();

                // Make sure screenshot size values are "landscape" for comparison
                if (sceneHeight > sceneWidth) {
                    double temp = sceneHeight;
                    sceneHeight = sceneWidth;
                    sceneWidth = temp;
                }

                // Make sure screen size values are "landscape" for comparison
                if (screenHeight > screenWidth) {
                    int temp = screenHeight;
                    screenHeight = screenWidth;
                    screenWidth = temp;
                }

                if ((screenHeight<sceneHeight) && (screenWidth<sceneWidth)) {
                    if ((screenHeight<sceneHeight/2)&&(screenWidth<sceneWidth/2)) {
                        imgRectScaled = new Point[]{new Point(imgRect[0].x / 3, imgRect[0].y / 3), new Point(imgRect[1].x / 3, imgRect[1].y / 3), new Point(imgRect[2].x / 3, imgRect[2].y / 3), new Point(imgRect[3].x / 3, imgRect[3].y / 3), new Point(imgRect[4].x / 3, imgRect[4].y / 3)};
                        log("Device with Retina display rendered at x3 => coordinates have been recalculated");
                        imgRect = imgRectScaled;
                    }
                    else {
                        imgRectScaled = new Point[]{new Point(imgRect[0].x / 2, imgRect[0].y / 2), new Point(imgRect[1].x / 2, imgRect[1].y / 2), new Point(imgRect[2].x / 2, imgRect[2].y / 2), new Point(imgRect[3].x / 2, imgRect[3].y / 2), new Point(imgRect[4].x / 2, imgRect[4].y / 2)};
                        log("Device with Retina display rendered at x2 => coordinates have been recalculated");
                        imgRect = imgRectScaled;
                    }
                }
            }

            Point center = imgRect[4];

            // Check that found center coordinate isn't out of screen bounds
            if ((center.x >= size.width) || (center.x < 0) || (center.y >= size.height) || (center.y < 0)) {
                log("Screen size is (width, height): " + size.getWidth() + ", " + size.getHeight());
                log("WARNING: Coordinates found do not match the screen --> image not found.");
                imgRect = null;
            } else {
              return imgRect;
            }
        }
	      return null;
    }

    /**
     * ======================================================================================
     * TAPS ON SCREEN - MIDDLE OR AT RELATIVE COORDINATES
     * ======================================================================================
     */

    // Taps on the center of the screen.
    public void tapMiddle() throws Exception {
        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth() / 2, size.getHeight() / 2);

        tapAtCoordinates((int) middle.x, (int) middle.y);
    }

    // Taps at given coordinates given in pixels
    public void tapAtCoordinates(int x, int y) throws Exception {
        if (automationName.equalsIgnoreCase("selendroid")) {
            selendroidTapAtCoordinate(x, y, 1);
        } else if (platformName.equalsIgnoreCase("Android")){
            Dimension size = driver.manage().window().getSize();
        	if(y > size.getHeight() || x > size.getWidth()){
    			log("using adb tap at " + x + ", " + y);
    			try{
                    //run eclipse from commandline to get path variable correct and find adb
            		Process p = Runtime.getRuntime().exec("adb -s " + udid + " shell input tap " + x + " " + y);
            		p.waitFor();
    			} catch (Exception e) {
    				log(e.toString());
    			}
        	} else {
        		driver.tap(1, x, y, 1);
        	}
        } else {
        	driver.tap(1, x, y, 1);
        }
    }

    // Selendroid specific taps at given coordinates in pixels
    public void selendroidTapAtCoordinate(int x, int y, int secs) throws Exception {
        TouchActions actions = new TouchActions(driver);
        actions.down(x, y).perform();
        sleep(secs);
        actions.up(x, y).perform();
    }

    //Taps at relative coordinates on the screen.
    // "x_offset" and "y_offset" set the X and Y coordinates.
    // "taps" sets the number of taps that are performed.
    // "frequency" sets the frequency of the taps.
    public void tapAtRelativeCoordinates(double x_offset, double y_offset, int taps, double frequency) throws Exception {
        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * x_offset, middle.y * y_offset);

        log("Tapping at coordinates: " + middleWithOffset.toString() + "  when size of the screen is: " + size.toString());
        for (int i = 0; i < taps; i++) {
            if (automationName.equalsIgnoreCase("selendroid")) {
                selendroidTapAtCoordinate((int) middleWithOffset.x, (int) middleWithOffset.y, 1);
            } else {
                driver.tap(1, (int) middleWithOffset.x, (int) middleWithOffset.y, 1);
            }
            sleep(frequency);
        }
    }

    public void tapAtRelativeCoordinates(double x_offset, double y_offset, int taps) throws Exception {
        tapAtRelativeCoordinates(x_offset, y_offset, taps, 1);
    }

    public void tapAtRelativeCoordinates(double x_offset, double y_offset) throws Exception {
        tapAtRelativeCoordinates(x_offset, y_offset, 1, 0.9);
    }

    /**
     * ======================================================================================
     * FINDING AN IMAGE ON SCREEN
     * ======================================================================================
     */

    //Checks if an image is visible on the current screen view.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "tolerance" sets the required accuracy for the image recognition algorithm.
    // "with_assert" specifies if the method will return a fail or not if the searched image is not found on the screen. Use findImageOnScreenNoAssert() to have this set by default to FALSE
    public Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance, boolean withAssert, boolean take_screenshot, boolean crop) throws Exception {
        Point[] imgRect = null;
        boolean new_step = true;
        long start_time = System.nanoTime();
        int originalRetries = retries;
        while ((retries > 0) && (imgRect == null)) {
            if (retries < originalRetries) {
                if (retryWait > 0) {
                    log("retryWait given, sleeping " + retryWait + " seconds.");
                    sleep(retryWait);
                }
                new_step = false;
            }

            log("Find image started, retries left: " + retries);
            if (take_screenshot)
                takeScreenshot(image + "_screenshot", new_step);
            imgRect = findImage(image, image + "_screenshot" + getRetryCounter() + "_" + timeDifferenceStartTest, tolerance);
            retries = retries - 1;
        }

        long end_time = System.nanoTime();
        int difference = (int) ((end_time - start_time) / 1e6 / 1000);
        log("==> Find image took: " + difference + " secs.");

        if (withAssert) {
            assertNotNull("Image " + image + " not found on screen.", imgRect);
        }

        if (crop) {
            Point top_left = imgRect[0];
            Point top_right = imgRect[1];
            Point bottom_left = imgRect[2];
            Point center = imgRect[4];
            imageFinder.cropImage(screenshotsFolder + getScreenshotsCounter() + "_" + image + "_screenshot" + getRetryCounter() + "_" + timeDifferenceStartTest + "sec", top_left.x, top_left.y, top_right.x - top_left.x, bottom_left.y - top_left.y);
        }
        return imgRect;
    }

    public Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance, boolean withAssert, boolean takeScreenshot) throws Exception {
        return findImageOnScreen(image, retries, retryWait, tolerance, withAssert, takeScreenshot, DEFAULT_CROP);
    }

    public Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance, boolean withAssert) throws Exception {
        return findImageOnScreen(image, retries, retryWait, tolerance, withAssert, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    public Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance) throws Exception {
        return findImageOnScreen(image, retries, retryWait, tolerance, DEFAULT_WITH_ASSERT, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    public Point[] findImageOnScreen(String image, int retries, int retryWait) throws Exception {
        return findImageOnScreen(image, retries, retryWait, DEFAULT_TOLERANCE, DEFAULT_WITH_ASSERT, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    public Point[] findImageOnScreen(String image, int retries) throws Exception {
        return findImageOnScreen(image, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE, DEFAULT_WITH_ASSERT, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    public Point[] findImageOnScreen(String image) throws Exception {
        return findImageOnScreen(image, DEFAULT_RETRIES);
    }

    public Point[] findImageOnScreenNoAssert(String image, int retries, int retryWait, double tolerance) throws Exception {
        return findImageOnScreen(image, retries, retryWait, tolerance, false, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    public Point[] findImageOnScreenNoAssert(String image, int retries, int retryWait) throws Exception {
        return findImageOnScreenNoAssert(image, retries, retryWait, DEFAULT_TOLERANCE);
    }

    public Point[] findImageOnScreenNoAssert(String image, int retries) throws Exception {
        return findImageOnScreenNoAssert(image, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public Point[] findImageOnScreenNoAssert(String image) throws Exception {
        return findImageOnScreenNoAssert(image, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public Point[] findImageOnScreen(String image, boolean take_screenshot) throws Exception {
        return findImageOnScreen(image, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE, DEFAULT_WITH_ASSERT, take_screenshot, DEFAULT_CROP);
    }

    //Searches for an image until it disappears from the current view. Good for checking if a loading screen has disappeared.
    public void waitForImageToDisappearFromScreen(String image) throws Exception {
        boolean first_time = true;
        boolean check = true;
        long start, present;
        start = System.nanoTime();
        present = start;

        log("==> Trying to find image: " + image);

        while ((check) && ((present - start) / 1e6 / 1000 < 300)) {

            if (first_time) {
                first_time = false;
                takeScreenshot(image + "_screenshot", true);
                if ((findImage(image, image + "_screenshot" + getRetryCounter())) == null) {
                    log("Loading screen not found. Moving on");
                    check = false;
                } else {
                    sleep(3);
                }
            } else {
                takeScreenshot(image + "_screenshot", false);
                if ((findImage(image, image + "_screenshot" + getRetryCounter())) == null) {
                    log("Loading screen not found. Moving on");
                    check = false;
                } else {
                    sleep(3);
                }
            }

            present = System.nanoTime();

            if ((present - start) / 1e6 / 1000 >= 300) {
                fail("Application takes too long to load: Stopping tests.....");
                check = false;
            }
        }
    }

    /**
     * ======================================================================================
     * TAPPING AN IMAGE ON SCREEN
     * ======================================================================================
     */

    //Finds an image on screen and taps on it. Method will cause a FAIL if the image is not found.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "tolerance" sets the required accuracy for the image recognition algorithm.
    // "x_offset" and "y_offset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    public void tapImageOnScreen(String image, double x_offset, double y_offset, int retries, int retryWait, double tolerance) throws Exception {
        Point[] imgRect = findImageOnScreen(image, retries, retryWait, tolerance);
        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];
        Point center = imgRect[4];

        if ((x_offset == 0.5) && (y_offset == 0.5)) {
            log("Coordinates are: " + center.x + "," + center.y);
            tapAtCoordinates((int) center.x, (int) center.y);
        } else {
            //adding the offset to each coordinate; if offset = 0.5, middle will be returned
            double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
            double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

            log("Coordinates with offset are: " + newX + ", " + newY);
            tapAtCoordinates((int) newX, (int) newY);
        }
    }

    public void tapImageOnScreen(String image, double x_offset, double y_offset, int retries, int retryWait) throws Exception {
        tapImageOnScreen(image, x_offset, y_offset, retries, retryWait, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image, double x_offset, double y_offset, int retries) throws Exception {
        tapImageOnScreen(image, x_offset, y_offset, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image, double x_offset, double y_offset) throws Exception {
        tapImageOnScreen(image, x_offset, y_offset, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image, int retries, int retryWait, double tolerance) throws Exception {
        tapImageOnScreen(image, 0.5, 0.5, retries, retryWait, tolerance);
    }

    public void tapImageOnScreen(String image, int retries, int retryWait) throws Exception {
        tapImageOnScreen(image, 0.5, 0.5, retries, retryWait, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image, int retries) throws Exception {
        tapImageOnScreen(image, 0.5, 0.5, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image) throws Exception {
        tapImageOnScreen(image, 0.5, 0.5, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    //Finds an image on screen and taps on it. Method will cause a FAIL if the image is not found.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "x_offset" and "y_offset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    // "taps" sets the number of taps that are performed.
    // "frequency" sets the frequency of the taps.
    public void multipleTapImageOnScreen(String image, int retries, int taps, double frequency, double x_offset, double y_offset) throws Exception {
        Point[] imgRect = findImageOnScreen(image, retries);
        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];
        Point center = imgRect[4];
        //imgRect[4] will have the center of the rectangle containing the image

        if ((x_offset == 0.5) && (y_offset == 0.5)) {

            log("Coordinates are: " + center.x + "," + center.y);

            for (int i = 0; i < taps; i++) {
                tapAtCoordinates((int) center.x, (int) center.y);
                sleep(frequency);
            }
        } else {
            double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
            double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

            for (int i = 0; i < taps; i++) {
                log("Coordinates with offset are: " + newX + ", " + newY);
                tapAtCoordinates((int) newX, (int) newY);
                sleep(frequency);
            }
        }
    }

    public void multipleTapImageOnScreen(String image, int taps, double frequency, double x_offset, double y_offset) throws Exception {
        multipleTapImageOnScreen(image, DEFAULT_RETRIES, taps, frequency, x_offset, y_offset);
    }

    public void multipleTapImageOnScreen(String image, int retries, int taps, double frequency) throws Exception {
        multipleTapImageOnScreen(image, retries, taps, frequency, 0.5, 0.5);
    }

    public void multipleTapImageOnScreen(String image, int taps, double frequency) throws Exception {
        multipleTapImageOnScreen(image, DEFAULT_RETRIES, taps, frequency, 0.5, 0.5);
    }

    public void multipleTapImageOnScreen(String image, int taps) throws Exception {
        multipleTapImageOnScreen(image, DEFAULT_RETRIES, taps, 1, 0.5, 0.5);
    }

    //Finds an image on screen and taps on it. Method will NOT cause a FAIL if the image is not found.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "x_offset" and "y_offset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    public boolean tryTapImageOnScreen(String image, double x_offset, double y_offset, int retries) throws Exception {
        Point[] imgRect = findImageOnScreenNoAssert(image, retries);

        if (imgRect == null) {
            return false;
        } else {
            Point top_left = imgRect[0];
            Point top_right = imgRect[1];
            Point bottom_left = imgRect[2];
            Point center = imgRect[4];

            if ((x_offset == 0.5) && (y_offset == 0.5)) {
                log("Coordinates are: " + center.x + "," + center.y);
                tapAtCoordinates((int) center.x, (int) center.y);
            } else {
                double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
                double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;
                tapAtCoordinates((int) newX, (int) newY);
            }
            return true;
        }
    }

    public boolean tryTapImageOnScreen(String image, double x_offset, double y_offset) throws Exception {
        return tryTapImageOnScreen(image, x_offset, y_offset, DEFAULT_RETRIES);
    }

    public boolean tryTapImageOnScreen(String image, int retries) throws Exception {
        return tryTapImageOnScreen(image, 0.5, 0.5, retries);
    }

    public boolean tryTapImageOnScreen(String image) throws Exception {
        return tryTapImageOnScreen(image, 0.5, 0.5, DEFAULT_RETRIES);
    }

    // Finds an image on screen and taps and hold on it for a specified duration.
    // "duration" is given in seconds
    // "with_assert" specifies if the method will return a fail or not if the image is not found.
    public void tapAndHoldImageOnScreen(String image, double x_offset, double y_offset, int duration, boolean with_assert) throws Exception {

        Point[] imgRect;

        if (with_assert) {
            imgRect = findImageOnScreen(image);
        } else {
            imgRect = findImageOnScreenNoAssert(image);
        }

        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];

        double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
        double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

        log("Coordinates with offset are: " + newX + ", " + newY);

        if (automationName.equalsIgnoreCase("selendroid")) {
            selendroidTapAtCoordinate((int) newX, (int) newY, duration);
        } else {
            driver.tap(1, (int) newX, (int) newY, duration);
        }
    }

    public void tapAndHoldImageOnScreen(String image, double x_offset, double y_offset, int duration) throws Exception {
        tapAndHoldImageOnScreen(image, x_offset, y_offset, duration, DEFAULT_WITH_ASSERT);
    }

    public void tapAndHoldImageOnScreen(String image, int duration, boolean with_assert) throws Exception {
        tapAndHoldImageOnScreen(image, 0.5, 0.5, duration, with_assert);
    }

    public void tapAndHoldImageOnScreen(String image, int duration) throws Exception {
        tapAndHoldImageOnScreen(image, 0.5, 0.5, duration, DEFAULT_WITH_ASSERT);
    }

    //Taps and holds on relative coordinates on the screen.
    public void tapAndHoldOnScreen(double x_offset, double y_offset, int duration) throws Exception {

        duration = duration * 1000;
        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * x_offset, middle.y * y_offset);

        log("Coordinates with offset are: " + middleWithOffset.x + ", " + middleWithOffset.y);

        if (automationName.equalsIgnoreCase("selendroid")) {
            selendroidTapAtCoordinate((int) middleWithOffset.x, (int) middleWithOffset.y, duration);
        } else {
            driver.tap(1, (int) middleWithOffset.x, (int) middleWithOffset.y, duration);
        }
    }

    /**
     * =====================================================================================
     * SWIPE GESTURES
     * =====================================================================================
     */

    //Performs a vertical swipe on the screen, starting from a searched image.
    //"distance" is given in pixels. A positive "distance" will perform a swipe down, a negative "distance" will perform a swipe up.
    //if "x_offset" and "y_offset" are used, the swipe will start from a relative coordinate of that image. If not used, the swipe will start from the center of the image.
    public void swipeVerticallyOnImage(String image, int distance, double x_offset, double y_offset) throws Exception {
        Point[] imgRect = findImageOnScreen(image);

        int startX = (int) (imgRect[0].x + (imgRect[1].x - imgRect[0].x) * x_offset);
        int startY = (int) (imgRect[0].y + (imgRect[2].y - imgRect[0].y) * y_offset);
        int endX = startX;
        int endY = startY + distance;

        if (platformName.equalsIgnoreCase("iOS")) {
            iOSSwipe(startX, startY, endX, endY);
        } else {
            androidSwipe(startX, startY, endX, endY);
        }
    }

    public void swipeVerticallyOnImage(String image, int distance) throws Exception {
        swipeVerticallyOnImage(image, distance, 0.5, 0.5);
    }

    //Performs a horizontal swipe on the screen, starting from a searched image.
    //"distance" is given in pixels. A positive "distance" will perform a swipe to the right, a negative "distance" will perform a swipe to the left.
    //if "x_offset" and "y_offset" are used, the swipe will start from a relative coordinate of that image. If not used, the swipe will start from the center of the image.
    public void swipeHorizontallyOnImage(String image, int distance, double x_offset, double y_offset) throws Exception {
        Point[] imgRect = findImageOnScreen(image);

        int startX = (int) (imgRect[0].x + (imgRect[1].x - imgRect[0].x) * x_offset);
        int startY = (int) (imgRect[0].y + (imgRect[2].y - imgRect[0].y) * y_offset);
        int endX = startX + distance;
        int endY = startY;

        if (platformName.equalsIgnoreCase("iOS")) {
            iOSSwipe(startX, startY, endX, endY);
        } else {
            androidSwipe(startX, startY, endX, endY);
        }
    }

    public void swipeHorizontallyOnImage(String image, int distance) throws Exception {
        swipeHorizontallyOnImage(image, distance, 0.5, 0.5);
    }


    //Performs a horizontal swipe starting from a relative coordinate of the device screen.
    //"distance" is given in pixels. A positive "distance" will perform a swipe to the right, a negative "distance" will perform a swipe to the left.
    //"swipes" sets the number of swipes that are performed.
    //"frequency" sets the frequency of the swipes.
    public void swipeHorizontally(double x_offset, double y_offset, int distance, int swipes, double frequency) throws Exception {

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * x_offset, middle.y * y_offset);

        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX + distance;
        int endY = startY;


        for (int i = 0; i < swipes; i++) {
            if (platformName.equalsIgnoreCase("iOS")) {
                iOSSwipe(startX, startY, endX, endY);
            } else {
                androidSwipe(startX, startY, endX, endY);
            }
            sleep(frequency);
        }
        log("Finished executing swipes");
    }

    //Performs a horizontal swipe starting from a relative coordinate of the device screen.
    //"distance" is relative, a number from 0 to 1. A positive "distance" will perform a swipe to the right, a negative "distance" will perform a swipe to the left.
    //"swipes" sets the number of swipes that are performed.
    //"frequency" sets the frequency of the swipes.
    public void swipeHorizontally(double x_offset, double y_offset, double distance, int swipes, double frequency) throws Exception { //positive distance for swipe right, negative for swipe left.

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * x_offset, middle.y * y_offset);
        double relative_distance = middle.x * distance;

        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX + (int) Math.floor(relative_distance);
        int endY = startY;


        for (int i = 0; i < swipes; i++) {
            if (platformName.equalsIgnoreCase("iOS")) {
                iOSSwipe(startX, startY, endX, endY);
            } else {
                androidSwipe(startX, startY, endX, endY);
            }
            sleep(frequency);
        }
        log("Finished executing swipes");
    }


    //Performs a vertical swipe starting from a relative coordinate of the device screen.
    //"distance" is given in pixels. A positive "distance" will perform a swipe down, a negative "distance" will perform a swipe up.
    //"swipes" sets the number of swipes that are performed.
    //"frequency" sets the frequency of the swipes.
    public void swipeVertically(double x_offset, double y_offset, int distance, int swipes, double frequency) throws Exception {

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * x_offset, middle.y * y_offset);

        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX;
        int endY = startY + distance;


        for (int i = 0; i < swipes; i++) {
            if (platformName.equalsIgnoreCase("iOS")) {
                iOSSwipe(startX, startY, endX, endY);
            } else {
                androidSwipe(startX, startY, endX, endY);
            }
            sleep(frequency);
        }
        log("Finished executing swipes");
    }

    //Performs a vertical swipe starting from a relative coordinate of the device screen.
    //"distance" is relative, a number from 0 to 1. A positive "distance" will perform a swipe down, a negative "distance" will perform a swipe up.
    //"swipes" sets the number of swipes that are performed.
    //"frequency" sets the frequency of the swipes.
    public void swipeVertically(double x_offset, double y_offset, double distance, int swipes, double frequency) throws Exception {

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * x_offset, middle.y * y_offset);
        double relative_distance = middle.y * distance;


        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX;
        int endY = startY + (int) Math.floor(relative_distance);


        for (int i = 0; i < swipes; i++) {
            if (platformName.equalsIgnoreCase("iOS")) {
                iOSSwipe(startX, startY, endX, endY);
            } else {
                androidSwipe(startX, startY, endX, endY);
            }
            sleep(frequency);
        }
        log("Finished executing swipes");
    }


    public void androidSwipe(int startX, int startY, int endX, int endY) throws Exception {
        TouchActions actions = new TouchActions(driver);

        actions.down(startX, startY).perform();
        sleep(0.5);
        actions.move(endX, endY).perform();
        actions.up(endX, endY).perform();
    }

    public void iOSSwipe(int startX, int startY, int endX, int endY) throws Exception {
        TouchAction action = new TouchAction(driver);

        action.press(startX, startY);
        action.waitAction(1000);  //has to be >= 500 otherwise it will fail
        action.moveTo(endX, endY);
        action.release();
        action.perform();
    }


    public void swipe(double startX, double startY, double endX, double endY) throws Exception {
        TouchActions actions = new TouchActions(driver);
        Dimension size = driver.manage().window().getSize();

        Point screen = new Point(size.getWidth(), size.getHeight());
        Point swipe_start = new Point(screen.x * startX, screen.y * startY);
        Point swipe_end = new Point(screen.x * endX, screen.y * endY);

        if (platformName.equalsIgnoreCase("Android")) {
            androidSwipe((int) swipe_start.x, (int) swipe_start.y, (int) swipe_end.x, (int) swipe_end.y);
        } else {
            iOSSwipe((int) swipe_start.x, (int) swipe_start.y, (int) swipe_end.x, (int) swipe_end.y);
        }
    }

    /**
     * ======================================================================================
     * DRAG AND DROP
     * ======================================================================================
     */

    //Performs a drag and drop from the middle of the "object" image to the middle of the "location" image.
    public void dragFromOneImageToAnother(String object, String location) throws Exception {

        Point[] object_coord = findImageOnScreen(object);
        Point[] location_coord = findImageOnScreen(location, false);

        int startX = (int) object_coord[4].x;
        int startY = (int) object_coord[4].y;

        int endX = (int) location_coord[4].x;
        int endY = (int) location_coord[4].y;

        if (automationName.equalsIgnoreCase("selendroid")) {
            androidSwipe(startX, startY, endX, endY);
        } else {
            iOSSwipe(startX, startY, endX, endY);
        }
    }


    //Performs a drag and drop from the middle of the "object" image to the middle of the screen.
    public void dragImageToMiddle(String object) throws Exception {

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth() / 2 - 20, size.getHeight() / 2 + 20);

        Point[] object_coord = findImageOnScreen(object, 10);

        int startX = (int) object_coord[4].x;
        int startY = (int) object_coord[4].y;

        int endX = (int) middle.x;
        int endY = (int) middle.y;

        if (automationName.equalsIgnoreCase("selendroid")) {
            androidSwipe(startX, startY, endX, endY);
        } else {
            iOSSwipe(startX, startY, endX, endY);
        }
    }

    //Performs a drag and drop from the middle of the "object" image to the specified relative coordinates of the screen.
    public void dragImageToCoordinates(String object, double x_offset, double y_offset) throws Exception {

        Dimension size = driver.manage().window().getSize();
        Point screen = new Point(size.getWidth(), size.getHeight());
        Point swipe_end = new Point(screen.x * x_offset, screen.y * y_offset);

        Point[] object_coord = findImageOnScreen(object, 10);

        int startX = (int) object_coord[4].x;
        int startY = (int) object_coord[4].y;

        int endX = (int) swipe_end.x;
        int endY = (int) swipe_end.y;

        if (automationName.equalsIgnoreCase("selendroid")) {
            androidSwipe(startX, startY, endX, endY);
        } else {
            iOSSwipe(startX, startY, endX, endY);
        }
    }

    /**
     * ======================================================================================
     * ADB UTILITIES
     * ======================================================================================
     */


    //Uses adb commands to get the screen size. To be used when appium methods fail. Only works on Android devices.
    public Dimension getScreenSizeADB() throws Exception {
        log("trying to get size from adb...");
        log("------------------------------");
        if (platformName.equalsIgnoreCase("iOS")) {
            return driver.manage().window().getSize();
        } else {
            String adb = "adb";
            String[] adbCommand = {adb, "shell", "dumpsys", "window"};
            try {
                ProcessBuilder p = new ProcessBuilder(adbCommand);
                Process proc = p.start();
                InputStream stdin = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                String[] size = null;
                while ((line = br.readLine()) != null) {
                    if (!line.contains("OriginalmUnrestrictedScreen")) { //we do this check for devices with android 5.x+ The adb command returns an extra line with the values 0x0 which must be filtered out.
                        if (line.contains("mUnrestrictedScreen")) {
                            proc.waitFor();
                            String[] tmp = line.split("\\) ");
                            size = tmp[1].split("x");
                        }
                    }
                }
                int width = Integer.parseInt(size[0]);
                int height = Integer.parseInt(size[1]);
                Dimension screenSize = new Dimension(width, height);
                return screenSize;

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return null;
    }

    public boolean findDeviceTypeADB() throws Exception {
        log("trying to find device type ...");
        log("------------------------------");
        if (platformName.equalsIgnoreCase("iOS")) {
            //TO Be added
        } else {
            String adb = "adb";
            String[] adbCommand = {adb, "shell", "getprop", "ro.build.characteristics"};
            try {
                ProcessBuilder p = new ProcessBuilder(adbCommand);
                Process proc = p.start();
                InputStream stdin = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                String[] size = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("tablet")) {
                        return true;
                    }

                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return false;
    }

    //Uses adb commands to tap at relative coordinates. To be used when appium methods fail. Only works on Android devices.
    public void tapAtRelativeCoordinatesADB(double x_offset, double y_offset) throws Exception {
        if (platformName.equalsIgnoreCase("iOS")) {
            tapAtRelativeCoordinates(x_offset, y_offset);
        } else {
            Dimension size = getScreenSizeADB();
            log("Size of device as seen by ADB is - width: " + size.width + " height: " + size.height);
            String x = String.valueOf(size.width * x_offset);
            String y = String.valueOf(size.height * y_offset);
            log("ADB: x and y: " + x + ", " + y);
            String[] adbCommand = {"adb", "shell", "input", "tap", x, y};
//            String[] adbCommand = {"adb", "shell", "input", "touchscreen", "swipe", x, y, x, y, "2000"};

            try {
                ProcessBuilder p = new ProcessBuilder(adbCommand);
                Process proc = p.start();
                InputStream stdin = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null)
                    System.out.print(line);


                proc.waitFor();

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    //Uses adb commands to tap at coordinates. To be used when appium methods fail. Only works on Android devices.
    public void tapAtCoordinatesADB(double x, double y) throws Exception {
        String[] adbCommand;
        if (platformName.equalsIgnoreCase("iOS")) {
            tapAtCoordinates((int) x, (int) y);
        } else {
            int Xx = (int) x;
            int Yy = (int) y;
            String X = String.valueOf(Xx);
            String Y = String.valueOf(Yy);
            log("ADB: X: " + X + ", Y: " + Y);
//                        String[] adbCommand = {"adb", "shell", "input", "tap", X, Y};

            if (automationName.equalsIgnoreCase("selendroid")) {
                log("adb_shell_input_tap"); //works for 4.1.x. Will not work for 4.0.x
                adbCommand = new String[]{"adb", "shell", "input", "tap", X, Y};
                processBuilder(adbCommand);
                log("Tap done.");
            } else {
                adbCommand = new String[]{"adb", "shell", "input", "touchscreen", "swipe", X, Y, X, Y, "2000"};
                processBuilder(adbCommand);
            }
        }
    }

    public void processBuilder(String[] adbCommand) {
        try {
            found = true;
            ProcessBuilder p = new ProcessBuilder(adbCommand);
            Process proc = p.start();
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                System.out.print(line);

            proc.waitFor();

        } catch (Throwable t) {
            found = false;
            t.printStackTrace();
        }
    }

    /**
     * ======================================================================================
     * CROP IMAGE
     * ======================================================================================
     */

    public void findAndCropImageFromScreen(String image) throws Exception {
        findImageOnScreen(image, 3, DEFAULT_RETRY_WAIT, 0.60, true, true, true);
    }

    /**
     * ======================================================================================
     * TESSERACT GRAB TEXT FROM IMAGE
     * ======================================================================================
     */

    public String grabText(String image) throws Exception {
        findAndCropImageFromScreen(image);
        String imageInput = screenshotsFolder + getScreenshotsCounter() + "_" + image + "_screenshot" + getRetryCounter() + "_" + timeDifferenceStartTest + "sec" + ".png";
        String[] tesseractCommand = {"tesseract", imageInput, "stdout"};
        String value = "";
        try {
            ProcessBuilder p = new ProcessBuilder(tesseractCommand);
            Process proc = p.start();
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line;
            String[] size = null;
            String[] splitLines;
            while ((line = br.readLine()) != null) {
                value += line;
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return value;
    }

    /**
     * ======================================================================================
     * OTHER UTILITIES
     * ======================================================================================
     */

    //TODO: experimental method
    public Point correctAndroidCoordinates(Point appium_coord) throws Exception {

        Dimension appium_dimensions = driver.manage().window().getSize();
        int appium_screenWidth = appium_dimensions.getWidth();
        int appium_screenHeight = appium_dimensions.getHeight();

        Dimension adb_dimension = getScreenSizeADB();
        int adb_screenWidth = adb_dimension.getWidth();
        int adb_screenHeight = adb_dimension.getHeight();

        double x_offset = appium_coord.x / appium_screenWidth;
        double y_offset = appium_coord.y / appium_screenHeight;
        log("x_offset is : " + x_offset);
        log("y_offset is : " + y_offset);

        return new Point(x_offset * adb_screenWidth, y_offset * adb_screenHeight);
    }

    public void cancelGameCenterLogin() throws Exception {
        log("Check to see if Google Plus Sign in needs to be cancelled..");
        if (driver.getPageSource().contains("Choose an account")) {
            if (automationName.equalsIgnoreCase("selendroid")) {
                driver.findElement(By.xpath("//LinearLayout/Button[@text='Cancel']")).click();
            } else if (platformName.equalsIgnoreCase("android")) {
                driver.findElement(By.xpath("//android.widget.Button[@text='Cancel']")).click();
            } else { //we are on ios
                driver.findElementByAccessibilityId("Cancel").click();
            }
        }
        if (automationName.equalsIgnoreCase("selendroid")) {
            while (findImageOnScreenNoAssert("native_sign_in_button") != null) {
                tapImageOnScreen("native_sign_in_button", 0.75, 0.5, 1);
            }
        } else {
            if (driver.getPageSource().contains("SIGN IN") || (driver.getPageSource().contains("Sign in")) || (driver.getPageSource().contains("Sign In"))) {
                log("Google Plus sign in shown...");
                takeScreenshot("google_plus_sign_in_shown");
                driver.findElement(By.xpath("//android.widget.Button[@text='Cancel']")).click();
                takeScreenshot("after_cancel_sign_in");
                driver.findElement(By.xpath("//android.widget.Button[@text='Cancel']")).click();
                takeScreenshot("after_second_cancel_sign_in");
            }
        }
    }

    public void gameCenterLogin() throws Exception {
        if (driver.getPageSource().contains("Sign in")) {
            log("Google Plus sign in shown...");
            log(driver.getPageSource());
            takeScreenshot("google_plus_sign_in_shown");
            while (driver.getPageSource().contains("Sign in"))
                if (automationName.equalsIgnoreCase("selendroid")) {
                    driver.findElement(By.xpath("//LinearLayout/Button[@text='Sign in']")).click();
                } else if (platformName.equalsIgnoreCase("android")) {
                    driver.findElement(By.xpath("//android.widget.Button[@text='Sign in']")).click();
                } else { //we are on ios
                    driver.findElementByAccessibilityId("Sign In").click();
                }
            takeScreenshot("after_clicking_sign_in");
        }
    }

    public void dismissFullScreenMessage() throws Exception {
        log("Trying to dismiss Full Screen notification message if it shows up...");
        tryTapImageOnScreen("full_screen_1", 0.75, 0.75, 2);
        tryTapImageOnScreen("full_screen_2", 0.75, 0.75, 1);
        tryTapImageOnScreen("full_screen_3", 0.75, 0.8, 1);
    }

}
