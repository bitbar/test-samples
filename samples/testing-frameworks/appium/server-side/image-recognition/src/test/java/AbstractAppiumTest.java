import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import objects.PlatformType;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public abstract class AbstractAppiumTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAppiumTest.class);
    public static File userDir = new File(System.getProperty("user.dir"));
    public static File[] matches = userDir.listFiles((dir, name) -> name.startsWith("application"));
    public static String appFile = System.getenv("APP_FILE") != null ? System.getenv("APP_FILE") : matches[0].toString();
    public static PlatformType platform;
    public static String automationName = System.getenv("AUTOMATION_NAME");
    public static String deviceName = System.getenv("DEVICE_NAME") != null ? System.getenv("DEVICE_NAME") : "device";
    public static String udid = System.getenv("UDID");
    public static String platformVersion = System.getenv("PLATFORM_VERSION") != null ? System.getenv("PLATFORM_VERSION") : "";
    // Set to false to autoDismiss
    public static boolean autoAccept = true;
    protected static AppiumDriver driver;
    protected static int defaultWaitTime = 60;

    public static AppiumDriver getIOSDriver() throws Exception {
        idevicescreenshotCheck();
        if (platform == null) {
            platform = PlatformType.IOS;
        }
        if (platformVersion == null) {
            platformVersion = "";
        }
        if (automationName == null) {
            DefaultArtifactVersion version = new DefaultArtifactVersion(ideviceinfoCheck("ProductVersion"));
            DefaultArtifactVersion minVersion = new DefaultArtifactVersion("9.3.5");
            // Use XCUITest if device is above iOS version 9.3.5
            if (version.compareTo(minVersion) >= 0) {
                automationName = "XCUITest";
            } else {
                automationName = "Appium";
            }
        }
        if (udid == null) {
            udid = ideviceinfoCheck("UniqueDeviceID");
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("platformName", platform.getPlatformName());
        capabilities.setCapability("deviceName", deviceName);
        if (udid != null)
            capabilities.setCapability("udid", udid);
        if (platformVersion != null)
            capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("app", appFile);
        capabilities.setCapability("newCommandTimeout", 120);

        log("Creating Appium session, this may take couple minutes..");
        driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public static AppiumDriver getAndroidDriver() throws Exception {
        adbCheck();
        if (platform == null) {
            platform = PlatformType.ANDROID;
        }
        if (automationName == null) {
            automationName = "appium";
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("platformName", platform.getPlatformName());
        capabilities.setCapability("deviceName", "Android Device");
        if (udid != null) {
            capabilities.setCapability("udid", udid);
        }
        if (platformVersion != null) {
            capabilities.setCapability("platformVersion", platformVersion);
        }

        capabilities.setCapability("app", appFile);
        capabilities.setCapability("newCommandTimeout", 120);

        log("Creating Appium session, this may take couple minutes..");
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        ((AndroidDriver) driver).resetApp();
        return driver;
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

    public static String ideviceinfoCheck(String key) throws IOException, InterruptedException {
        String[] cmd = new String[]{"ideviceinfo", "--key", key};
        int exitVal = -1;
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String output = "";
        while ((line = in.readLine()) != null) {
            log(line);
            output = line;
        }
        exitVal = p.waitFor();
        if (exitVal != 0) {
            log("ideviceinfo process exited with value: " + exitVal);
        }
        return output;
    }

    public static void idevicescreenshotCheck() throws Exception {
        String[] cmd = new String[]{"idevicescreenshot", "--help"};
        int exitVal = -1;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            exitVal = p.waitFor();
        } catch (IOException e) {
            log(e.toString());
        }
        if (exitVal != 0) {
            String errorMsg = "idevicescreenshot exited with value: " + exitVal + ". Please install it or add it to your path in order to use the image-recognition-opencv-library.";
            LOGGER.error(errorMsg);
            throw new Exception(errorMsg);
        }
    }

    public static void adbCheck() throws Exception {
        String[] cmd = new String[]{"adb", "version"};
        int exitVal = -1;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            exitVal = p.waitFor();
        } catch (IOException e) {
            log(e.toString());
        }
        if (exitVal != 0) {
            String errorMsg = "adb exited with value: " + exitVal + ". Please install it or add it to your path in order to use the image-recognition-opencv-library.";
            LOGGER.error(errorMsg);
            throw new Exception(errorMsg);
        }
    }

    public static void ideviceinstall(String appPath) throws IOException, InterruptedException {
        String[] cmd = new String[]{"ideviceinstaller", "-i", appPath};
        int exitVal = -1;
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            log(line);
        }
        exitVal = p.waitFor();
        if (exitVal != 0) {
            log("ideviceinstaller process exited with value: " + exitVal);
        }
    }

    public static void ideviceuninstall(String bundleId) throws IOException, InterruptedException {
        String[] cmd = new String[]{"ideviceinstaller", "-U", bundleId};
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            log(line);
        }
        int exitVal = p.waitFor();
        if (exitVal != 0) {
            log("ideviceinstaller process exited with value: " + exitVal);
        }
    }

    //Stops the script for the given amount of seconds.
    public static void sleep(long seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    //Stops the script for the given amount of seconds.
    public static void sleep(double seconds) throws Exception {
        log("Waiting for " + seconds + " sec");
        seconds = seconds * 1000;
        Thread.sleep((int) seconds);
    }

    public static void log(String message) {
        LOGGER.info(message);
    }

    protected void swipeUp() {
        swipeUp(0.15f, 0.15f);
    }

    protected void swipeUp(float topPad, float bottomPad) {
        Dimension size = driver.manage().window().getSize();
        LOGGER.debug("Window size is " + size);
        swipeUp(new Point(0, 0), size, 1000, topPad, bottomPad);
    }

    protected void swipeUp(Point rootLocation, Dimension rootSize, int duration, float topPad, float bottomPad) {
        int offset = 1;
        int topOffset = Math.round(rootSize.getHeight() * topPad);
        int bottomOffset = Math.round(rootSize.getHeight() * bottomPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        LOGGER.debug("Swiping up at" +
                " x1: " + center.getX() +
                " y1:" + (rootLocation.getY() + rootSize.getHeight() - bottomOffset + offset) +
                " x2:" + center.getX() +
                " y2:" + (rootLocation.getY() + topOffset));
        swipe(center.getX(),
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
        LOGGER.debug("Window size is " + size);
        swipeDown(new Point(0, 0), size, 1000, topPad, bottomPad);
    }

    protected void swipeDown(Point rootLocation, Dimension rootSize, int duration, float topPad, float bottomPad) {
        int offset = 1;
        int topOffset = Math.round(rootSize.getHeight() * topPad);
        int bottomOffset = Math.round(rootSize.getHeight() * bottomPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        LOGGER.debug("Swiping down at" +
                " x1: " + center.getX() +
                " y1:" + (rootLocation.getY() + topOffset) +
                " x2:" + center.getX() +
                " y2:" + (rootLocation.getY() + rootSize.getHeight() - bottomOffset + offset));
        swipe(center.getX(),
                rootLocation.getY() + topOffset,
                center.getX(),
                rootLocation.getY() + rootSize.getHeight() - bottomOffset + offset,
                duration);
    }

    protected void swipeLeft() {
        swipeLeft(0.15f, 0.15f);
    }

    protected void swipeLeft(float leftPad, float rightPad) {
        Dimension size = driver.manage().window().getSize();
        LOGGER.debug("Window size " + size);
        swipeLeft(new Point(0, 0), size, 1000, leftPad, rightPad);
    }

    protected void swipeLeft(Point rootLocation, Dimension rootSize, int duration, float leftPad, float rightPad) {
        int offset = 1;
        int leftOffset = Math.round(rootSize.getWidth() * leftPad);
        int rightOffset = Math.round(rootSize.getWidth() * rightPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        LOGGER.debug("Swiping left at" +
                " x1: " + (rootLocation.getX() + rootSize.getWidth() - rightOffset + offset) +
                " y1:" + center.getY() +
                " x2:" + (rootLocation.getX() + leftOffset) +
                " y2:" + center.getY());
        swipe(rootLocation.getX() + rootSize.getWidth() - rightOffset + offset,
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
        swipeRight(new Point(0, 0), size, 1000, leftPad, rightPad);
    }

    protected void swipeRight(Point rootLocation, Dimension rootSize, int duration, float leftPad, float rightPad) {
        int offset = 1;
        int leftOffset = Math.round(rootSize.getWidth() * leftPad);
        int rightOffset = Math.round(rootSize.getWidth() * rightPad);
        Point center = new Point(rootLocation.x + rootSize.getWidth() / 2, rootLocation.y + rootSize.getHeight() / 2);
        LOGGER.debug("Swiping right at" +
                " x1: " + (rootLocation.getX() + leftOffset) +
                " y1:" + center.getY() +
                " x2:" + (rootLocation.getX() + rootSize.getWidth() - rightOffset + offset) +
                " y2:" + center.getY());
        swipe(rootLocation.getX() + leftOffset,
                center.getY(),
                (rootLocation.getX() + rootSize.getWidth() - rightOffset + offset),
                center.getY(),
                duration);
    }

    protected void tap(int x, int y) {
        TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
        touchAction.tap(PointOption.point(x, y)).perform();
    }

    protected void tap(int x, int y, int duration) {
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.press(PointOption.point(x, y));
        action.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)));
        action.release();
        action.perform();
    }

    public void swipe(int startX, int startY, int endX, int endY, int duration) {
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.press(PointOption.point(startX, startY));
        action.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)));
        action.moveTo(PointOption.point(endX, endY));
        action.release();
        action.perform();
    }

    protected void hideKeyboard() {
        try {
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            ((HidesKeyboard) driver).hideKeyboard();
        } catch (Exception e) {
            LOGGER.debug("Hiding soft keyboard failed");
            LOGGER.debug(e.toString());
        }
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
    }
}
