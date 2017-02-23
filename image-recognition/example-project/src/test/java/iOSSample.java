import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.slf4j.LoggerFactory;

import objects.ImageSearchResult;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

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

public class iOSSample extends TestdroidImageRecognition {

    public iOSSample() throws Exception {
        super();
        logger = LoggerFactory.getLogger(iOSSample.class);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        driver = getIOSDriver();
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            log("Quitting Appium driver at tearDown");
            driver.quit();
        } else {
            log("driver was null at tearDown");
        }
    }

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            String fullFileName = System.getProperty("user.dir") + "/" + System.getProperty("SCREENSHOT_FOLDER") + description.getMethodName() + "_failure.png";
            try {
                File scrFile = driver.getScreenshotAs(OutputType.FILE);
                File testScreenshot = new File(fullFileName);
                FileUtils.copyFile(scrFile, testScreenshot);
                logger.info("Screenshot stored to {}", testScreenshot.getAbsolutePath());
                logger.info("PAGE SOURCE:");
                logger.info(driver.getPageSource());
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    };

    @Test
    public void mainPageTest() throws Exception {
        log("Image Recognition sample script started.");
        takeScreenshot("Before hideKeyboard");
        hideKeyboard();
        ImageSearchResult result = findImageOnScreen("bitbar_logo.png");
        assert(result.isFound());
        log("Success.");
    }
}
