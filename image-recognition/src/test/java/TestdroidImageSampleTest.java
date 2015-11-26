import com.testdroid.appium.TestdroidAppiumClient;
import com.testdroid.appium.TestdroidAppiumDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Testdroid Image Recognition Sample Test
 *
 * https://git@github.com/bitbar/testdroid-samples
 *
 * Usage:
 *
 * @TODO
 *
 * @author support@bitbar.com
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestdroidImageSampleTest extends TestdroidImageRecognition {

    public TestdroidImageSampleTest() throws Exception {
        super();
        logger = LoggerFactory.getLogger(TestdroidImageSampleTest.class);
        automationName = client.getAutomationName();
        Thread.currentThread().setName("TestdroidImageSampleTest - " + deviceName);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        driver = getDriver();
        client = new TestdroidAppiumClient();
        client.setPlatformName(TestdroidAppiumClient.APPIUM_PLATFORM_ANDROID);
        client.setTestdroidTarget(TestdroidAppiumClient.TESTDROID_TARGET_ANDROID);
        // Wait one hour for free device
        client.setDeviceWaitTime(3600);

    }

    @AfterClass
    public static void tearDown() {
        System.out.println("Quitting Appium driver");
        driver.quit();
    }

    @Test
    public void mainPageTest() throws Exception {
        rotation = "0 degrees";
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        logger.info("Image Recongition sample script started.");
        
        driver.hideKeyboard();
        findImageOnScreen("bitbar_logo");
    }
}
