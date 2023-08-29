import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;

public abstract class AbstractAppiumTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAppiumTest.class);

    public static File userDir = new File(System.getProperty("user.dir"));

    public static File[] matches = userDir.listFiles((dir, name) -> name.startsWith("application"));

    public static String appFile = System.getenv("APP_FILE") != null ? System.getenv("APP_FILE") :
        matches[0].toString();

    public static String automationName = System.getenv("AUTOMATION_NAME");

    public static String deviceName = System.getenv("DEVICE_NAME") != null ? System.getenv("DEVICE_NAME") : "device";

    public static String udid = System.getenv("UDID");

    public static String platformVersion = System.getenv("PLATFORM_VERSION") != null ? System.getenv(
        "PLATFORM_VERSION") : "";

    protected static AppiumDriver driver;

    protected static int defaultWaitTime = 60;

    String screenshotsFolder;

    public AbstractAppiumTest() {
        super();
        screenshotsFolder = System.getenv("SCREENSHOT_FOLDER");
        if (screenshotsFolder == null || screenshotsFolder.isEmpty()) {
            screenshotsFolder = "target/reports/screenshots/";
        }
        File dir = new File(screenshotsFolder);
        dir.mkdirs();
    }

    public static AppiumDriver getIOSDriver() throws Exception {
        idevicescreenshotCheck();
        if (platformVersion == null) {
            platformVersion = "";
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        DesiredCapabilities appiumOptions = new DesiredCapabilities();
        appiumOptions.setCapability("automationName", "XCUITest");
        if (udid != null) {
            appiumOptions.setCapability("udid", udid);
        }
        appiumOptions.setCapability("app", appFile);
        capabilities.setCapability("platformName", "IOS");
        capabilities.setCapability("appium:options", appiumOptions);

        log("Creating Appium session, this may take couple minutes..");
        driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWaitTime));
        return driver;
    }

    public static AppiumDriver getAndroidDriver() throws Exception {
        adbCheck();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        DesiredCapabilities appiumOptions = new DesiredCapabilities();
        appiumOptions.setCapability("automationName", "uiautomator2");
        appiumOptions.setCapability("deviceName", "Android Device");
        appiumOptions.setCapability("app", appFile);
        capabilities.setCapability("platformName", "ANDROID");
        capabilities.setCapability("appium:options", appiumOptions);

        log("Creating Appium session, this may take couple minutes..");
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWaitTime));
        ((AndroidDriver) driver).resetApp();
        return driver;
    }

    private static String ideviceinfoCheck(String key) throws IOException, InterruptedException {
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

    private static void idevicescreenshotCheck() throws Exception {
        String[] cmd = new String[]{"idevicescreenshot", "--help"};
        int exitVal = -1;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            exitVal = p.waitFor();
        } catch (IOException e) {
            log(e.toString());
        }
        if (exitVal != 0) {
            String errorMsg = "idevicescreenshot exited with value: " + exitVal + ". Please install it or add it to " +
                "your path in order to use the image-recognition-opencv-library.";
            LOGGER.error(errorMsg);
            throw new Exception(errorMsg);
        }
    }

    private static void adbCheck() throws Exception {
        String[] cmd = new String[]{"adb", "version"};
        int exitVal = -1;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            exitVal = p.waitFor();
        } catch (IOException e) {
            log(e.toString());
        }
        if (exitVal != 0) {
            String errorMsg = "adb exited with value: " + exitVal + ". Please install it or add it to your path in " +
                "order to use the image-recognition-opencv-library.";
            LOGGER.error(errorMsg);
            throw new Exception(errorMsg);
        }
    }

    public static void log(String message) {
        LOGGER.info(message);
    }

    protected void takeScreenshot(String screenshotName) throws IOException {
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        String screenshotFile = screenshotsFolder + screenshotName + ".png";
        String screenShotFilePath = System.getProperty("user.dir") + "/" + screenshotFile;
        File testScreenshot = new File(screenShotFilePath);
        FileUtils.copyFile(scrFile, testScreenshot);
        LOGGER.info("Screenshot stored to {}", testScreenshot.getAbsolutePath());
    }
}
