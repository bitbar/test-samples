package com.testdroid.appium.ios.sample;

import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleAppiumiOSTest extends com.testdroid.appium.BaseTest {
    protected static final String TESTDROID_SERVER = "http://appium.testdroid.com";
    protected static final String TARGET_APP_PATH = "../../../apps/builds/BitbarIOSSample.ipa";

    @BeforeClass
    public static void setUp() throws Exception {

        Map<String, String> env = System.getenv();
        String testdroid_apikey = env.get("TESTDROID_APIKEY");

        if(StringUtils.isEmpty(testdroid_apikey)) {
            throw new IllegalArgumentException("Missing TESTDROID_APIKEY environment variable");
        }

        String fileUUID = uploadFile(TARGET_APP_PATH,TESTDROID_SERVER, testdroid_apikey);


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");

        capabilities.setCapability("device", "iphone");
        capabilities.setCapability("deviceName", "iOS Phone");

        capabilities.setCapability("testdroid_apiKey", testdroid_apikey);
        capabilities.setCapability("testdroid_project", "LocaliOSAppium");
        capabilities.setCapability("testdroid_description", "Appium project description");
        capabilities.setCapability("testdroid_testrun", "iOS Run");
        capabilities.setCapability("testdroid_device", "iPad 3 A1416 8.2");
        capabilities.setCapability("testdroid_app", fileUUID); //to use existing app using "latest" as fileUUID
        capabilities.setCapability("testdroid_target", "iOS");
        capabilities.setCapability("app", "com.bitbar.testdroid.BitbarIOSSample");
        System.out.println("Capabilities:" + capabilities.toString());
        wd = new IOSDriver(new URL(TESTDROID_SERVER+"/wd/hub"), capabilities);
    }
    @AfterClass
    public static void tearDown()
    {
        if(wd != null) {
            wd.quit();
        }

    }

    @Test
    public void mainPageTest() throws Exception {
        try {

            wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            wd.findElement(By.name("answer1")).click();
            WebElement element = wd.findElement(By.name("userName"));
            element.click();
            element.sendKeys("Testdroid");
            wd.findElement(By.name("Return")).click();
            wd.findElement(By.name("sendAnswer")).click();

        } catch( Exception e ) {
            takeScreenshot("Failed.png");
            throw e;
        }

    }


}
