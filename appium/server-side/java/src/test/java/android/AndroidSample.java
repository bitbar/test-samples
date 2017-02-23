package android;

import common.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.slf4j.LoggerFactory;

/**
 * Testdroid Server Side Android Sample Test
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

public class AndroidSample extends AbstractAppiumTest {

    public AndroidSample() throws Exception {
        super();
        logger = LoggerFactory.getLogger(AndroidSample.class);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        appFile = "application.apk";
        browserName = null;
        driver = getAndroidDriver();
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

    @Test
    public void androidSampleTest() throws Exception {
        try {
            log("Server Side Android sample script started.");
            try {
                driver.hideKeyboard();
            } catch (Exception e) {
                log("Keyboard not present; going forward.");
            }

            driver.findElement(By.xpath("//*[@text='Use Testdroid Cloud']")).click();
            driver.findElement(By.xpath("//*[contains(@class, 'EditText')]")).sendKeys("John Doe");
            takeScreenshot("after_adding_name");
            try {
                driver.hideKeyboard();
            } catch (Exception e) {
                log("Keyboard not present; going forward.");
            }
            driver.findElement(By.xpath("//*[1]/*[1]/*[2]/*[1]")).click();
            takeScreenshot("after_answer");
        } catch (Exception e) {
            takeScreenshot("Failure.");
            log(driver.getPageSource());
            throw e;
        }

    }
}
