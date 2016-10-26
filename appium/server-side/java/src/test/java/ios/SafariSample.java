package ios;

import common.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.slf4j.LoggerFactory;

/**
 * Testdroid Server Side Safari Sample Test
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

public class SafariSample extends AbstractAppiumTest {

    public SafariSample() throws Exception {
        super();
        logger = LoggerFactory.getLogger(SafariSample.class);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        appFile = null;
        browserName = "safari";
        driver = getIOSDriver();
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("Quitting Appium driver");
        driver.quit();
    }

    @Test
    public void safariTest() throws Exception {
        try {
            log("Safari parallel script started.");
            driver.get("http://bitbar.github.io/testdroid-samples/");

            driver.rotate(ScreenOrientation.LANDSCAPE);
            takeScreenshot("Landscape");

            driver.rotate(ScreenOrientation.PORTRAIT);
            takeScreenshot("Portrait");

            driver.findElement(By.xpath("//button[contains(., \"Click for answer\")]")).click();

            log("Check answer text");

            // Doesn't seem to work for java-client
            //String style  = driver.findElement(By.xpath("//p[@id=\"result_element\" and contains(., \"Testdroid\")]")).getAttribute("style");

            String pageSource = driver.getPageSource();
            String expectedStyle = "<button id=\"test_button\" style=\"background-color: rgb(127, 255, 0);\">Click for answer</button>";
            Assert.assertTrue(pageSource.contains(expectedStyle));

        } catch (Exception e) {
            takeScreenshot("Test failure");
            log(driver.getPageSource());
            throw e;
        }
    }
}
