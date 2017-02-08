import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.OutputType;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Testdroid Image Recognition Sample Test
 *
 * https://git@github.com/bitbar/testdroid-samples
 *
 * Usage:
 *
 * @author support@bitbar.com
 */

@SuppressWarnings({"UnqualifiedFieldAccess", "UnqualifiedMethodAccess"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class iOSSample extends TestdroidImageRecognition {

    public iOSSample() throws Exception {
        super(LoggerFactory.getLogger(iOSSample.class));
    }

    @BeforeClass
    public static void setUp() throws IllegalAccessException, IOException, InterruptedException, java.net.MalformedURLException {
        AkazeImageFinder.setupOpenCVEnv();
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
    public void mainPageTest() throws Exception, IOException, InterruptedException {
        log("Image Recognition sample script started.");
        takeScreenshot("Before hideKeyboard");
        hideKeyboard();
        findImageOnScreen("bitbar_logo");
        log("Success.");
    }
}
