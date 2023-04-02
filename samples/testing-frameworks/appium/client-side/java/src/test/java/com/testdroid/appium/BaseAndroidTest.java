package com.testdroid.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.IOException;
import java.net.URL;

public class BaseAndroidTest extends BaseTest {

    protected AndroidDriver wd;

    @Override
    protected AppiumDriver getAppiumDriver() {
        return wd;
    }

    @Override
    protected void setAppiumDriver() throws IOException {
        LOGGER.debug("Setting up AndroidDriver");
        this.wd = new AndroidDriver(new URL(getAppiumServerAddress() + "/wd/hub"), capabilities);
    }

    @Override
    protected String getServerSideApplicationPath() {
        return System.getProperty("user.dir") + "/application.apk";
    }

    @Override
    protected String getDesiredCapabilitiesPropertiesFileName() {
        if (isClientSideTestRun()) {
            return "desiredCapabilities.android.clientside.properties";
        } else if (isClientSideTestRunWithBiometry()) {
            return "desiredCapabilities.androidBiometry.clientside.properties";
        } else {
            return "desiredCapabilities.android.serverside.properties";
        }
    }
}
