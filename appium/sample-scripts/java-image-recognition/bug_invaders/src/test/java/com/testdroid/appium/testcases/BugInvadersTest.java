package com.testdroid.appium.testcases;

import com.testdroid.appium.common.Constants;
import org.junit.Test;
import org.opencv.core.Point;

import com.testdroid.appium.common.DriverHandler;
import com.testdroid.appium.common.ImageUtil;
import org.slf4j.*;

/**
 * Demo steps:
 * - find different images in game loading phase
 * - find Items button in home view and and tap found object
 * - navigate back until quit game view reached
 * - find OK button and tap to quit
 *
 * @author Weilin Yu
 */
public class BugInvadersTest extends DriverHandler {

    Logger logger = LoggerFactory.getLogger(BugInvadersTest.class);

    private String screenshotPrefix = "screenshot";
    private int screenshotCount = 1;

    ImageUtil util = new ImageUtil(driver, client);

    private static final String[] targetImagesInitializing = {"game"};
    private static final String[] targetImagesGameLoop = {"gun1", "gun2", "gun3", "gun4"};

    @Test
    public void tesHobbitKabamDemo() throws Exception {

        logger.info(">>>>>> BUG INVADERS DEMO >>>>>>");

        boolean isLoading = false;
        boolean isLoaded = false;

        Thread.sleep(3000);
        takeScreenshot();

        for (int i = 0; i < targetImagesInitializing.length; i++) {
            String targetImage = targetImagesInitializing[i];
            logger.info(String.format("XXXXXXXXXXXXXXXXXXXX Initializing - STEP %s - Target image: %s XXXXXXXXXXXXXXXXXXXX", i + 1, targetImage));
            findAndClick(targetImage);
        }

        while (true) {
            for (int i = 0; i < targetImagesGameLoop.length; i++) {
                String targetImage = targetImagesGameLoop[i];
                logger.info(String.format("XXXXXXXXXXXXXXXXXXXX Game - STEP %s - Target image: %s XXXXXXXXXXXXXXXXXXXX", i + 1, targetImage));
                findAndClick(targetImage);
            }
        }
    }

    private void findAndClick(String targetImage) {

        boolean rotate = false;

        Point[] imgRect = null;

        imgRect = util.findTarget(Constants.platformName + "_" + targetImage, Constants.RETRIES_FIND_IMAGE);

        if (imgRect == null) {
            logger.debug(String.format("%s :: imageRect is null", targetImage));
        } else {
            // if four corners of target image are recognized
            if (ImageUtil.objMatchPoints > 4 && ImageUtil.sceneMatchPoints > 4) {
                logger.debug(String.format("%s found. Rotation: %s, Tapping it", targetImage, ImageUtil.rotation));

                // tap in the center of imageRect for 0.1 seconds
                util.tapObject(imgRect, 0.1, rotate);
                // takeScreenshot();
            } else {
                logger.debug(String.format("%s not found", targetImage));
            }
        }

        // Thread.sleep(10000);

    }

    private void takeScreenshot() throws Exception {
        String screenshotName = String.format("%s_%s_%s", Constants.platformName, screenshotPrefix, screenshotCount++);
        logger.info(String.format("Taking screenshot: %s.png", screenshotName));
        util.takeScreenShot(screenshotName);
    }

}
