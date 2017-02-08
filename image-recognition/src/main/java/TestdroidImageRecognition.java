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
@SuppressWarnings({"UnqualifiedFieldAccess", "UnqualifiedMethodAccess", "UnclearExpression"})
public class TestdroidImageRecognition extends AbstractAppiumTest {

    final Logger logger;
    private static final int DEFAULT_RETRIES = 5;
    private static final int DEFAULT_RETRY_WAIT = 0;
    private static final double DEFAULT_TOLERANCE = 0.6;
    private static final boolean DEFAULT_WITH_ASSERT = true;
    private static final boolean DEFAULT_TAKE_SCREENSHOT = true;
    private static final boolean DEFAULT_CROP = false;
    private final AkazeImageFinder imageFinder = new AkazeImageFinder();

    private String queryimageFolder = "";
    // private static long startTime;
    private static final long timeDifferenceStartTest = 0L;

    TestdroidImageRecognition() {
        super();
        logger = LoggerFactory.getLogger(TestdroidImageRecognition.class);
    }

    TestdroidImageRecognition(Logger childLogger) {
        super();
        logger = childLogger;
    }


    //If this method is called inside a test the script will check if the device has a resolution lower than 500x500 and if so will use
    // a different set of images when trying to find a image. These images are located in /queryimages/low_res
    public void setQueryImageFolder() {
        Dimension size = driver.manage().window().getSize();
        log("Screen size: " + size);
        //noinspection MagicNumber,MagicNumber
        if ((size.getHeight() <= 500) || (size.getWidth() <= 500)) {
            queryimageFolder = "low_res/";
        }
    }

    /**
     * ======================================================================================
     * FINDING AN IMAGE IN ANOTHER IMAGE
     * ======================================================================================
     */

    private Point[] findImage(String image, String scene) {
        return findImage(image, scene, DEFAULT_TOLERANCE);
    }

    //This method calls on the Akaze scripts to find the coordinates of a given image in another image.
    //The "image" parameter is the image that you are searching for
    //The "scene" parameter is the image in which we are looking for "image"
    // "tolerance" sets the required accuracy for the image recognition algorithm.
    @SuppressWarnings("FeatureEnvy")
    private Point[] findImage(String image, String scene, double tolerance) {

        // queryImageFolder is "", unless set by setQueryImageFolder()
        String queryImageFile = "queryimages/" + queryimageFolder + image;
        log("Searching for " + queryImageFile);
        log("Searching in " + searchedImage);
        Point[] imgRect = new Point[0];
        try {
            imgRect = imageFinder.findImage(queryImageFile, searchedImage, tolerance);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        if (imgRect != null) {
            Dimension size = getScreenSizeADB();

            if ("iOS".equalsIgnoreCase(platformName)) {
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

                if ((screenHeight < sceneHeight) && (screenWidth < sceneWidth)) {
                    Point[] imgRectScaled;
                    if ((screenHeight < sceneHeight / 2) && (screenWidth < sceneWidth / 2)) {
                        imgRectScaled = new Point[]{new Point(imgRect[0].x / 3, imgRect[0].y / 3), new Point(imgRect[1].x / 3, imgRect[1].y / 3), new Point(imgRect[2].x / 3, imgRect[2].y / 3), new Point(imgRect[3].x / 3, imgRect[3].y / 3), new Point(imgRect[4].x / 3, imgRect[4].y / 3)};
                        log("Device with Retina display rendered at x3 => coordinates have been recalculated");
                        imgRect = imgRectScaled;
                    } else {
                        imgRectScaled = new Point[]{new Point(imgRect[0].x / 2, imgRect[0].y / 2), new Point(imgRect[1].x / 2, imgRect[1].y / 2), new Point(imgRect[2].x / 2, imgRect[2].y / 2), new Point(imgRect[3].x / 2, imgRect[3].y / 2), new Point(imgRect[4].x / 2, imgRect[4].y / 2)};
                        log("Device with Retina display rendered at x2 => coordinates have been recalculated");
                        imgRect = imgRectScaled;
                    }
                }
            }

            Point center = imgRect[4];

            // Check that found center coordinate isn't out of screen bounds
            //noinspection OverlyComplexBooleanExpression
            if ((center.x >= size.width) || (center.x < 0) || (center.y >= size.height) || (center.y < 0)) {
                log("Screen size is (width, height): " + size.getWidth() + ", " + size.getHeight());
                log("WARNING: Coordinates found do not match the screen --> image not found.");
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
    public void tapMiddle() throws InterruptedException {
        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth() / 2, size.getHeight() / 2);

        tapAtCoordinates((int) middle.x, (int) middle.y);
    }

    // Taps at given coordinates given in pixels
    private void tapAtCoordinates(int x, int y) throws InterruptedException {
        if ("selendroid".equalsIgnoreCase(automationName)) {
            selendroidTapAtCoordinate(x, y, 1);
        } else if ("Android".equalsIgnoreCase(platformName)) {
            Dimension size = driver.manage().window().getSize();
            if (y > size.getHeight() || x > size.getWidth()) {
                log("using adb tap at " + x + ", " + y);
                try {
                    //run eclipse from commandline to get path variable correct and find adb
                    Process p = Runtime.getRuntime().exec("adb -s " + udid + " shell input tap " + x + " " + y);
                    p.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
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
    private static void selendroidTapAtCoordinate(int x, int y, int secs) throws InterruptedException {
        TouchActions actions = new TouchActions(driver);
        actions.down(x, y).perform();
        sleep(secs);
        actions.up(x, y).perform();
    }

    //Taps at relative coordinates on the screen.
    // "xOffset" and "yOffset" set the X and Y coordinates.
    // "taps" sets the number of taps that are performed.
    // "frequency" sets the frequency of the taps.
    private void tapAtRelativeCoordinates(double xOffset, double yOffset, int taps,
                                          double frequency) throws InterruptedException {
        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * xOffset, middle.y * yOffset);

        log("Tapping at coordinates: " + middleWithOffset + "  when size of the screen is: " + size);
        for (int i = 0; i < taps; i++) {
            if ("selendroid".equalsIgnoreCase(automationName)) {
                selendroidTapAtCoordinate((int) middleWithOffset.x, (int) middleWithOffset.y, 1);
            } else {
                driver.tap(1, (int) middleWithOffset.x, (int) middleWithOffset.y, 1);
            }
            sleep(frequency);
        }
    }

    public void tapAtRelativeCoordinates(double xOffset, double yOffset, int taps) throws InterruptedException {
        tapAtRelativeCoordinates(xOffset, yOffset, taps, 1);
    }

    private void tapAtRelativeCoordinates(double xOffset, double yOffset) throws InterruptedException {
        //noinspection MagicNumber
        tapAtRelativeCoordinates(xOffset, yOffset, 1, 0.9);
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
    // "withAssert" specifies if the method will return a fail or not if the searched image is not found on the screen. Use findImageOnScreenNoAssert() to have this set by default to FALSE
    private Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance,
                                      boolean withAssert, boolean takeScreenshot, boolean crop) throws InterruptedException, IOException {
        int retries1 = retries;
        Point[] imgRect = null;
        boolean newStep = true;
        long startTime = System.nanoTime();
        int originalRetries = retries1;
        while ((retries1 > 0) && (imgRect == null)) {
            if (retries1 < originalRetries) {
                if (retryWait > 0) {
                    log("retryWait given, sleeping " + retryWait + " seconds.");
                    sleep(retryWait);
                }
                newStep = false;
            }

            log("Find image started, retries left: " + retries1);
            if (takeScreenshot)
                takeScreenshot(image + "_screenshot", newStep);
            imgRect = findImage(image, image + "_screenshot" + getRetryCounter() + "_" + timeDifferenceStartTest, tolerance);
            retries1 -= 1;
        }

        long endTime = System.nanoTime();
        @SuppressWarnings("MagicNumber") int difference = (int) ((endTime - startTime) / 1e6 / 1000);
        log("==> Find image took: " + difference + " secs.");

        if (withAssert) {
            assertNotNull("Image " + image + " not found on screen.", imgRect);
        }

        if (crop) {
            Point topLeft = imgRect[0];
            Point topRight = imgRect[1];
            Point bottomLeft = imgRect[2];
            Point center = imgRect[4];
            imageFinder.cropImage(screenshotsFolder + getScreenshotsCounter() + "_" + image + "_screenshot" + getRetryCounter() + "_" + timeDifferenceStartTest + "sec", topLeft.x, topLeft.y, topRight.x - topLeft.x, bottomLeft.y - topLeft.y);
        }
        return imgRect;
    }

    public Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance, boolean withAssert, boolean takeScreenshot) throws InterruptedException, IOException {
        return findImageOnScreen(image, retries, retryWait, tolerance, withAssert, takeScreenshot, DEFAULT_CROP);
    }

    public Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance, boolean withAssert) throws InterruptedException, IOException {
        return findImageOnScreen(image, retries, retryWait, tolerance, withAssert, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    private Point[] findImageOnScreen(String image, int retries, int retryWait, double tolerance) throws InterruptedException, IOException {
        return findImageOnScreen(image, retries, retryWait, tolerance, DEFAULT_WITH_ASSERT, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    public Point[] findImageOnScreen(String image, int retries, int retryWait) throws InterruptedException, IOException {
        return findImageOnScreen(image, retries, retryWait, DEFAULT_TOLERANCE, DEFAULT_WITH_ASSERT, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    private Point[] findImageOnScreen(String image, int retries) throws InterruptedException, IOException {
        return findImageOnScreen(image, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE, DEFAULT_WITH_ASSERT, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    Point[] findImageOnScreen(String image) throws IOException, InterruptedException {
        return findImageOnScreen(image, DEFAULT_RETRIES);
    }

    private Point[] findImageOnScreenNoAssert(String image, int retries, int retryWait,
                                              double tolerance) throws InterruptedException, IOException {
        return findImageOnScreen(image, retries, retryWait, tolerance, false, DEFAULT_TAKE_SCREENSHOT, DEFAULT_CROP);
    }

    public Point[] findImageOnScreenNoAssert(String image, int retries, int retryWait) throws IOException, InterruptedException {
        return findImageOnScreenNoAssert(image, retries, retryWait, DEFAULT_TOLERANCE);
    }

    private Point[] findImageOnScreenNoAssert(String image, int retries) throws IOException, InterruptedException {
        return findImageOnScreenNoAssert(image, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    private Point[] findImageOnScreenNoAssert(String image) throws IOException, InterruptedException {
        return findImageOnScreenNoAssert(image, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    private Point[] findImageOnScreen(String image, boolean takeScreenshot) throws InterruptedException, IOException {
        return findImageOnScreen(image, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE, DEFAULT_WITH_ASSERT, takeScreenshot, DEFAULT_CROP);
    }

    //Searches for an image until it disappears from the current view. Good for checking if a loading screen has disappeared.
    public void waitForImageToDisappearFromScreen(String image) throws IOException, InterruptedException {
        long start = System.nanoTime();

        log("==> Trying to find image: " + image);

        //noinspection MagicNumber,MagicNumber
        boolean check = true;
        boolean firstTime = true;
        long present = start;
        while ((check) && ((present - start) / 1e6 / 1000 < 300)) {

            if (firstTime) {
                firstTime = false;
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

            //noinspection MagicNumber,MagicNumber
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
    // "xOffset" and "yOffset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    private void tapImageOnScreen(String image, double xOffset, double yOffset, int retries,
                                  int retryWait, double tolerance) throws IOException, InterruptedException {
        Point[] imgRect = findImageOnScreen(image, retries, retryWait, tolerance);
        Point topLeft = imgRect[0];
        Point topRight = imgRect[1];
        Point bottomLeft = imgRect[2];
        Point center = imgRect[4];

        //noinspection MagicNumber,MagicNumber
        if ((xOffset == 0.5) && (yOffset == 0.5)) {
            log("Coordinates are: " + center.x + "," + center.y);
            tapAtCoordinates((int) center.x, (int) center.y);
        } else {
            //adding the offset to each coordinate; if offset = 0.5, middle will be returned
            double newX = topLeft.x + (topRight.x - topLeft.x) * xOffset;
            double newY = topLeft.y + (bottomLeft.y - topLeft.y) * yOffset;

            log("Coordinates with offset are: " + newX + ", " + newY);
            tapAtCoordinates((int) newX, (int) newY);
        }
    }

    public void tapImageOnScreen(String image, double xOffset, double yOffset, int retries, int retryWait) throws IOException, InterruptedException {
        tapImageOnScreen(image, xOffset, yOffset, retries, retryWait, DEFAULT_TOLERANCE);
    }

    private void tapImageOnScreen(String image, double xOffset, double yOffset, int retries) throws IOException, InterruptedException {
        tapImageOnScreen(image, xOffset, yOffset, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image, double xOffset, double yOffset) throws IOException, InterruptedException {
        tapImageOnScreen(image, xOffset, yOffset, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image, int retries, int retryWait, double tolerance) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        tapImageOnScreen(image, 0.5, 0.5, retries, retryWait, tolerance);
    }

    public void tapImageOnScreen(String image, int retries, int retryWait) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        tapImageOnScreen(image, 0.5, 0.5, retries, retryWait, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image, int retries) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        tapImageOnScreen(image, 0.5, 0.5, retries, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    public void tapImageOnScreen(String image) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        tapImageOnScreen(image, 0.5, 0.5, DEFAULT_RETRIES, DEFAULT_RETRY_WAIT, DEFAULT_TOLERANCE);
    }

    //Finds an image on screen and taps on it. Method will cause a FAIL if the image is not found.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "xOffset" and "yOffset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    // "taps" sets the number of taps that are performed.
    // "frequency" sets the frequency of the taps.
    private void multipleTapImageOnScreen(String image, int retries, int taps, double frequency,
                                          double xOffset, double yOffset) throws InterruptedException, IOException {
        Point[] imgRect = findImageOnScreen(image, retries);
        Point topLeft = imgRect[0];
        Point topRight = imgRect[1];
        Point bottomLeft = imgRect[2];
        Point center = imgRect[4];
        //imgRect[4] will have the center of the rectangle containing the image

        //noinspection MagicNumber,MagicNumber
        if ((xOffset == 0.5) && (yOffset == 0.5)) {

            log("Coordinates are: " + center.x + "," + center.y);

            for (int i = 0; i < taps; i++) {
                tapAtCoordinates((int) center.x, (int) center.y);
                sleep(frequency);
            }
        } else {
            double newX = topLeft.x + (topRight.x - topLeft.x) * xOffset;
            double newY = topLeft.y + (bottomLeft.y - topLeft.y) * yOffset;

            for (int i = 0; i < taps; i++) {
                log("Coordinates with offset are: " + newX + ", " + newY);
                tapAtCoordinates((int) newX, (int) newY);
                sleep(frequency);
            }
        }
    }

    public void multipleTapImageOnScreen(String image, int taps, double frequency, double xOffset, double yOffset) throws InterruptedException, IOException {
        multipleTapImageOnScreen(image, DEFAULT_RETRIES, taps, frequency, xOffset, yOffset);
    }

    public void multipleTapImageOnScreen(String image, int retries, int taps, double frequency) throws InterruptedException, IOException {
        //noinspection MagicNumber,MagicNumber
        multipleTapImageOnScreen(image, retries, taps, frequency, 0.5, 0.5);
    }

    public void multipleTapImageOnScreen(String image, int taps, double frequency) throws InterruptedException, IOException {
        //noinspection MagicNumber,MagicNumber
        multipleTapImageOnScreen(image, DEFAULT_RETRIES, taps, frequency, 0.5, 0.5);
    }

    public void multipleTapImageOnScreen(String image, int taps) throws InterruptedException, IOException {
        //noinspection MagicNumber,MagicNumber
        multipleTapImageOnScreen(image, DEFAULT_RETRIES, taps, 1, 0.5, 0.5);
    }

    //Finds an image on screen and taps on it. Method will NOT cause a FAIL if the image is not found.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "xOffset" and "yOffset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    private boolean tryTapImageOnScreen(String image, double xOffset, double yOffset, int retries) throws IOException, InterruptedException {
        Point[] imgRect = findImageOnScreenNoAssert(image, retries);

        if (imgRect == null) {
            return false;
        } else {
            Point topLeft = imgRect[0];
            Point topRight = imgRect[1];
            Point bottomLeft = imgRect[2];
            Point center = imgRect[4];

            //noinspection MagicNumber,MagicNumber
            if ((xOffset == 0.5) && (yOffset == 0.5)) {
                log("Coordinates are: " + center.x + "," + center.y);
                tapAtCoordinates((int) center.x, (int) center.y);
            } else {
                double newX = topLeft.x + (topRight.x - topLeft.x) * xOffset;
                double newY = topLeft.y + (bottomLeft.y - topLeft.y) * yOffset;
                tapAtCoordinates((int) newX, (int) newY);
            }
            return true;
        }
    }

    public boolean tryTapImageOnScreen(String image, double xOffset, double yOffset) throws IOException, InterruptedException {
        return tryTapImageOnScreen(image, xOffset, yOffset, DEFAULT_RETRIES);
    }

    public boolean tryTapImageOnScreen(String image, int retries) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        return tryTapImageOnScreen(image, 0.5, 0.5, retries);
    }

    public boolean tryTapImageOnScreen(String image) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        return tryTapImageOnScreen(image, 0.5, 0.5, DEFAULT_RETRIES);
    }

    // Finds an image on screen and taps and hold on it for a specified duration.
    // "duration" is given in seconds
    // "withAssert" specifies if the method will return a fail or not if the image is not found.
    private void tapAndHoldImageOnScreen(String image, double xOffset, double yOffset,
                                         int duration, boolean withAssert) throws InterruptedException, IOException {

        Point[] imgRect = withAssert ? findImageOnScreen(image) : findImageOnScreenNoAssert(image);

        Point topLeft = imgRect[0];
        Point topRight = imgRect[1];
        Point bottomLeft = imgRect[2];

        double newX = topLeft.x + (topRight.x - topLeft.x) * xOffset;
        double newY = topLeft.y + (bottomLeft.y - topLeft.y) * yOffset;

        log("Coordinates with offset are: " + newX + ", " + newY);

        if ("selendroid".equalsIgnoreCase(automationName)) {
            selendroidTapAtCoordinate((int) newX, (int) newY, duration);
        } else {
            driver.tap(1, (int) newX, (int) newY, duration);
        }
    }

    public void tapAndHoldImageOnScreen(String image, double xOffset, double yOffset, int duration) throws IOException, InterruptedException {
        tapAndHoldImageOnScreen(image, xOffset, yOffset, duration, DEFAULT_WITH_ASSERT);
    }

    public void tapAndHoldImageOnScreen(String image, int duration, boolean withAssert) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        tapAndHoldImageOnScreen(image, 0.5, 0.5, duration, withAssert);
    }

    public void tapAndHoldImageOnScreen(String image, int duration) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        tapAndHoldImageOnScreen(image, 0.5, 0.5, duration, DEFAULT_WITH_ASSERT);
    }

    //Taps and holds on relative coordinates on the screen.
    public void tapAndHoldOnScreen(double xOffset, double yOffset, int duration) throws InterruptedException {
        int duration1 = duration;

        duration1 *= 1000;
        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * xOffset, middle.y * yOffset);

        log("Coordinates with offset are: " + middleWithOffset.x + ", " + middleWithOffset.y);

        if ("selendroid".equalsIgnoreCase(automationName)) {
            selendroidTapAtCoordinate((int) middleWithOffset.x, (int) middleWithOffset.y, duration1);
        } else {
            driver.tap(1, (int) middleWithOffset.x, (int) middleWithOffset.y, duration1);
        }
    }

    /**
     * =====================================================================================
     * SWIPE GESTURES
     * =====================================================================================
     */

    //Performs a vertical swipe on the screen, starting from a searched image.
    //"distance" is given in pixels. A positive "distance" will perform a swipe down, a negative "distance" will perform a swipe up.
    //if "xOffset" and "yOffset" are used, the swipe will start from a relative coordinate of that image. If not used, the swipe will start from the center of the image.
    private void swipeVerticallyOnImage(String image, int distance, double xOffset,
                                        double yOffset) throws InterruptedException, IOException {
        Point[] imgRect = findImageOnScreen(image);

        int startX = (int) (imgRect[0].x + (imgRect[1].x - imgRect[0].x) * xOffset);
        int startY = (int) (imgRect[0].y + (imgRect[2].y - imgRect[0].y) * yOffset);
        int endX = startX;
        int endY = startY + distance;

        if ("iOS".equalsIgnoreCase(platformName)) {
            iOSSwipe(startX, startY, endX, endY);
        } else {
            androidSwipe(startX, startY, endX, endY);
        }
    }

    public void swipeVerticallyOnImage(String image, int distance) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        swipeVerticallyOnImage(image, distance, 0.5, 0.5);
    }

    //Performs a horizontal swipe on the screen, starting from a searched image.
    //"distance" is given in pixels. A positive "distance" will perform a swipe to the right, a negative "distance" will perform a swipe to the left.
    //if "xOffset" and "yOffset" are used, the swipe will start from a relative coordinate of that image. If not used, the swipe will start from the center of the image.
    private void swipeHorizontallyOnImage(String image, int distance, double xOffset,
                                          double yOffset) throws InterruptedException, IOException {
        Point[] imgRect = findImageOnScreen(image);

        int startX = (int) (imgRect[0].x + (imgRect[1].x - imgRect[0].x) * xOffset);
        int startY = (int) (imgRect[0].y + (imgRect[2].y - imgRect[0].y) * yOffset);
        int endX = startX + distance;
        int endY = startY;

        if ("iOS".equalsIgnoreCase(platformName)) {
            iOSSwipe(startX, startY, endX, endY);
        } else {
            androidSwipe(startX, startY, endX, endY);
        }
    }

    public void swipeHorizontallyOnImage(String image, int distance) throws IOException, InterruptedException {
        //noinspection MagicNumber,MagicNumber
        swipeHorizontallyOnImage(image, distance, 0.5, 0.5);
    }


    //Performs a horizontal swipe starting from a relative coordinate of the device screen.
    //"distance" is given in pixels. A positive "distance" will perform a swipe to the right, a negative "distance" will perform a swipe to the left.
    //"swipes" sets the number of swipes that are performed.
    //"frequency" sets the frequency of the swipes.
    public void swipeHorizontally(double xOffset, double yOffset, int distance, int swipes, double frequency) throws InterruptedException {

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * xOffset, middle.y * yOffset);

        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX + distance;
        int endY = startY;


        for (int i = 0; i < swipes; i++) {
            if ("iOS".equalsIgnoreCase(platformName)) {
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
    public void swipeHorizontally(double xOffset, double yOffset, double distance, int swipes, double frequency) throws InterruptedException { //positive distance for swipe right, negative for swipe left.

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * xOffset, middle.y * yOffset);
        double relativeDistance = middle.x * distance;

        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX + (int) Math.floor(relativeDistance);
        int endY = startY;


        for (int i = 0; i < swipes; i++) {
            if ("iOS".equalsIgnoreCase(platformName)) {
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
    public void swipeVertically(double xOffset, double yOffset, int distance, int swipes, double frequency) throws InterruptedException {

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * xOffset, middle.y * yOffset);

        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX;
        int endY = startY + distance;


        for (int i = 0; i < swipes; i++) {
            if ("iOS".equalsIgnoreCase(platformName)) {
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
    public void swipeVertically(double xOffset, double yOffset, double distance, int swipes, double frequency) throws InterruptedException {

        Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth(), size.getHeight());
        Point middleWithOffset = new Point(middle.x * xOffset, middle.y * yOffset);
        double relativeDistance = middle.y * distance;


        int startX = (int) middleWithOffset.x;
        int startY = (int) middleWithOffset.y;
        int endX = startX;
        int endY = startY + (int) Math.floor(relativeDistance);


        for (int i = 0; i < swipes; i++) {
            if ("iOS".equalsIgnoreCase(platformName)) {
                iOSSwipe(startX, startY, endX, endY);
            } else {
                androidSwipe(startX, startY, endX, endY);
            }
            sleep(frequency);
        }
        log("Finished executing swipes");
    }


    private static void androidSwipe(int startX, int startY, int endX, int endY) throws InterruptedException {
        TouchActions actions = new TouchActions(driver);

        actions.down(startX, startY).perform();
        //noinspection MagicNumber
        sleep(0.5);
        actions.move(endX, endY).perform();
        actions.up(endX, endY).perform();
    }

    private static void iOSSwipe(int startX, int startY, int endX, int endY){
        TouchAction action = new TouchAction(driver);

        action.press(startX, startY);
        action.waitAction(1000);  //has to be >= 500 otherwise it will fail
        action.moveTo(endX, endY);
        action.release();
        action.perform();
    }


    public void swipe(double startX, double startY, double endX, double endY) throws InterruptedException {
        TouchActions actions = new TouchActions(driver);
        Dimension size = driver.manage().window().getSize();

        Point screen = new Point(size.getWidth(), size.getHeight());
        Point swipeStart = new Point(screen.x * startX, screen.y * startY);
        Point swipeEnd = new Point(screen.x * endX, screen.y * endY);

        if ("Android".equalsIgnoreCase(platformName)) {
            androidSwipe((int) swipeStart.x, (int) swipeStart.y, (int) swipeEnd.x, (int) swipeEnd.y);
        } else {
            iOSSwipe((int) swipeStart.x, (int) swipeStart.y, (int) swipeEnd.x, (int) swipeEnd.y);
        }
    }

    /**
     * ======================================================================================
     * DRAG AND DROP
     * ======================================================================================
     */

    //Performs a drag and drop from the middle of the "object" image to the middle of the "location" image.
    public void dragFromOneImageToAnother(String object, String location) throws InterruptedException, IOException {

        Point[] objectCoord = findImageOnScreen(object);
        Point[] locationCoord = findImageOnScreen(location, false);

        int startX = (int) objectCoord[4].x;
        int startY = (int) objectCoord[4].y;

        int endX = (int) locationCoord[4].x;
        int endY = (int) locationCoord[4].y;

        if ("selendroid".equalsIgnoreCase(automationName)) {
            androidSwipe(startX, startY, endX, endY);
        } else {
            iOSSwipe(startX, startY, endX, endY);
        }
    }


    //Performs a drag and drop from the middle of the "object" image to the middle of the screen.
    public void dragImageToMiddle(String object) throws InterruptedException, IOException {

        Dimension size = driver.manage().window().getSize();
        @SuppressWarnings("MagicNumber") Point middle = new Point(size.getWidth() / 2 - 20, size.getHeight() / 2 + 20);

        Point[] objectCoord = findImageOnScreen(object, 10);

        int startX = (int) objectCoord[4].x;
        int startY = (int) objectCoord[4].y;

        int endX = (int) middle.x;
        int endY = (int) middle.y;

        if ("selendroid".equalsIgnoreCase(automationName)) {
            androidSwipe(startX, startY, endX, endY);
        } else {
            iOSSwipe(startX, startY, endX, endY);
        }
    }

    //Performs a drag and drop from the middle of the "object" image to the specified relative coordinates of the screen.
    public void dragImageToCoordinates(String object, double xOffset, double yOffset) throws InterruptedException, IOException {

        Dimension size = driver.manage().window().getSize();
        Point screen = new Point(size.getWidth(), size.getHeight());
        Point swipeEnd = new Point(screen.x * xOffset, screen.y * yOffset);

        Point[] objectCoord = findImageOnScreen(object, 10);

        int startX = (int) objectCoord[4].x;
        int startY = (int) objectCoord[4].y;

        int endX = (int) swipeEnd.x;
        int endY = (int) swipeEnd.y;

        if ("selendroid".equalsIgnoreCase(automationName)) {
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
    private static Dimension getScreenSizeADB() {
        log("trying to get size from adb...");
        log("------------------------------");
        if ("iOS".equalsIgnoreCase(platformName)) {
            return driver.manage().window().getSize();
        }
        try {
            String adb = "adb";
            String[] adbCommand = {adb, "shell", "dumpsys", "window"};
            ProcessBuilder p = new ProcessBuilder(adbCommand);
            Process proc = p.start();
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            String[] size = null;
            while (line != null) {
                if (!line.contains("OriginalmUnrestrictedScreen")) { //we do this check for devices with android 5.x+ The adb command returns an extra line with the values 0x0 which must be filtered out.
                    if (line.contains("mUnrestrictedScreen")) {
                        String[] tmp = line.split("\\) ");
                        size = tmp[1].split("x");
                    }
                }
                line = br.readLine();
            }
            int width = Integer.parseInt(size[0]);
            int height = Integer.parseInt(size[1]);
            Dimension screenSize = new Dimension(width, height);
            return screenSize;

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public static boolean findDeviceTypeADB(){
        log("trying to find device type ...");
        log("------------------------------");
        if ("iOS".equalsIgnoreCase(platformName)) {
            //TO Be added
        } else {
            try {
                String adb = "adb";
                String[] adbCommand = {adb, "shell", "getprop", "ro.build.characteristics"};
                ProcessBuilder p = new ProcessBuilder(adbCommand);
                Process proc = p.start();
                InputStream stdin = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                String[] size = null;
                while (line != null) {
                    if (line.contains("tablet")) {
                        return true;
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return false;
    }

    //Uses adb commands to tap at relative coordinates. To be used when appium methods fail. Only works on Android devices.
    public void tapAtRelativeCoordinatesADB(double xOffset, double yOffset) throws InterruptedException {
        if ("iOS".equalsIgnoreCase(platformName)) {
            tapAtRelativeCoordinates(xOffset, yOffset);
        } else {
            Dimension size = getScreenSizeADB();
            log("Size of device as seen by ADB is - width: " + size.width + " height: " + size.height);
            String x = String.valueOf(size.width * xOffset);
            String y = String.valueOf(size.height * yOffset);
            log("ADB: x and y: " + x + ", " + y);
            //            String[] adbCommand = {"adb", "shell", "input", "touchscreen", "swipe", x, y, x, y, "2000"};

            try {
                String[] adbCommand = {"adb", "shell", "input", "tap", x, y};
                ProcessBuilder p = new ProcessBuilder(adbCommand);
                Process proc = p.start();
                InputStream stdin = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                while (line != null) {
                    System.out.print(line);
                    line = br.readLine();
                }
                proc.waitFor();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    //Uses adb commands to tap at coordinates. To be used when appium methods fail. Only works on Android devices.
    public void tapAtCoordinatesADB(double x, double y) throws InterruptedException {
        if ("iOS".equalsIgnoreCase(platformName)) {
            tapAtCoordinates((int) x, (int) y);
        } else {
            int Xx = (int) x;
            int Yy = (int) y;
            String X = String.valueOf(Xx);
            String Y = String.valueOf(Yy);
            log("ADB: X: " + X + ", Y: " + Y);
//                        String[] adbCommand = {"adb", "shell", "input", "tap", X, Y};

            String[] adbCommand;
            if ("selendroid".equalsIgnoreCase(automationName)) {
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

    private static void processBuilder(String[] adbCommand) {
        try {
            ProcessBuilder p = new ProcessBuilder(adbCommand);
            Process proc = p.start();
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
                System.out.print(line);
                line = br.readLine();
            }

            proc.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * ======================================================================================
     * CROP IMAGE
     * ======================================================================================
     */

    private void findAndCropImageFromScreen(String image) throws InterruptedException, IOException {
        //noinspection MagicNumber
        findImageOnScreen(image, 3, DEFAULT_RETRY_WAIT, 0.60, true, true, true);
    }

    /**
     * ======================================================================================
     * TESSERACT GRAB TEXT FROM IMAGE
     * ======================================================================================
     */

    public String grabText(String image) throws IOException, InterruptedException {
        findAndCropImageFromScreen(image);
        String imageInput = screenshotsFolder + getScreenshotsCounter() + "_" + image + "_screenshot" + getRetryCounter() + "_" + timeDifferenceStartTest + "sec" + ".png";
        String value = "";
        try {
            String[] tesseractCommand = {"tesseract", imageInput, "stdout"};
            ProcessBuilder p = new ProcessBuilder(tesseractCommand);
            Process proc = p.start();
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            String[] size = null;
            String[] splitLines;
            while (line != null) {
                value += line;
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
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
    public Point correctAndroidCoordinates(Point appiumCoord){

        Dimension appiumDimensions = driver.manage().window().getSize();
        int appiumScreenWidth = appiumDimensions.getWidth();
        int appiumScreenHeight = appiumDimensions.getHeight();

        Dimension adbDimension = getScreenSizeADB();
        int adbScreenWidth = adbDimension.getWidth();
        int adbScreenHeight = adbDimension.getHeight();

        double xOffset = appiumCoord.x / appiumScreenWidth;
        double yOffset = appiumCoord.y / appiumScreenHeight;
        log("xOffset is : " + xOffset);
        log("yOffset is : " + yOffset);

        return new Point(xOffset * adbScreenWidth, yOffset * adbScreenHeight);
    }

    public void cancelGameCenterLogin() throws IOException, InterruptedException {
        log("Check to see if Google Plus Sign in needs to be cancelled..");
        if (driver.getPageSource().contains("Choose an account")) {
            if ("selendroid".equalsIgnoreCase(automationName)) {
                driver.findElement(By.xpath("//LinearLayout/Button[@text='Cancel']")).click();
            } else if ("android".equalsIgnoreCase(platformName)) {
                driver.findElement(By.xpath("//android.widget.Button[@text='Cancel']")).click();
            } else { //we are on ios
                driver.findElementByAccessibilityId("Cancel").click();
            }
        }
        if ("selendroid".equalsIgnoreCase(automationName)) {
            while (findImageOnScreenNoAssert("native_sign_in_button") != null) {
                //noinspection MagicNumber,MagicNumber
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

    public static void gameCenterLogin() throws IOException, InterruptedException {
        if (driver.getPageSource().contains("Sign in")) {
            log("Google Plus sign in shown...");
            log(driver.getPageSource());
            takeScreenshot("google_plus_sign_in_shown");
            while (driver.getPageSource().contains("Sign in"))
                if ("selendroid".equalsIgnoreCase(automationName)) {
                    driver.findElement(By.xpath("//LinearLayout/Button[@text='Sign in']")).click();
                } else if ("android".equalsIgnoreCase(platformName)) {
                    driver.findElement(By.xpath("//android.widget.Button[@text='Sign in']")).click();
                } else { //we are on ios
                    driver.findElementByAccessibilityId("Sign In").click();
                }
            takeScreenshot("after_clicking_sign_in");
        }
    }

    public void dismissFullScreenMessage() throws IOException, InterruptedException {
        log("Trying to dismiss Full Screen notification message if it shows up...");
        //noinspection MagicNumber,MagicNumber
        tryTapImageOnScreen("full_screen_1", 0.75, 0.75, 2);
        //noinspection MagicNumber,MagicNumber
        tryTapImageOnScreen("full_screen_2", 0.75, 0.75, 1);
        //noinspection MagicNumber,MagicNumber
        tryTapImageOnScreen("full_screen_3", 0.75, 0.8, 1);
    }

}
