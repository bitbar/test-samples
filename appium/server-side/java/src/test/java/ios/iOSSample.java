package ios;

import common.*;
import io.appium.java_client.MobileElement;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.*;

/**
 * Testdroid Server side iOS Sample Test
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

public class iOSSample extends AbstractAppiumTest {

    public iOSSample() throws Exception {
        super();
        logger = LoggerFactory.getLogger(SafariSample.class);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        appFile = "application.ipa";
        browserName = null;
        driver = getIOSDriver();
    }

    @AfterClass
    public static void tearDown() {
        log("Quitting Appium driver");
        driver.quit();
    }

    @Test
    public void iosSampleTest() throws Exception {
        log("Image Recognition sample script started.");
        takeScreenshot("Beginning");
        try {
          driver.hideKeyboard();
        } catch (Exception e) {
          log("Keyboard not present; going forward.");
        }

        log("Click on 'answer1'");
        driver.findElement(By.xpath("//*[contains(@name, 'answer1')]")).click();
        log("sendKeys 'Testdroid'");
        MobileElement element = driver.findElement(By.xpath("//*[contains(@name, 'userName')]"));
        element.click();
        element.sendKeys("Testdroid");
        log("Click on 'Return'");
        driver.findElement(By.xpath("//*[contains(@name, 'Return')]")).click();
        log("Click on 'sendAnswer'");
        driver.findElement(By.xpath("//*[contains(@name, 'sendAnswer')]")).click();
        takeScreenshot("End");
    }
}
