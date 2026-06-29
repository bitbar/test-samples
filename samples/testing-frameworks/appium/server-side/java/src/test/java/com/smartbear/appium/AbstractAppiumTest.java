package com.smartbear.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

abstract class AbstractAppiumTest {

    private static final String APPLICATION_PREFIX = "application";

    private static final String APP_FILE = System.getenv("APP_FILE");

    private static final int DEFAULT_WAIT_TIME = 60;

    private static final String HUB_URL = "http://localhost:4723/wd/hub";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAppiumTest.class);

    private static final String SCREENSHOTS_FOLDER = "screenshots";

    private static final String UDID = System.getenv("UDID");

    private static final File USER_DIR = new File(System.getProperty("user.dir"));

    protected static AppiumDriver driver;

    protected static AppiumDriver getAndroidDriver() throws Exception {
        adbCheck();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        DesiredCapabilities appiumOptions = new DesiredCapabilities();
        appiumOptions.setCapability("automationName", "uiautomator2");
        appiumOptions.setCapability("deviceName", "Android Device");
        appiumOptions.setCapability("app", getAppFile());
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:options", appiumOptions);

        LOGGER.info("Creating Appium session for Android, this may take a couple of minutes...");
        AppiumDriver appiumDriver = new AndroidDriver(URI.create(HUB_URL).toURL(), capabilities);
        appiumDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_WAIT_TIME));
        return appiumDriver;
    }

    protected static AppiumDriver getIOSDriver() throws Exception {
        idevicescreenshotCheck();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        DesiredCapabilities appiumOptions = new DesiredCapabilities();
        appiumOptions.setCapability("automationName", "XCUITest");
        if (UDID != null) {
            appiumOptions.setCapability("udid", UDID);
        }
        appiumOptions.setCapability("app", getAppFile());
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("appium:options", appiumOptions);

        LOGGER.info("Creating Appium session for iOS, this may take a couple of minutes...");
        AppiumDriver appiumDriver = new IOSDriver(URI.create(HUB_URL).toURL(), capabilities);
        appiumDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_WAIT_TIME));
        return appiumDriver;
    }

    private static void idevicescreenshotCheck() throws Exception {
        String[] cmd = new String[]{"idevicescreenshot", "--help"};
        int exitVal = -1;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            exitVal = p.waitFor();
        } catch (IOException e) {
            LOGGER.info("Couldn't execute command", e);
        }
        if (exitVal != 0) {
            throw new Exception(String.format("idevicescreenshot exited with value: %s", exitVal));
        }
    }

    private static void adbCheck() throws Exception {
        String[] cmd = new String[]{"adb", "version"};
        int exitVal = -1;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            exitVal = p.waitFor();
        } catch (IOException e) {
            LOGGER.error("Couldn't execute command", e);
        }
        if (exitVal != 0) {
            throw new Exception(String.format("adb exited with value: %s", exitVal));
        }
    }

    private static String getAppFile() {
        if (APP_FILE != null) {
            return APP_FILE;
        }
        File[] matches = getMatchesAppFile();
        if (matches != null && matches.length > 0 && matches[0] != null) {
            return matches[0].toString();
        }
        String errorMsg = "App file not found. Please provide the app file as an environment variable or place it in " +
                "the project root directory with a name starting with 'application'.";
        throw new IllegalArgumentException(errorMsg);
    }

    private static File[] getMatchesAppFile() {
        return USER_DIR.listFiles((dir, name) -> name.startsWith(APPLICATION_PREFIX));
    }

    protected void takeScreenshot(String screenshotName) throws IOException {
        Path screenshotDir = Paths.get(System.getProperty("user.dir"), SCREENSHOTS_FOLDER);
        Files.createDirectories(screenshotDir);
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        Path screenshotFilePath = screenshotDir.resolve(screenshotName + ".png");
        File testScreenshot = screenshotFilePath.toFile();
        FileUtils.copyFile(scrFile, testScreenshot);
        LOGGER.info("Screenshot stored to {}", testScreenshot.getAbsolutePath());
    }

}
