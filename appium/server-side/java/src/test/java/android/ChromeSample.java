package android;

import common.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.slf4j.LoggerFactory;

/**
 * Testdroid Server Side Chrome Sample Test
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

public class ChromeSample extends AbstractAppiumTest {

    public ChromeSample() throws Exception {
        super();
        logger = LoggerFactory.getLogger(ChromeSample.class);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        appFile = null;
        browserName = "chrome";
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
    public void mainPageTest() throws Exception {
        try {
            log("Chrome parallel script started.");
            driver.get("http://bitbar.github.io/testdroid-samples/");

            driver.rotate(ScreenOrientation.LANDSCAPE);
            takeScreenshot("Landscape");

            driver.rotate(ScreenOrientation.PORTRAIT);
            takeScreenshot("Portrait");

            driver.findElement(By.xpath("//button[contains(., \"Click for answer\")]")).click();

            log("Check answer text");
            String style  = driver.findElement(By.xpath("//p[@id=\"result_element\" and contains(., \"Testdroid\")]")).getAttribute("style");
            String expectedStyle = "rgb(127, 255, 0";
            Assert.assertTrue(style.contains(expectedStyle));

        } catch (Exception e) {
            takeScreenshot("Test failure");
            log(driver.getPageSource());
            throw e;
        }
    }
}
