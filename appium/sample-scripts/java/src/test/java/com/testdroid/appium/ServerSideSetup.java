package com.testdroid.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public abstract class ServerSideSetup {

    protected static Logger logger = LoggerFactory.getLogger(ServerSideSetup.class);
    protected static AppiumDriver<MobileElement> driver;
    protected static int defaultWaitTime = 60;

    public static String automationName = System.getenv("AUTOMATION_NAME");
    public static String deviceName = System.getenv("DEVICE_NAME") != null ? System.getenv("DEVICE_NAME") : "device";
    public static String udid = System.getenv("UDID");
    public static String platformVersion = System.getenv("PLATFORM_VERSION") != null ? System.getenv("PLATFORM_VERSION") : "";
    // Set to false to autoDismiss
    public static boolean autoAccept = true;

    public void setUpAppiumAndroidDriver() throws IOException {
        if (automationName == null){
            automationName = "appium";
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("deviceName", "Android Device");
        if (udid != null)
            capabilities.setCapability("udid", udid);
        if (platformVersion != null)
            capabilities.setCapability("platformVersion", platformVersion);

        capabilities.setCapability("app", System.getProperty("user.dir") + "/application.apk");
        capabilities.setCapability("newCommandTimeout", 2400);

        logger.debug("Creating Appium session, this may take couple minutes..");
        driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        driver.resetApp();
    }

    public void setUpAppiumIOSDriver() throws Exception {
        if (platformVersion == null){
            platformVersion = "";
        }
        if (automationName == null) {
            automationName = "XCUITest";
        }
        if (udid == null) {
            udid = ideviceinfoCheck("UniqueDeviceID");
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName", automationName);
        capabilities.setCapability("deviceName", deviceName);
        if (udid != null)
            capabilities.setCapability("udid", udid);
        if (platformVersion != null)
            capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("app", System.getProperty("user.dir") + "/application.ipa");
        capabilities.setCapability("newCommandTimeout", 120);

        logger.debug("Creating Appium session, this may take couple minutes..");
        driver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
    }

    protected void quitAppiumSession() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected File takeScreenshot(String screenshotName) {
        String fullFileName = System.getProperty("user.dir") + "/screenshots/" + screenshotName + ".png";
        logger.debug("Taking screenshot...");
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            File testScreenshot = new File(fullFileName);
            FileUtils.copyFile(scrFile, testScreenshot);
            logger.debug("Screenshot stored to " + testScreenshot.getAbsolutePath());

            return testScreenshot;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ideviceinfoCheck(String key) throws IOException, InterruptedException {
        String[] cmd = new String[]{"ideviceinfo", "--key", key};
        int exitVal = -1;
        ProcessBuilder probuilder = new ProcessBuilder(cmd);
        String output = "";
        try {
            Process process = probuilder.start();

            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            output = "";
            logger.debug("Output of running %s is:\n" + Arrays.toString(cmd));
            while ((line = br.readLine()) != null) {
                logger.debug(line);
                output = line;
            }

            int exitValue = process.waitFor();
            logger.debug("\n\nExit Value is " + exitValue);
            if (exitVal != 0) {
                logger.debug("ideviceinfo process exited with value: " + exitVal);
            }

        } catch (Exception e) {}

        return output;
    }
}
