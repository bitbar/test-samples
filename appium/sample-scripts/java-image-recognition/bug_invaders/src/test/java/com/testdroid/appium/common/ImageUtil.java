package com.testdroid.appium.common;

import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.opencv.core.Point;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testdroid.appium.akaze.AkazeImageFinder;
import com.testdroid.appium.TestdroidAppiumClient;
import com.testdroid.appium.TestdroidAppiumDriver;

/**
 * Utility functions for image processing and gestures.
 *
 * @author Weilin Yu
 */
public class ImageUtil {

    public TestdroidAppiumDriver driver;
    public TestdroidAppiumClient client;

    public static String deviceName = "Android tablet";
    public static String automationName = "Appium";
    public static String screenshotsFolder = "";

    public static final int SHORT_SLEEP = 2;
    public static final int LONG_SLEEP = 10;

    public static int objMatchPoints = 0;
    public static int sceneMatchPoints = 0;

    public static String rotation;

    public int counter;

    public Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public ImageUtil(AppiumDriver driver, TestdroidAppiumClient client) {
        this.driver = (TestdroidAppiumDriver) driver;
        this.client = client;
        deviceName = client.getDeviceName();
        automationName = client.getAutomationName();
        screenshotsFolder = "";
        counter = 0;
        rotation = "notSet"; // rotation not yet calculated
    }

    public void setRotation(String rotation) {
        ImageUtil.rotation = rotation;
    }

    public String getScreenshotsFolder() {
        if (screenshotsFolder.equals("")) {
            return "target/reports/" + deviceName + "/" + "screenshots/";
        } else {
            return "target/reports/" + deviceName + "/" + "screenshots/"
                    + screenshotsFolder + "/";
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
        String fullFileName = System.getProperty("user.dir") + "/"
                + getScreenshotsFolder() + getScreenshotsCounter() + "_"
                + screenshotName + ".png";
        driver.takeScreenshot(fullFileName);
    }

    public void sleep(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }

    public Point[] findImage(String image, String scene) {
        logger.info("Image to find: queryimages/" + screenshotsFolder + "/"
                + image + " in " + getScreenshotsFolder()
                + getScreenshotsCounter() + "_" + scene);
        AkazeImageFinder imageFinder = new AkazeImageFinder(rotation);

        Point[] imgRect = new Point[0];
        try {
            imgRect = imageFinder.findImage("queryimages/" + screenshotsFolder
                    + "/" + image, getScreenshotsFolder()
                    + getScreenshotsCounter() + "_" + scene);

            objMatchPoints = AkazeImageFinder.objMatchPoints;
            sceneMatchPoints = AkazeImageFinder.sceneMatchPoints;

        } catch (Exception e) {
            e.printStackTrace();
        }

        rotation = imageFinder.getRotation();

        if (imgRect != null) {
            if (rotation.equals("notSet")) {
                logger.info("ERROR: cannot detect screenshot rotation based on screenshot: "
                        + getScreenshotsFolder()
                        + getScreenshotsCounter()
                        + "_" + scene);
            }

            // for retina devices we need to recalculate coordinates
            if ((deviceName.contains("iPhone"))
                    || (deviceName.contains("iPad 3"))
                    || (deviceName.contains("iPad 4"))
                    || (deviceName.contains("iPad Air"))) {
                Point[] imgRectScaled = new Point[]{
                        new Point(imgRect[0].x / 2, imgRect[0].y / 2),
                        new Point(imgRect[1].x / 2, imgRect[1].y / 2),
                        new Point(imgRect[2].x / 2, imgRect[2].y / 2),
                        new Point(imgRect[3].x / 2, imgRect[3].y / 2),
                        new Point(imgRect[4].x / 2, imgRect[4].y / 2)};
                logger.info("Device with Retina display => co-ordinates have been recalculated");
                return imgRectScaled;
            } else {
                return imgRect;
            }
        } else {
            return null;
        }
    }

    public Point[] findImageOnScreen(String imgName) {

        Point[] imgRect = null;

        try {
            takeScreenshot(imgName + "_screenshot");
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgRect = findImage(imgName, imgName + "_screenshot");

        if (objMatchPoints > 4 && sceneMatchPoints > 4) {
            return imgRect;
        } else {
            return null;
        }
    }

    public Point[] findTarget(String queryImg, int loopCount) {
        int i = 0;
        Point[] imgRect = null;

        while (i++ < loopCount) {
            logger.debug("ImageUtil::findTarget: " + i + "/" + loopCount
                    + " try find " + queryImg + ".png");

            try {
                imgRect = findImageOnScreen(queryImg);
            } catch (Exception e) {
                System.out
                        .println("ImageUtil::findTarget: ERROR - imgRect==null");
                e.printStackTrace();
            }

            logger.debug("ImageUtil::findTarget: obj matching points: "
                    + ImageUtil.objMatchPoints);

            if (ImageUtil.objMatchPoints > 4 && ImageUtil.sceneMatchPoints > 4) {
                logger.debug("ImageUtil::findTarget: " + queryImg + " found.");
                break;
            }
        }
        return imgRect;
    }

    public void swipe(double startX, double startY, double endX, double endY, double duration) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> swipeObject = new HashMap<String, Double>();
        swipeObject.put("startX", startX);
        swipeObject.put("startY", startY);
        swipeObject.put("endX", endX);
        swipeObject.put("endY", endY);
        swipeObject.put("duration", duration);
        js.executeScript("mobile: swipe", swipeObject);
    }

    public void tapObject(Point[] imgRect, double duration, boolean rotate) {

        double x = 0;
        double y = 0;

        logger.debug(String.format("imageRect.length: %s", imgRect.length));
        for (int i = 0; i < 4; i++) {
            x += imgRect[i].x;
            y += imgRect[i].y;
            logger.debug(String.format("imageRect values :: x: %s, y: %s", imgRect[i].x, imgRect[i].y));
        }
        x /= 4;
        y /= 4;

        logger.debug(String.format("Initial values :: x: %s, y: %s", x, y));

        int[] screenDimen = Constants.SCREEN_DIMENSION_IOS;
        if (Constants.platformName.equals(TestdroidAppiumClient.APPIUM_PLATFORM_ANDROID)) {
            screenDimen = Constants.SCREEN_DIMENSION_ANDROID;
        }

        logger.debug(String.format("Screen dimensions :: x: %s, y: %s", screenDimen[0], screenDimen[1]));
        logger.debug(String.format("Rotated Screen dimensions :: x: %s, y: %s", screenDimen[0], screenDimen[1]));

        if (rotate) {

            double temp = x;
            x = y;
            y = temp;
            logger.debug(String.format("Swapped values :: x: %s, y: %s", x, y));

            y = screenDimen[1] - y;
            logger.debug(String.format("Adjusted values :: x: %s, y: %s", x, y));

        }


        // x = x / (double) screenDimen[0];
        // y = y / (double) screenDimen[1];

        logger.debug(String.format("Percentage values :: x: %s, y: %s", x, y));

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            HashMap<String, Double> tapObject = new HashMap<String, Double>();
            tapObject.put("x", x);
            tapObject.put("y", y);
//            tapObject.put("x", (double) 570 / (double) screenDimen[0]);
//            tapObject.put("y", (double) 520 / (double) screenDimen[1]);
            tapObject.put("duration", duration);
            js.executeScript("mobile: tap", tapObject);
        } catch (NullPointerException e) {
            logger.debug("ImageUtil::tapFoundObj: ERROR - imgRect==null");
            e.printStackTrace();
        }
    }

    /**
     * Take screenshot to screenshot folder at project root
     *
     * @param fileName A file name of screenshot
     */
    public void takeScreenShot(String fileName) {
        try {
            File imgFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(imgFile, new File("screenshot/" + fileName
                    + ".png"));
            logger.debug("ImageUtil::takeScreenShot: " + fileName + ".png saved to screenshot folder");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // end of class
