package com.testdroid.appium;

import io.appium.java_client.ios.IOSDriver;

import java.io.IOException;
import java.net.URL;

public class BaseIOSTest extends BaseTest {

	@Override
	protected void setAppiumDriver() throws IOException {
		this.wd = new IOSDriver(new URL(getAppiumServerAddress() + "/wd/hub"), capabilities);
	}

    @Override
    protected String getServerSideApplicationPath() {
        return System.getProperty("user.dir") + "/application.ipa";
    }

    @Override
    protected String getDesiredCapabilitiesPropertiesFileName() {
        if (isClientSideTestRun()){
            return "desiredCapabilities.ios.clientside.properties";
        } else {
            return "desiredCapabilities.ios.serverside.properties";
        }
    }

}
