import com.testdroid.appium.TestdroidAppiumClient;
import com.testdroid.appium.TestdroidAppiumDriverIos;
import io.appium.java_client.TouchAction;
import org.opencv.core.Point;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

/**
 * Testdroid Image Recognition
 *
 * https://git@github.com/bitbar/testdroid-samples
 *
 * Usage:
 *
 * @TODO
 *
 * @author support@bitbar.com
 */


public class TestdroidImageRecognitionIos {
    public static TestdroidAppiumDriverIos driver;
    public Logger logger = LoggerFactory.getLogger(TestdroidImageRecognitionIos.class);
    public static int counter = 0;
    public static final int SHORT_SLEEP = 2;
    public static final int LONG_SLEEP = 10;
    public String rotation = "notSet";
    public static String platformName;
    public static String deviceName;
    public static String automationName;

    public static TestdroidAppiumDriverIos getDriverIos(TestdroidAppiumClient client) throws Exception {
        // Wait one hour for free device
        client.setDeviceWaitTime(3600);
        client.setPlatformName(client.APPIUM_PLATFORM_IOS);

        // Initiate the webdriver. Capabilities must be set by now.
        driver = client.getIOSDriver();
        deviceName = client.getDeviceName();
        platformName = client.getPlatformName();

        if (client.getTestdroidTarget() == null) {
            automationName = client.getAutomationName();
        } else {
            automationName = client.getTestdroidTarget();
        }
          return driver;
    }

    public String getScreenshotsFolder() {
        return "target/reports/screenshots/ios/";
    }

    public String getScreenshotsCounter() {
        if (counter < 10) {
            return "0" + counter;
        } else {
            return String.valueOf(counter);
        }
    }

    public void takeScreenshot(String screenshotName) throws Exception {
        counter = counter + 1;
        String fullFileName = System.getProperty("user.dir") + "/" + getScreenshotsFolder() + getScreenshotsCounter() + "_" + screenshotName + ".png";
        driver.takeScreenshot(fullFileName);
    }

    public Point[] findImage(String image, String scene, String setRotation) {
        return findImage(image, scene, setRotation, 0.6);
    }

    public Point[] findImage(String image, String scene, String setRotation, double tolerance) {
        log("Image to find: queryimages/" + image + " in " + getScreenshotsFolder() + getScreenshotsCounter() + "_" + scene);
        AkazeImageFinder imageFinder = new AkazeImageFinder(rotation);

        Point[] imgRect = new Point[0];


        try {
//            imgRect = imageFinder.findImage("queryimages/" + screenshotsFolder + "/" + image, getScreenshotsFolder() + getScreenshotsCounter() + "_" + scene);
            imgRect = imageFinder.findImage("queryimages/" + image, getScreenshotsFolder() + getScreenshotsCounter() + "_" + scene, tolerance);

        } catch (Exception e) {
            e.printStackTrace();
        }
        rotation = imageFinder.getRotation();

        if (imgRect != null) {
            //for retina devices we need to recalculate coordinates

            org.openqa.selenium.Dimension size = driver.manage().window().getSize();
            logger.info("Device screen size as seen by Appium is: " + size.toString());

            double sceneHeight = imageFinder.getSceneHeight();
            double sceneWidth = imageFinder.getSceneHeight();

            int screenHeight = size.getHeight();
            int screenWidth = size.getWidth();

            if (sceneHeight > screenWidth) {
                double temp = sceneHeight;
                sceneHeight = sceneWidth;
                sceneWidth = temp;
            }

            if (screenHeight > screenWidth) {
                int temp = screenHeight;
                screenHeight = screenWidth;
                screenWidth = temp;
            }

            if ((screenHeight<sceneHeight)&&(screenWidth<sceneWidth)&&platformName==TestdroidAppiumClient.APPIUM_PLATFORM_IOS) {
                if ((screenHeight<sceneHeight/2)&&(screenWidth<sceneWidth/2)) {
                    Point[] imgRectScaled = new Point[]{new Point(imgRect[0].x / 3, imgRect[0].y / 3), new Point(imgRect[1].x / 3, imgRect[1].y / 3), new Point(imgRect[2].x / 3, imgRect[2].y / 3), new Point(imgRect[3].x / 3, imgRect[3].y / 3), new Point(imgRect[4].x / 3, imgRect[4].y / 3)};
                    log("Device with Retina display rendered at x3 => coordinates have been recalculated");
                    return imgRectScaled;
                }
                else {
                    Point[] imgRectScaled = new Point[]{new Point(imgRect[0].x / 2, imgRect[0].y / 2), new Point(imgRect[1].x / 2, imgRect[1].y / 2), new Point(imgRect[2].x / 2, imgRect[2].y / 2), new Point(imgRect[3].x / 2, imgRect[3].y / 2), new Point(imgRect[4].x / 2, imgRect[4].y / 2)};
                    log("Device with Retina display rendered at x2 => coordinates have been recalculated");
                    return imgRectScaled;
                }
            } else {

                Point center = imgRect[4];

                if ((center.x >= size.width) || (center.x < 0) || (center.y >= size.height) || (center.y < 0)) {
                    log("Screen size is (width, height): " + size.getWidth() + ", " + size.getHeight());
                    log("WARNING: Coordinates found do not match the screen --> image not found.");
                    //     return imgRect;
                }
                return imgRect;

            }
        } else {
            return null;
        }
    }

    public Point[] findImage(String image, String scene) {
        return findImage(image, scene, rotation);
    }

    public void tapImage(String image, String scene) throws Exception {
        Point[] imgRect = findImage(image, scene);
        //imgRect[4] will have the center of the rectangle containing the image

        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);
        } else {
            driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
        }
        sleep(SHORT_SLEEP);
    }

    public void tapMiddle() throws Exception {
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth() / 2, size.getHeight() / 2);

        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate((int) middle.x, (int) middle.y, 1);
        } else {
            driver.tap(1, (int) middle.x, (int) middle.y, 1);
        }
        sleep(SHORT_SLEEP);
    }

    public void tapAtCoordinates(int x, int y) throws Exception {
        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate(x, y, 1);
        } else {
            driver.tap(1, x, y, 1);
        }
        sleep(SHORT_SLEEP);
    }

    public void selendroidTapAtCoordinate(int x, int y, int secs) throws Exception {
        TouchActions actions = new TouchActions(driver);
        actions.down(x, y).perform();
        sleep(secs);
        actions.up(x, y).perform();
    }

    public void tapImageOnScreen(String image) throws Exception {
        Point[] imgRect = findImageOnScreen(image);
        //imgRect[4] will have the center of the rectangle containing the image

        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);
        } else {
            driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
        }
        sleep(SHORT_SLEEP);
    }

    public void tapImageOnScreen(String image, int retries) throws Exception {
        Point[] imgRect = findImageOnScreen(image, retries);
        //imgRect[4] will have the center of the rectangle containing the image

        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);

        } else {
            driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
        }
    }

    public void tapImageOnScreen(String image, double x_offset, double y_offset) throws Exception {
        Point[] imgRect = findImageOnScreen(image);
        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];

        //adding the offset to each coordinate; if offset = 0.5, middle will be returned
        double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
        double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate((int) newX, (int) newY, 1);
        } else {
            driver.tap(1, (int) newX, (int) newY, 1);
        }
    }

    public void tapImageOnScreen(String image, double x_offset, double y_offset, int retries) throws Exception {
        tapImageOnScreen(image, x_offset, y_offset, retries, 0.6);
    }

    public void tapImageOnScreen(String image, double x_offset, double y_offset, int retries, double tolerance) throws Exception {
        Point[] imgRect = findImageOnScreen(image, retries, tolerance);
        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];

        //adding the offset to each coordinate; if offset = 0.5, middle will be returned
        double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
        double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate((int) newX, (int) newY, 1);
        } else {
            driver.tap(1, (int) newX, (int) newY, 1);
        }
    }

    public void tapImageOnScreenTwice(String image, double x_offset, double y_offset) throws Exception {
        Point[] imgRect = findImageOnScreen(image);
        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];

        //adding the offset to each coordinate; if offset = 0.5, middle will be returned
        double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
        double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

        if (automationName.equals("selendroid")) {
            selendroidTapAtCoordinate((int) newX, (int) newY, 1);
            sleep(SHORT_SLEEP);
            selendroidTapAtCoordinate((int) newX, (int) newY, 1);
        } else {
            driver.tap(1, (int) newX, (int) newY, 1);
            sleep(SHORT_SLEEP);
            driver.tap(1, (int) newX, (int) newY, 1);
        }
    }

    public boolean tryTapImageOnScreen(String image) throws Exception {
        Point[] imgRect = findImageOnScreenNoAssert(image);

        if (imgRect == null) {
            return false;
        } else {
            if (automationName.equals("selendroid")) {
                selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);
            } else {
                driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
            }
            sleep(SHORT_SLEEP);
            return true;
        }
    }

    public boolean tryTapImageOnScreen(String image, int retries) throws Exception {
        Point[] imgRect = findImageOnScreenNoAssert(image, retries);

        if (imgRect == null) {
            return false;
        } else {
            if (automationName.equals("selendroid")) {
                selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);
            } else {
                driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
            }
            sleep(SHORT_SLEEP);
            return true;
        }
    }

    public boolean tryTapImageOnScreen(String image, double x_offset, double y_offset, int retries) throws Exception {
        Point[] imgRect = findImageOnScreenNoAssert(image, retries);

        if (imgRect == null) {
            return false;
        } else {
            Point top_left = imgRect[0];
            Point top_right = imgRect[1];
            Point bottom_left = imgRect[2];

            //adding the offset to each coordinate; if offset = 0.5, middle will be returned
            double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
            double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

            if (automationName.equals("selendroid")) {
                selendroidTapAtCoordinate((int) newX, (int) newY, 1);
            } else {
                driver.tap(1, (int) newX, (int) newY, 1);
            }
            return true;
        }
    }

    public Point[] findImageOnScreen(String image) throws Exception {
        int retries = 5;
        Point[] imgRect = null;

        while ((retries > 0) && (imgRect == null)) {
            if (retries < 5) {
                log("Find image failed, retries left: " + retries);
            }
            takeScreenshot(image + "_screenshot");
            imgRect = findImage(image, image + "_screenshot");
            retries = retries - 1;
        }
        assertNotNull("Image " + image + " not found on screen.", imgRect);
        //assertNotNull(imgRect);
        return imgRect;
    }

    public Point[] findImageOnScreen(String image, int retries) throws Exception {
        return findImageOnScreen(image, retries, 0.6);
    }

    public Point[] findImageOnScreen(String image, int retries, double tolerance) throws Exception {
        Point[] imgRect = null;

        while ((retries > 0) && (imgRect == null)) {
            log("Find image started, retries left: " + retries);
            takeScreenshot(image + "_screenshot");
            imgRect = findImage(image, image + "_screenshot", rotation, tolerance);
            retries = retries - 1;
        }
        assertNotNull("Image " + image + " not found on screen.", imgRect);
        return imgRect;
    }

    public Point[] findImageOnScreenNoAssert(String image) throws Exception {
        int retries = 5;
        Point[] imgRect = null;

        while ((retries > 0) && (imgRect == null)) {
            if (retries < 5) {
                log("Find image failed, retries left: " + retries);
            }
            takeScreenshot(image + "_screenshot");
            imgRect = findImage(image, image + "_screenshot");
            retries = retries - 1;
        }
        return imgRect;
    }

    public Point[] findImageOnScreenNoAssert(String image, int retries) throws Exception {
        Point[] imgRect = null;

        while ((retries > 0) && (imgRect == null)) {
            if (retries < 5) {
                log("Find image failed, retries left: " + retries);
            }
            takeScreenshot(image + "_screenshot");
            imgRect = findImage(image, image + "_screenshot");
            retries = retries - 1;
        }
        return imgRect;
    }

    public Point[] findImageOnScreenAndSetRotation(String image) throws Exception {
        //used to initially determine if the screenshots need to be rotated and by what degree
        int retries = 5;
        Point[] imgRect = null;

        while ((retries > 0) && (imgRect == null)) {
            if (retries < 5) {
                log("Find image failed, retries left: " + retries);
            }
            takeScreenshot(image + "_screenshot");
            imgRect = findImage(image, image + "_screenshot", "notSet"); //this will identify the rotation initially
            retries = retries - 1;
        }
        assertNotNull("Image " + image + " not found on screen.", imgRect);
        return imgRect;
    }

    public void swipeScreenWithImage(String image) throws Exception {
        swipeScreenWithImage(image, 1);
    }

    public void swipeScreenWithImage(String image, int repeats) throws Exception {
        //Point[] imgRect = findImageOnScreen(image, 10);
        sleep(SHORT_SLEEP);
        Point[] imgRect = findImageOnScreen(image, 10, 0.3);
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();

        if (automationName.equals("selendroid")) {
            TouchActions action = new TouchActions(driver);
            action.down((int) imgRect[4].x, (int) imgRect[4].y).perform();

            double left_x = size.getWidth() * 0.20;
            double right_x = size.getWidth() * 0.80;
            double top_y = size.getHeight() * 0.20;

            //we will repeat the swiping based on "repeats" argument
            while (repeats > 0) {
                log("Swiping screen with image in progress..");
                action.move((int) left_x, (int) top_y).perform();
                sleep(SHORT_SLEEP);
                //swiping horizontally
                int i = 1;
                while (top_y + i * 10 < size.getHeight() * 0.9) {
                    action.move((int) right_x, (int) (top_y + i * 10)).perform();
                    Thread.sleep(50);
                    action.move((int) left_x, (int) (top_y + i * 10)).perform();
                    Thread.sleep(50);
                    i = i + 1;
                }
                //swiping vertically
                i = 1;
                action.move((int) left_x, (int) top_y);
                while (left_x + i * 10 < right_x) {
                    action.move((int) (left_x + i * 10), (size.getHeight() - 1));
                    Thread.sleep(50);
                    action.move((int) (left_x + i * 10), (int) top_y);
                    Thread.sleep(50);
                    i = i + 1;
                }
                repeats = repeats - 1;
            }
            action.up(0, 0).perform();
        } else {
            TouchAction action = new TouchAction(driver);
            action.press((int) imgRect[4].x, (int) imgRect[4].y).perform();

            double left_x = size.getWidth() * 0.20;
            double right_x = size.getWidth() * 0.80;
            double top_y = size.getHeight() * 0.20;

            //we will repeat the swiping based on "repeats" argument
            while (repeats > 0) {
                log("Swiping screen with image in progress..");
                action.moveTo((int) left_x, (int) top_y).perform();
                sleep(SHORT_SLEEP);
                //swiping horizontally
                int i = 1;
                while (top_y + i * 20 < size.getHeight() * 0.9) {
                    action.moveTo((int) right_x, (int) (top_y + i * 20)).perform();
                    Thread.sleep(50);
                    action.moveTo((int) left_x, (int) (top_y + i * 20)).perform();
                    Thread.sleep(50);
                    i = i + 1;
                }
                //swiping vertically
                i = 1;
                action.moveTo((int) left_x, (int) top_y);
                while (left_x + i * 20 < right_x) {
                    action.moveTo((int) (left_x + i * 20), size.getHeight() - 1);
                    Thread.sleep(50);
                    action.moveTo((int) (left_x + i * 20), (int) top_y);
                    Thread.sleep(50);
                    i = i + 1;
                }
                repeats = repeats - 1;
            }
            action.release().perform();
        }
    }

    public void dragImage(String image, double x_offset, double y_offset) throws Exception {
        //drags image on screen using x and y offset from middle of the screen
        //0.5 offset => middle point
        Point[] imgRect = findImageOnScreen(image, 10);
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();
        Point point = new Point(size.getWidth() * x_offset, size.getHeight() * y_offset);
        log("Dragging image to coordinates: " + point.toString());

        if (automationName.equals("selendroid")) {
            TouchActions action = new TouchActions(driver);
            action.down((int) imgRect[4].x, (int) imgRect[4].y).perform();
            sleep(SHORT_SLEEP);
            action.move((int) point.x, (int) point.y).perform();
            action.up((int) point.x, (int) point.y).perform();
        } else {
            TouchAction action = new TouchAction(driver);
            action.press((int) imgRect[4].x, (int) imgRect[4].y).perform();
            sleep(SHORT_SLEEP);
            action.moveTo((int) point.x, (int) point.y).perform();
            action.release().perform();
        }
    }

    public void cancelGameCenterLogin() throws Exception {
        log("Check to see if Google Plus Sign in needs to be cancelled..");
        if (driver.getPageSource().contains("Choose an account")) {
            if (automationName.equals("selendroid")) {
                driver.findElement(By.xpath("//LinearLayout/Button[@text='Cancel']")).click();
            } else if (automationName.equals("android")) {
                driver.findElement(By.xpath("//android.widget.Button[@text='Cancel']")).click();
            } else { //we are on ios
                driver.findElementByAccessibilityId("Cancel").click();
            }
        }
        if (automationName.equals("selendroid")) {
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
                if (automationName.equals("selendroid")) {
                    driver.findElement(By.xpath("//LinearLayout/Button[@text='Sign in']")).click();
                } else if (automationName.equals("android")) {
                    driver.findElement(By.xpath("//android.widget.Button[@text='Sign in']")).click();
                } else { //we are on ios
                    driver.findElementByAccessibilityId("Sign In").click();
                }
            takeScreenshot("after_clicking_sign_in");
        }
    }

    public void dismissFullScreenMessage() throws Exception {
        log("Trying to dismiss Full Screen notification message if it shows up...");
        tryTapImageOnScreen("full_screen_1", 0.75, 0.75, 1);
        tryTapImageOnScreen("full_screen_2", 0.75, 0.75, 1);
    }

    public void sleep(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }

    public void log(String message) {
        logger.info(message);
    }
}
