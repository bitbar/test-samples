import io.appium.java_client.TouchAction;
import objects.ImageLocation;
import objects.ImageRecognitionSettings;
import objects.ImageSearchResult;
import objects.PlatformType;

import org.apache.commons.io.FileUtils;
import org.opencv.core.Point;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import imagerecognition.ImageRecognition;

import java.io.*;


/**
 * Created by testdroid on 22/07/16.
 */
public class TestdroidImageRecognition extends AbstractAppiumTest {

    public Logger logger = LoggerFactory.getLogger(TestdroidImageRecognition.class);
    String screenshotsFolder;
    String queryImageFolder;

    public TestdroidImageRecognition(){
        super();
        screenshotsFolder = System.getenv("SCREENSHOT_FOLDER");
        if (screenshotsFolder == null || screenshotsFolder.isEmpty()) {
            screenshotsFolder = "target/reports/screenshots/";
        }
        queryImageFolder = System.getenv("QUERYIMAGES_FOLDER");
        if (queryImageFolder == null || queryImageFolder.isEmpty()) {
            queryImageFolder = "queryimages/";
        }
        File dir = new File(screenshotsFolder);
        dir.mkdirs();
    }

    /**
     * ======================================================================================
     * FINDING AN IMAGE IN ANOTHER IMAGE
     * ======================================================================================
     */

    public ImageLocation findImage(String image, String scene) throws Exception {
        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        return ImageRecognition.findImage(image, scene, settings, platform);
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
        } else if (platform.equals(PlatformType.ANDROID)){
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

    public ImageSearchResult findImageOnScreen(String image) throws Exception {
        ImageRecognitionSettings defaultSettings = new ImageRecognitionSettings();
        return findImageOnScreen(image, defaultSettings);
    }
    
    public ImageSearchResult findImageOnScreen(String imageName, ImageRecognitionSettings settings) throws Exception {
        String imageFile = queryImageFolder+imageName;
        log("Searching for: "+imageFile);
        ImageSearchResult foundImage = ImageRecognition.findImageOnScreen(imageFile, screenshotsFolder, settings, platform);
        return foundImage;
    }

    public void waitForImageToDisappearFromScreen(String image) throws Exception {
        String imageFile = queryImageFolder+image;
        boolean hasImageDisappeared = ImageRecognition.hasImageDissappearedFromScreenBeforeTimeout(imageFile, screenshotsFolder, platform);
        assert(hasImageDisappeared);
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
    public void tapImageOnScreen(String imageName, double x_offset, double y_offset, ImageRecognitionSettings settings) throws Exception {
        ImageSearchResult result = findImageOnScreen(imageName, settings);
        assert(result.isFound());
        ImageLocation location = result.getImageLocation();
        Point top_left = location.getTopLeft();
        Point top_right = location.getTopRight();
        Point bottom_left = location.getBottomLeft();
        Point center = location.getCenter();

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

    public void tapImageOnScreen(String image) throws Exception {
        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        tapImageOnScreen(image, 0.5, 0.5, settings);
    }

    public void tapImageOnScreen(String image, ImageRecognitionSettings settings) throws Exception {
        tapImageOnScreen(image, 0.5, 0.5, settings);
    }

    //Finds an image on screen and taps on it. Method will cause a FAIL if the image is not found.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "x_offset" and "y_offset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    // "taps" sets the number of taps that are performed.
    // "frequency" sets the frequency of the taps.
    public void multipleTapImageOnScreen(String imageName, int taps, double frequency, double x_offset, double y_offset, ImageRecognitionSettings settings) throws Exception {
        ImageSearchResult result = findImageOnScreen(imageName, settings);
        assert(result.isFound());
        ImageLocation location = result.getImageLocation();
        
        Point top_left = location.getTopLeft();
        Point top_right = location.getTopRight();
        Point bottom_left = location.getBottomLeft();
        Point center = location.getCenter();

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
        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        multipleTapImageOnScreen(image, taps, frequency, x_offset, y_offset, settings);
    }

    public void multipleTapImageOnScreen(String image, int taps, double frequency) throws Exception {
        multipleTapImageOnScreen(image, taps, frequency, 0.5, 0.5);
    }

    public void multipleTapImageOnScreen(String image, int taps) throws Exception {
        multipleTapImageOnScreen(image, taps, 1, 0.5, 0.5);
    }

    //Finds an image on screen and taps on it. Method will NOT cause a FAIL if the image is not found.
    // "image" is the searched image name
    // "retries" is the number of times the method will try to find the searched image. If not set, default is 5.
    // "x_offset" and "y_offset" change the location on the found image where the tap is performed. If not used, the defaults are (0.5, 0.5) which represent the middle of the image.
    public boolean tryTapImageOnScreen(String image, double x_offset, double y_offset, ImageRecognitionSettings settings) throws Exception {
        ImageSearchResult searchResult = findImageOnScreen(image, settings);

        if (searchResult.isFound() == false) {
            return false;
        } else {
            Point top_left = searchResult.getImageLocation().getBottomLeft();
            Point top_right = searchResult.getImageLocation().getTopRight();
            Point bottom_left = searchResult.getImageLocation().getBottomLeft();
            Point center = searchResult.getImageLocation().getCenter();

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

    public boolean tryTapImageOnScreen(String image) throws Exception {
        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        return tryTapImageOnScreen(image, 0.5, 0.5, settings);
    }
    
    // Finds an image on screen and taps and hold on it for a specified duration.
    // "duration" is given in seconds
    // Returns true if Image was found, false if the image was not found
    public boolean tapAndHoldImageOnScreen(String imageName, double x_offset, double y_offset, int duration, ImageRecognitionSettings settings, boolean with_assert) throws Exception {
        ImageSearchResult foundImage = findImageOnScreen(imageName, settings);
        if (with_assert){
            assert(foundImage.isFound());
        }
        if (foundImage.isFound() == false ) {
            return false;
        }

        Point top_left = foundImage.getImageLocation().getTopLeft();
        Point top_right = foundImage.getImageLocation().getTopRight();
        Point bottom_left = foundImage.getImageLocation().getBottomLeft();

        double newX = top_left.x + (top_right.x - top_left.x) * x_offset;
        double newY = top_left.y + (bottom_left.y - top_left.y) * y_offset;

        log("Coordinates with offset are: " + newX + ", " + newY);

        if (automationName.equalsIgnoreCase("selendroid")) {
            selendroidTapAtCoordinate((int) newX, (int) newY, duration);
        } else {
            driver.tap(1, (int) newX, (int) newY, duration);
        }
        return true;
    }

    public void tapAndHoldImageOnScreen(String image, int duration, boolean with_assert) throws Exception {
        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        tapAndHoldImageOnScreen(image, 0.5, 0.5, duration, settings, with_assert);
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
    public void swipeVerticallyOnImage(String imageName, int distance, double x_offset, double y_offset) throws Exception {
        ImageSearchResult searchResult = findImageOnScreen(imageName);
        assert(searchResult.isFound());
        ImageLocation location = searchResult.getImageLocation();
        Point top_left = location.getTopLeft();
        Point top_right = location.getTopRight();
        Point bottom_left = location.getBottomLeft();
        
        int startX = (int) (top_left.x + (top_right.x - top_left.x) * x_offset);
        int startY = (int) (top_left.y + (bottom_left.y - top_left.y) * y_offset);
        int endX = startX;
        int endY = startY + distance;

        if (platform.equals(PlatformType.IOS)) {
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
    public void swipeHorizontallyOnImage(String imageName, int distance, double x_offset, double y_offset) throws Exception {
        ImageSearchResult searchResult = findImageOnScreen(imageName);
        assert(searchResult.isFound());
        ImageLocation location = searchResult.getImageLocation();
        Point top_left = location.getTopLeft();
        Point top_right = location.getTopRight();
        Point bottom_left = location.getBottomLeft();

        int startX = (int) (top_left.x + (top_right.x - top_left.x) * x_offset);
        int startY = (int) (top_left.y + (bottom_left.y - top_left.y) * y_offset);
        int endX = startX + distance;
        int endY = startY;

        if (platform.equals(PlatformType.IOS)) {
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
            if (PlatformType.IOS.equals(PlatformType.IOS)) {
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
            if (PlatformType.IOS.equals(PlatformType.IOS)) {
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
            if (PlatformType.IOS.equals(PlatformType.IOS)) {
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
            if (PlatformType.IOS.equals(PlatformType.IOS)) {
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

        if (PlatformType.IOS.equals(PlatformType.IOS)) {
            iOSSwipe((int) swipe_start.x, (int) swipe_start.y, (int) swipe_end.x, (int) swipe_end.y);
        } else {
            androidSwipe((int) swipe_start.x, (int) swipe_start.y, (int) swipe_end.x, (int) swipe_end.y);
        }
    }

    /**
     * ======================================================================================
     * DRAG AND DROP
     * ======================================================================================
     */

    //Performs a drag and drop from the middle of the "object" image to the middle of the "target" image.
    public void dragFromOneImageToAnother(String object, String target) throws Exception {
        ImageSearchResult object_image = findImageOnScreen(object);
        assert(object_image.isFound());
        ImageLocation object_image_location = object_image.getImageLocation();
        
        ImageSearchResult target_image = findImageOnScreen(target);
        assert(target_image.isFound());
        ImageLocation target_image_location = target_image.getImageLocation();

        int startX = (int) object_image_location.getCenter().x;
        int startY = (int) object_image_location.getCenter().y;

        int endX = (int) target_image_location.getCenter().x;
        int endY = (int) target_image_location.getCenter().y;

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

        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        settings.setRetries(10);
        ImageSearchResult object_image = findImageOnScreen(object, settings);
        assert(object_image.isFound());
        ImageLocation object_image_location = object_image.getImageLocation();

        int startX = (int) object_image_location.getCenter().x;
        int startY = (int) object_image_location.getCenter().y;

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

        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        settings.setRetries(10);
        ImageSearchResult object_image = findImageOnScreen(object, settings);
        assert(object_image.isFound());
        ImageLocation object_image_location = object_image.getImageLocation();

        int startX = (int) object_image_location.getCenter().x;
        int startY = (int) object_image_location.getCenter().y;

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
        if (platform.equals(PlatformType.IOS)) {
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
        if (platform.equals(PlatformType.IOS)) {
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
        if (platform.equals(PlatformType.IOS)) {
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
        if (platform.equals(PlatformType.IOS)) {
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

    /**
     * ======================================================================================
     * CROP IMAGE
     * ======================================================================================
     */

    public ImageSearchResult findAndCropImageFromScreen(String imageName) throws Exception {
        ImageRecognitionSettings settings = new ImageRecognitionSettings();
        settings.setRetries(3);
        settings.setTolerance(0.60);
        settings.setCrop(true);
        return findImageOnScreen(imageName, settings);
    }

    /**
     * ======================================================================================
     * TESSERACT GRAB TEXT FROM IMAGE
     * ======================================================================================
     */

    public String grabText(String imageName) throws Exception {
        ImageSearchResult result = findAndCropImageFromScreen(imageName);
        return ImageRecognition.getTextStringFromImage(result.getScreenshotFile());
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
            } else if (platform.equals(PlatformType.ANDROID)) {
                driver.findElement(By.xpath("//android.widget.Button[@text='Cancel']")).click();
            } else { //we are on ios
                driver.findElementByAccessibilityId("Cancel").click();
            }
        }
        if (automationName.equalsIgnoreCase("selendroid")) {
            ImageRecognitionSettings settings = new ImageRecognitionSettings();
            settings.setRetries(1);
            while (findImageOnScreen("native_sign_in_button").isFound()) {
                tapImageOnScreen("native_sign_in_button", 0.75, 0.5, settings);
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

    protected void takeScreenshot(String screenshotName) throws IOException {
        try {
            ImageRecognition.takeScreenshot(screenshotName, screenshotsFolder, platform);
        } catch (Exception e) {
            File scrFile = driver.getScreenshotAs(OutputType.FILE);
            String screenshotFile = screenshotsFolder + screenshotName + ".png";
            String screenShotFilePath = System.getProperty("user.dir") + "/" + screenshotFile;
            File testScreenshot = new File(screenShotFilePath);
            FileUtils.copyFile(scrFile, testScreenshot);
            logger.info("Screenshot stored to {}", testScreenshot.getAbsolutePath());
            return;
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
                } else if (platform.equals(PlatformType.ANDROID)) {
                    driver.findElement(By.xpath("//android.widget.Button[@text='Sign in']")).click();
                } else { //we are on ios
                    driver.findElementByAccessibilityId("Sign In").click();
                }
            takeScreenshot("after_clicking_sign_in");
        }
    }

    public void dismissFullScreenMessage() throws Exception {
        log("Trying to dismiss Full Screen notification message if it shows up...");
        ImageRecognitionSettings oneRetry = new ImageRecognitionSettings();
        oneRetry.setRetries(1);
        ImageRecognitionSettings twoRetries = new ImageRecognitionSettings();
        twoRetries.setRetries(2);
        tryTapImageOnScreen("full_screen_1", 0.75, 0.75, oneRetry);
        tryTapImageOnScreen("full_screen_1", 0.75, 0.75, twoRetries);
        tryTapImageOnScreen("full_screen_2", 0.75, 0.75, oneRetry);
        tryTapImageOnScreen("full_screen_3", 0.75, 0.8, oneRetry);
    }

}
