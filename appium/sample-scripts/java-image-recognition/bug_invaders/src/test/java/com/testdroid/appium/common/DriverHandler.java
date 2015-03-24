package com.testdroid.appium.common;

import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import junit.framework.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.testdroid.appium.TestdroidAppiumClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle starting appium server with desired capabilities
 * <p/>
 * Usage:
 * Localhost:
 * - set isLocalhost = true
 * Cloud service :
 * - set isLocalhost = false
 *
 * @author Weilin Yu
 */
public class DriverHandler {

    private static Logger logger = LoggerFactory.getLogger(DriverHandler.class);

    public static AppiumDriver driver = null;
    public static TestdroidAppiumClient client = null;

    public static Boolean isLocalhost = true;

    @BeforeClass
    public static void setUp() throws Exception {

        client = new TestdroidAppiumClient();

        setTestdroidClient();

        driver = client.getDriver();

        logger.debug("DriverHandler::setUp: " + driver.getCapabilities().toString());
        logger.debug("DriverHandler::setUp: Session ID: " + driver.getSessionId());

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Set desired capabilities for testdroid appium driver
     */
    public static void setTestdroidClient() {

        try {
            if (isLocalhost) {
                client.setAppiumUrl(new URL(Constants.APPIUM_URL_LOCALHOST));
                client.setDeviceName("my local device");
            } else {
                client.setAppiumUrl(new URL(Constants.APPIUM_URL_CLOUD));
                client.setDeviceName(Constants.ANDROID_DEVICES_NAMES[0]);
                client.setProjectName(Constants.PROJECT_NAME);
                client.setTestRunName(Constants.TESTRUN_NAMES[0]);
            }

            client.setPlatformName(Constants.platformName);
            client.setTestdroidJUnitWaitTime("300");

            if (Constants.platformName.equals(TestdroidAppiumClient.APPIUM_PLATFORM_IOS.toString())) {
                client.setBundleId(Constants.IOS_BUNDLE_ID);
                client.setAppFile(new File(Constants.APP_FILE_IOS));
            } else {
                client.setAutomationName("Appium");
                client.setAndroidPackage(Constants.ANDROID_PACKAGE_NAME);
                client.setAndroidActivity(Constants.ANDROID_ACTIVITY_NAME);
                client.setAppFile(new File(Constants.APP_FILE_ANDROID));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
