import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by testdroid on 22/07/16.
 */
public abstract class AbstractAppiumTest {

    public static final int SHORT_SLEEP = 1;
    public static final int MEDIUM_SLEEP = 5;
    public static final int LONG_SLEEP = 10;

    protected static Logger logger = LoggerFactory.getLogger(AbstractAppiumTest.class);
    private static long timeDifferenceStartTest;
    private static long startTime;

    protected static AppiumDriver<MobileElement> driver;
    protected static int defaultWaitTime = 120;

    private static int counter = 0;
    private static int retry_counter = 1;
    public static String searchedImage;

    public static String screenshotsFolder = "";
    public static String appFile = System.getenv("APP_FILE");
    public static String platformName = System.getenv("PLATFORM_NAME");
    public static String automationName = System.getenv("AUTOMATION_NAME");
    public static String deviceName = System.getenv("DEVICE_NAME");
    public static String udid = System.getenv("UDID");
    public static String platformVersion = System.getenv("PLATFORM_VERSION");
    // Set to false to autoDismiss
    public static boolean autoAccept = true;
    public static boolean idevicescreenshotExists = false;


    public static AppiumDriver getIOSDriver() throws Exception {
        if (appFile == null) {
            appFile = "application.ipa";
        }
        if (platformName == null) {
            platformName = "iOS";
        }
        if (deviceName == null){
        	deviceName = "device";
        }
        if (platformVersion == null){
        	platformVersion = "";
        }
        // Use default "appium" automation for iOS
        automationName = "appium";

        screenshotsFolder = "target/reports/screenshots/ios/";
        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", deviceName);
        if (udid != null)
            capabilities.setCapability("udid", udid);
        if (platformVersion != null)
            capabilities.setCapability("platformVersion", platformVersion);

        capabilities.setCapability("app", System.getProperty("user.dir") + File.separator + appFile);
        capabilities.setCapability("newCommandTimeout", 120);
        capabilities.setCapability("nativeInstrumentsLib", true);
        if (autoAccept) {
            capabilities.setCapability("autoAcceptAlerts", true);
            capabilities.setCapability("autoDismissAlerts", false);
        } else {
            capabilities.setCapability("autoAcceptAlerts", false);
            capabilities.setCapability("autoDismissAlerts", true);
        }

        idevicescreenshotCheck();

        log("Creating Appium session, this may take couple minutes..");
        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public static AppiumDriver getAndroidDriver() throws Exception {
        if (appFile == null) {
            appFile = "application.apk";
        }
        if (platformName == null) {
            platformName = "Android";
        }
        if (deviceName == null){
            deviceName = "device";
        }
        if (automationName == null){
        	automationName = "appium";
        }
        screenshotsFolder = "target/reports/screenshots/android/";
        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", "Android Device");
        if (udid != null)
            capabilities.setCapability("udid", udid);
        if (platformVersion != null)
            capabilities.setCapability("platformVersion", platformVersion);

        capabilities.setCapability("app", System.getProperty("user.dir") + File.separator + appFile);
        capabilities.setCapability("newCommandTimeout", 120);

        log("Creating Appium session, this may take couple minutes..");
        driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        driver.resetApp();
        return driver;
    }

    public static void takeScreenshot(String screenshotName) throws Exception {
        takeScreenshot(screenshotName, true);
    }

    public static void takeScreenshot(String screenshotName, boolean new_step) throws IOException, InterruptedException {
      if (idevicescreenshotExists) {
        // Keep Appium session alive between multiple non-driver screenshots
        driver.manage().window().getSize();
      }
      timeDifferenceStartTest = (int) ((System.nanoTime() - startTime) / 1e6 / 1000);
      long start_time = System.nanoTime();

      if (new_step) {
          counter = counter + 1;
          retry_counter = 1;
      } else {
          retry_counter = retry_counter + 1;
      }

      searchedImage = screenshotsFolder + getScreenshotsCounter() + "_" + screenshotName + getRetryCounter() + "_" + timeDifferenceStartTest + "sec";
      String fullFileName = System.getProperty("user.dir") + "/" + searchedImage + ".png";

    	if (platformName.equalsIgnoreCase("iOS") && idevicescreenshotExists) {
          String[] cmd = new String[]{"idevicescreenshot", "-u", udid, fullFileName};
      		Process p = Runtime.getRuntime().exec(cmd);
      		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line;
      		while ((line = in.readLine()) != null)
      			log(line);

      		int exitVal = p.waitFor();
          if (exitVal != 0) {
              log("idevicescreenshot process exited with value: " + exitVal);
          }
          cmd = new String[]{"sips", "-s", "format", "png", fullFileName, "--out", fullFileName};
          p = Runtime.getRuntime().exec(cmd);
          exitVal = p.waitFor();
          if (exitVal != 0) {
              log("sips process exited with value: " + exitVal);
          }
    	} else {
            // idevicescreenshot not available, using driver.getScreenshotAs()
        	File scrFile = driver.getScreenshotAs(OutputType.FILE);
          try {
              File testScreenshot = new File(fullFileName);
              FileUtils.copyFile(scrFile, testScreenshot);
              logger.info("Screenshot stored to {}", testScreenshot.getAbsolutePath());
              return;
          } catch (IOException e) {
              e.printStackTrace();
          }
    	}

      long end_time = System.nanoTime();
      int difference = (int) ((end_time - start_time) / 1e6 / 1000);
      logger.info("==> Taking a screenshot took " + difference + " secs.");
    }

    //On a test run on the local machine this method will save the Reports folder in different folders on every test run.
    public static void savePreviousRunReports() {
        long millis = System.currentTimeMillis();
        File dir = new File("./target/reports");
        File newName = new File("./target/reports" + millis);
        if (dir.isDirectory()) {
            dir.renameTo(newName);
        } else {
            dir.mkdir();
            dir.renameTo(newName);
        }
    }

    public static boolean idevicescreenshotCheck() throws IOException, InterruptedException {
        String[] cmd = new String[]{"idevicescreenshot", "--help"};
        int exitVal = -1;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            exitVal = p.waitFor();
        } catch (IOException e) {
            log(e.toString());
        }
        if (exitVal == 0) {
            log("idevicescreenshot exited with value: " + exitVal + ". Using it for screenshots.");
            idevicescreenshotExists = true;
        } else {
            log("idevicescreenshot process exited with value: " + exitVal + ". Won't be using it for screenshots.");
            idevicescreenshotExists = false;
        }
        return idevicescreenshotExists;
    }

    //Will count the screenshots taken for separate stages of the test.
    public static String getScreenshotsCounter() {
        if (counter < 100) {
            if (counter < 10) {
                return "00" + counter;
            } else {
                return "0" + counter;
            }
        } else {
            return Integer.toString(counter);
        }
    }

    //Will count the number of times we tried to find the same image. When this counter goes up, the screenshot counter remains the same.
    static String getRetryCounter() {
        if (retry_counter < 10) {
            return "_0" + retry_counter;
        } else {
            return "_" + Integer.toString(retry_counter);
        }
    }

    //Stops the script for the given amount of seconds.
    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    //Stops the script for the given amount of seconds.
    public static void sleep(double seconds) throws Exception {
        log("Waiting for " + seconds + " sec");
        seconds = seconds * 1000;
        Thread.sleep((int) seconds);
    }

    public static void log(String message) {
        logger.info(message);
    }

    protected void swipeUp() {
        swipeUp(0.15f, 0.15f);
    }

    protected void swipeUp(float topPad, float bottomPad) {
        Dimension size = driver.manage().window().getSize();
        logger.debug("Window size is " + size);
        swipeUp(new Point(0, 0), size, 1000, topPad, bottomPad);
    }

    protected void swipeUp(Point rootLocation, Dimension rootSize, int duration, float topPad, float bottomPad) {
        int offset = 1;
        int topOffset = Math.round(rootSize.getHeight() * topPad);
        int bottomOffset = Math.round(rootSize.getHeight() * bottomPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        logger.debug("Swiping up at" +
                " x1: " + center.getX() +
                " y1:" + (rootLocation.getY() + rootSize.getHeight() - bottomOffset + offset) +
                " x2:" + center.getX() +
                " y2:" + (rootLocation.getY() + topOffset));
        driver.swipe(center.getX(),
                rootLocation.getY() + rootSize.getHeight() - bottomOffset + offset,
                center.getX(),
                rootLocation.getY() + topOffset,
                duration);
    }

    protected void swipeDown() {
        swipeDown(0.15f, 0.15f);
    }

    protected void swipeDown(float topPad, float bottomPad) {
        Dimension size = driver.manage().window().getSize();
        logger.debug("Window size is " + size);
        swipeDown(new Point(0, 0), size, 1000, topPad, bottomPad);
    }

    protected void swipeDown(Point rootLocation, Dimension rootSize, int duration, float topPad, float bottomPad) {
        int offset = 1;
        int topOffset = Math.round(rootSize.getHeight() * topPad);
        int bottomOffset = Math.round(rootSize.getHeight() * bottomPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        logger.debug("Swiping down at" +
                " x1: " + center.getX() +
                " y1:" + (rootLocation.getY() + topOffset) +
                " x2:" + center.getX() +
                " y2:" + (rootLocation.getY() + rootSize.getHeight() - bottomOffset + offset));
        driver.swipe(center.getX(),
                (rootLocation.getY() + topOffset),
                center.getX(),
                (rootLocation.getY() + rootSize.getHeight() - bottomOffset + offset),
                duration);
    }

    protected void swipeLeft() {
        swipeLeft(0.15f, 0.15f);
    }

    protected void swipeLeft(float leftPad, float rightPad) {
        Dimension size = driver.manage().window().getSize();
        logger.debug("Window size " + size);
        swipeLeft(new Point(0,0), size, 1000, leftPad, rightPad);
    }

    protected void swipeLeft(Point rootLocation, Dimension rootSize, int duration, float leftPad, float rightPad) {
        int offset = 1;
        int leftOffset = Math.round(rootSize.getWidth() * leftPad);
        int rightOffset = Math.round(rootSize.getWidth() * rightPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        logger.debug("Swiping left at" +
                " x1: " + (rootLocation.getX() + rootSize.getWidth() - rightOffset + offset) +
                " y1:" + center.getY() +
                " x2:" + (rootLocation.getX() + leftOffset) +
                " y2:" + center.getY());
        driver.swipe((rootLocation.getX() + rootSize.getWidth() - rightOffset + offset),
                center.getY(),
                (rootLocation.getX() + leftOffset),
                center.getY(),
                duration);
    }

    protected void swipeRight() {
        swipeRight(0.15f, 0.15f);
    }

    protected void swipeRight(float leftPad, float rightPad) {
        Dimension size = driver.manage().window().getSize();
        swipeRight(new Point(0,0), size, 1000, leftPad, rightPad);
    }

    protected void swipeRight(Point rootLocation, Dimension rootSize, int duration, float leftPad, float rightPad) {
        int offset = 1;
        int leftOffset = Math.round(rootSize.getWidth() * leftPad);
        int rightOffset = Math.round(rootSize.getWidth() * rightPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        logger.debug("Swiping right at" +
                " x1: " + (rootLocation.getX() + leftOffset) +
                " y1:" + center.getY() +
                " x2:" + (rootLocation.getX() + rootSize.getWidth() - rightOffset + offset) +
                " y2:" + center.getY());
        driver.swipe((rootLocation.getX() + leftOffset),
                center.getY(),
                (rootLocation.getX() + rootSize.getWidth() - rightOffset + offset),
                center.getY(),
                duration);
    }

    protected void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (Exception e) {
            logger.debug("Hiding soft keyboard failed");
            logger.debug(e.toString());
        }
    }

}
