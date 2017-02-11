package imagerecognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Point;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import objects.ImageLocation;
import objects.ImageRecognitionSettings;
import objects.ImageSearchResult;
import objects.PlatformType;

public class ImageRecognition {

    private static Logger logger = LoggerFactory.getLogger(ImageRecognition.class);
    private static AkazeImageFinder imageFinder = new AkazeImageFinder();
    private static void log(String message) {
        logger.info(message);
    }

    static {
        AkazeImageFinder.setupOpenCVEnv();
    }

    
    /**
     * Find the location of the reference image on the screen
     *
     * @param  searchedImageFilePath Path to the reference image file to be searched
     * @param  sceneImageFilePath Path to the scene file in which the image is going to be searched for
     * @param  platform Defines the platform (phone operating system) that is in use. PlatformType.ANDROID for Android and PlatformType.IOS for iOS
     * @return Returns the location of the image or null if image has not been found
     */
    public static ImageLocation findImage(String searchedImageFilePath, String sceneImageFilePath, PlatformType platform) throws Exception {
        ImageRecognitionSettings setting = new ImageRecognitionSettings();
        return findImage(searchedImageFilePath, sceneImageFilePath, setting, platform);
    }
    
    /**
     * Find the location of the reference image on the screen
     * @param searchedImageFilePath Path to the reference image file to be searched
     * @param sceneImageFilePath Path to the scene file in which the image is going to be searched for
     * @param settings Image recognition related settings
     * @param platform Defines the platform (phone operating system) that is in use. PlatformType.ANDROID for Android and PlatformType.IOS for iOS
     * @return Returns the location of the image or null if image has not been found
     * @throws Exception
     */
    public static ImageLocation findImage(String searchedImageFilePath, String sceneImageFilePath, ImageRecognitionSettings settings, PlatformType platform) throws Exception {
        log("Searching for " + searchedImageFilePath);
        log("Searching in " + sceneImageFilePath);
        ImageLocation imgLocation = imageFinder.findImage(searchedImageFilePath, sceneImageFilePath, settings.getTolerance());

        if (imgLocation != null) {
            Dimension screenSize = getScreenSize(platform);
            if (platform.equals(PlatformType.IOS)) {
                imgLocation = scaleImageRectangleForIos(screenSize, imgLocation, sceneImageFilePath);
            }
            Point center = imgLocation.getCenter();
            if (!isPointInsideScreenBounds(center, screenSize)) {
                log("Screen size is (width, height): " + screenSize.getWidth() + ", " + screenSize.getHeight());
                log("WARNING: Coordinates found do not match the screen --> image not found.");
                imgLocation = null;
            }
        }
        return imgLocation;
    }

    private static ImageLocation scaleImageRectangleForIos(Dimension screenSize, ImageLocation imageLocation, String sceneImageFilePath) {
        //for retina devices we need to recalculate coordinates
        double sceneHeight = imageFinder.getSceneHeight(sceneImageFilePath);
        double sceneWidth = imageFinder.getSceneWidth(sceneImageFilePath);
        int screenHeight = screenSize.getHeight();
        int screenWidth = screenSize.getWidth();

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
                log("Recalculating coordinates for x3 retina displays..");
                imageLocation.divideCoordinatesBy(3);
                log("Device with Retina display rendered at  => coordinates have been recalculated");
            }
            else {
                log("Recalculating coordinates for x2 retina displays..");
                imageLocation.divideCoordinatesBy(2);
                log("Device with Retina display rendered at x2 => coordinates have been recalculated");
            }
        }
        return imageLocation;
    }

    private static boolean isPointInsideScreenBounds(Point center, Dimension screenSize) {
        return !((center.x >= screenSize.width) || (center.x < 0) || (center.y >= screenSize.height) || (center.y < 0));
    }


    /**
     * Checks whether image disappears from screen before a predefined timeout.
     * 
     * @param searchedImageFilePath Path to the reference image file to be searched
     * @param screenshotBaseDirectory Path to the directory in which the screenshots should be stored
     * @param platform Defines the platform (phone operating system) that is in use. PlatformType.ANDROID for Android and PlatformType.IOS for iOS
     * @return True if the image cannot be found or disappears successfully. False if the image can be found and the function timeouts.
     * @throws Exception
     */
    public static boolean hasImageDissappearedFromScreenBeforeTimeout(String searchedImageFilePath,
            String screenshotBaseDirectory, PlatformType platform) throws Exception {
        log("==> Trying to find image: " + searchedImageFilePath);
        int retry_counter=0;
        long start = System.nanoTime();
        while (((System.nanoTime() - start) / 1e6 / 1000 < 300)) {
            String screenshotName = FilenameUtils.getBaseName(searchedImageFilePath) + "_screenshot_"+retry_counter;
            String screenShotFile = ImageRecognition.takeScreenshot(screenshotName, screenshotBaseDirectory, platform);
            if ((findImage(searchedImageFilePath, screenShotFile, platform)) == null) {
                log("Image has successfully disappeared from screen.");
                return true;
            }
            sleep(3);	
            retry_counter++;
        }
        logger.warn("Image did not disappear from screen");
        return false;
    }

    /**
     * Extract text from an image.
     * 
     * @param imageInput Path to the image file in which a text should be found
     * @return The found text in the image.
     */
    public static String getTextStringFromImage(String imageInput) {
        String[] tesseractCommand = {"tesseract", imageInput, "stdout"};
        String value = "";
        try {
            ProcessBuilder p = new ProcessBuilder(tesseractCommand);
            Process proc = p.start();
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                value += line;
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return value;
    }




    /**
     * @param searchedImageFilePath Path to the reference image file to be searched
     * @param screenshotBaseDirectory Path to the directory in which the screenshots should be stored
     * @param settings Image recognition related settings
     * @param platform Defines the platform (phone operating system) that is in use. PlatformType.ANDROID for Android and PlatformType.IOS for iOS
     * @return ImageSearchResult, an object containing information about the location of the found image and a screenshot from the moment the reference image was found.
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    public static ImageSearchResult findImageOnScreen(String searchedImageFilePath, String screenshotBaseDirectory, ImageRecognitionSettings settings, PlatformType platform) throws InterruptedException, IOException, Exception {
        ImageSearchResult imageSearchResult = findImageLoop(searchedImageFilePath, screenshotBaseDirectory, settings, platform);
        if (imageSearchResult.isFound() && settings.isCrop()) {
            log("Cropping image..");
            imageFinder.cropImage(imageSearchResult);
            log("Cropping image.. Succeeded!");
        }
        return imageSearchResult;
    }

    private static ImageSearchResult findImageLoop(String searchedImagePath, String screenshotBaseDirectory, ImageRecognitionSettings settings, PlatformType platform) throws InterruptedException, IOException, Exception {
        long start_time = System.nanoTime();
        ImageSearchResult imageSearchResult = new ImageSearchResult();
        String imageName = FilenameUtils.getBaseName(searchedImagePath);
        for (int i = 0; i < settings.getRetries(); i++) {
            String screenshotName = imageName + "_screenshot_"+i;
            String screenshotFile = takeScreenshot(screenshotName,screenshotBaseDirectory, platform);
            ImageLocation imageLocation = ImageRecognition.findImage(searchedImagePath, screenshotFile, settings, platform);
            if (imageLocation!=null){
                long end_time = System.nanoTime();
                int difference = (int) ((end_time - start_time) / 1e6 / 1000);
                log("==> Find image took: " + difference + " secs.");
                imageSearchResult.setImageLocation(imageLocation);
                imageSearchResult.setScreenshotFile(screenshotFile);
                return imageSearchResult;
            }
            retryWait(settings);
        }
        log("==> Image not found");
        return imageSearchResult;
    }

    private static void retryWait(ImageRecognitionSettings settings) throws InterruptedException {
        if (settings.getRetryWaitTime() > 0) {
            log("retryWait given, sleeping " + settings.getRetryWaitTime() + " seconds.");
            sleep(settings.getRetryWaitTime());
        }
    }

    private static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    public static String takeScreenshot(String screenshotName, String screenshotBaseDirectory, PlatformType platform) throws Exception {
        long start_time = System.nanoTime();

        String screenshotFile = screenshotBaseDirectory + screenshotName + ".png";
        String screenShotFilePath = System.getProperty("user.dir") + "/" + screenshotFile;

        if (platform.equals(PlatformType.IOS)) {
            takeIDeviceScreenshot(screenShotFilePath);
        } else if (platform.equals(PlatformType.ANDROID)) {
            takeAndroidScreenshot(screenShotFilePath);
        } else{
            throw new Exception("Invalid platformType: "+platform);
        }

        long end_time = System.nanoTime();
        int difference = (int) ((end_time - start_time) / 1e6 / 1000);
        logger.info("==> Taking a screenshot took " + difference + " secs.");
        return screenshotFile;
    }

    private static void takeAndroidScreenshot(String screenShotFilePath) throws IOException, InterruptedException {
        log("Taking android screenshot...");
        log(screenShotFilePath);
        String[] cmd = new String[]{"screenshot2", "-d", screenShotFilePath};
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = in.readLine()) != null)
            log(line);

        int exitVal = p.waitFor();
        if (exitVal != 0) {
            log("screenshot2 process exited with value: " + exitVal);
        }
    }

    private static void takeIDeviceScreenshot(String screenShotFilePath) throws Exception {
        String udid = getIosUdid();
        String[] cmd = new String[]{"idevicescreenshot", "-u", udid, screenShotFilePath};
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = in.readLine()) != null)
            log(line);

        int exitVal = p.waitFor();
        if (exitVal != 0) {
            log("idevicescreenshot process exited with value: " + exitVal);
        }
        cmd = new String[]{"sips", "-s", "format", "png", screenShotFilePath, "--out", screenShotFilePath};
        p = Runtime.getRuntime().exec(cmd);
        exitVal = p.waitFor();
        if (exitVal != 0) {
            log("sips process exited with value: " + exitVal);
        }
    }
    
    private static Dimension getScreenSize(PlatformType platform) throws Exception {
        if (platform.equals(PlatformType.IOS)) {
            return getIosScreenSize();
        } else {
            return getAndroidScreenSize();
        }
    }

    private static Dimension getIosScreenSize() throws Exception {
        String udid = getIosUdid();
        String productType = getIosProductType(udid);
        Dimension screenSize = getIosScreenSizePointsFromPropertiesFile(productType);
        return screenSize;
    }

    private static String getIosProductType(String udid) throws IOException, InterruptedException, Exception {
        String[] cmd = new String[]{"ideviceinfo", "-u", udid, "--key", "ProductType"};
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

        int exitVal = p.waitFor();
        if (exitVal != 0) {
            throw new Exception("ideviceinfo process exited with value: " + exitVal);
        }
        String productType = in.readLine();
        return productType;
    }

    private static String getIosUdid() throws Exception {
        String udid = System.getenv("UDID");
        if (udid==null){
            throw new Exception("$UDID was null, set UDID environment variable and try again");
        }
        return udid;
    }

    private static Dimension getIosScreenSizePointsFromPropertiesFile(String productType) throws Exception {
        Properties screenSizeProperties = fetchProperties();
        String screenDimensionString = (String) screenSizeProperties.get(productType);
        if (screenDimensionString == null){
            throw new Exception("ios-screen-size.properties is missing entry for: " + productType);
        }
        String screenDimensions[] = screenDimensionString.split(" x ");
        if (screenDimensions.length!=2){
            throw new Exception("Invalid ios-screen-size.properties file syntax for line: " + productType);
        }
        int width = Integer.parseInt(screenDimensions[0]);
        int height = Integer.parseInt(screenDimensions[1]);
        return new Dimension(width, height);
    }
    
    private static Properties fetchProperties() throws Exception {
        Properties iosScreenSizeProperties = new Properties();
        InputStream input = null;
        try {
            String filename = "ios-screen-size.properties";
            input = ImageRecognition.class.getClassLoader().getResourceAsStream(filename);
            
            if (input == null) {
                throw new Exception("ios-screen-size.properties does not exist");
            }
            iosScreenSizeProperties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return iosScreenSizeProperties;
    }

    private static Dimension getAndroidScreenSize() throws IOException, InterruptedException {
        String adb = "adb";
        String[] adbCommand = {adb, "shell", "dumpsys", "window"};
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
    }


}
