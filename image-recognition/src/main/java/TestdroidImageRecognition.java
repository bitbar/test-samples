import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.testdroid.appium.TestdroidAppiumClient;
import com.testdroid.appium.TestdroidAppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.opencv.core.Point;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

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

public class TestdroidImageRecognition {


    public String platform;
    public String deviceName;
    public String automationName;
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static TestdroidAppiumClient client;
    public static TestdroidAppiumDriver driver;
    public Logger logger = LoggerFactory.getLogger(TestdroidImageRecognition.class);
    public static String screenshotsFolder = "";
    public int counter;
    public static final int SHORT_SLEEP = 2;
    public static final int LONG_SLEEP = 10;
    public String rotation;


    public TestdroidImageRecognition() throws Exception {
        deviceName = client.getDeviceName();
        driver = client.getDriver();
        automationName = client.getAutomationName();
        screenshotsFolder = "";
        counter = 0;
        rotation = "notSet"; //rotation not yet calculated
    }

    public static TestdroidAppiumDriver getDriver() {
        return driver;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getScreenshotsFolder() {
        if (screenshotsFolder.equals("")){
            return "target/reports/" + getDeviceName() + "/" + "screenshots/";
        }
        else {
            return "target/reports/" + getDeviceName() + "/" + "screenshots/" + screenshotsFolder + "/";
        }

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
        File folderPath = new File(System.getProperty("user.dir") + "/" + getScreenshotsFolder());
        String folderName = System.getProperty("user.dir") + "/" + "target/reports/" + getDeviceName();
        driver.takeScreenshot(fullFileName);
    }

    public Point[] findImage(String image, String scene, String setRotation) {
        log("Image to find: queryimages/" + screenshotsFolder + "/" + image + " in " + getScreenshotsFolder() + getScreenshotsCounter() + "_" + scene);
        AkazeImageFinder imageFinder = new AkazeImageFinder(rotation);

        Point[] imgRect = new Point[0];
        try {
            imgRect = imageFinder.findImage("queryimages/" + screenshotsFolder + "/" + image, getScreenshotsFolder() + getScreenshotsCounter() + "_" + scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rotation = imageFinder.getRotation();

        if (imgRect != null) {
            if (rotation.equals("notSet")) {
                log("ERROR: cannot detect screenshot rotation based on screenshot: " + getScreenshotsFolder() + getScreenshotsCounter() + "_" + scene);
            }

            //for retina devices we need to recalculate coordinates
            if ((deviceName.contains("iPhone"))
                    || (deviceName.contains("iPad 3"))
                    || (deviceName.contains("iPad 4"))
                    || (deviceName.contains("iPad Air"))) {
                Point[] imgRectScaled = new Point[]{new Point(imgRect[0].x / 2, imgRect[0].y / 2), new Point(imgRect[1].x / 2, imgRect[1].y / 2), new Point(imgRect[2].x / 2, imgRect[2].y / 2), new Point(imgRect[3].x / 2, imgRect[3].y / 2), new Point(imgRect[4].x / 2, imgRect[4].y / 2)};
                log("Device with Retina display => co-ordinates have been recalculated");
                return imgRectScaled;
            } else {
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
        if (automationName.equals("Selendroid")) {
            selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);
        } else {
            driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
        }
        sleep(SHORT_SLEEP);
    }


    public void tapMiddle() throws Exception {
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();
        Point middle = new Point(size.getWidth() / 2, size.getHeight() / 2);
        if (automationName.equals("Selendroid")) {
            selendroidTapAtCoordinate((int) middle.x, (int) middle.y, 1);
        } else {
            driver.tap(1, (int) middle.x, (int) middle.y, 1);
        }
        sleep(SHORT_SLEEP);
    }

    public void sleep(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
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
        assertNotNull(imgRect);
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
        assertNotNull(imgRect);
        return imgRect;
    }

    public Point[] findImageOnScreen(String image, int retries) throws Exception {

        Point[] imgRect = null;
        while ((retries > 0) && (imgRect == null)) {
            log("Find image started, retries left: " + retries);
            takeScreenshot(image + "_screenshot");
            imgRect = findImage(image, image + "_screenshot");
            retries = retries - 1;
        }
        assertNotNull(imgRect);
        return imgRect;
    }

    public void tapImageOnScreen(String image) throws Exception {
        Point[] imgRect = findImageOnScreen(image);
        //imgRect[4] will have the center of the rectangle containing the image

        if (automationName.equals("Selendroid")) {
            selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);
        } else {
            driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
        }
        sleep(SHORT_SLEEP);
    }


    public void tapImageOnScreen(String image, int retries) throws Exception {
        Point[] imgRect = findImageOnScreen(image, retries);
        //imgRect[4] will have the center of the rectangle containing the image

        if (automationName.equals("Selendroid")) {
            selendroidTapAtCoordinate((int) imgRect[4].x, (int) imgRect[4].y, 1);

        } else {
            driver.tap(1, (int) imgRect[4].x, (int) imgRect[4].y, 1);
        }
    }

    public void swipeScreenWithImage(String image, int repeats) throws Exception {
        Point[] imgRect = findImageOnScreen(image, 10);
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();

        if (automationName.equals("Selendroid")) {
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
                    action.move((int) (left_x + i * 10), size.getHeight());
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
                    action.moveTo((int) (left_x + i * 20), size.getHeight());
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

        if (automationName.equals("Selendroid")) {
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


    public void selendroidTapAtCoordinate(int x, int y, int secs) throws Exception {
        TouchActions actions = new TouchActions(driver);
        actions.down(x, y).perform();
        sleep(secs);
        actions.up(x, y).perform();
    }

    public void tapImageOnScreen(String image, double x_offset, double y_offset) throws Exception {
        Point[] imgRect = findImageOnScreen(image);
        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];

        //adding the offset to each coordinate; if offset = 0.5, middle will be returned
        double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
        double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;


        if (automationName.equals("Selendroid")) {
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


        if (automationName.equals("Selendroid")) {
            selendroidTapAtCoordinate((int) newX, (int) newY, 1);
            sleep(SHORT_SLEEP);
            selendroidTapAtCoordinate((int) newX, (int) newY, 1);
        } else {
            driver.tap(1, (int) newX, (int) newY, 1);
            sleep(SHORT_SLEEP);
            driver.tap(1, (int) newX, (int) newY, 1);
        }
    }


    public void tapImageOnScreen(String image, double x_offset, double y_offset, int retries) throws Exception {
        Point[] imgRect = findImageOnScreen(image, retries);
        Point top_left = imgRect[0];
        Point top_right = imgRect[1];
        Point bottom_left = imgRect[2];

        //adding the offset to each coordinate; if offset = 0.5, middle will be returned
        double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
        double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;


        if (automationName.equals("Selendroid")) {
            selendroidTapAtCoordinate((int) newX, (int) newY, 1);
        } else {
            driver.tap(1, (int) newX, (int) newY, 1);
        }
    }

    public void swipeScreenWithImage(String image) throws Exception {

        swipeScreenWithImage(image, 1);
    }

    public void log(String message, String marker) {

    }

    public void log(String message) {
        logger.info(message);
    }
}
