import com.testdroid.appium.TestdroidAppiumClient;
import com.testdroid.appium.TestdroidAppiumDriverIos;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.slf4j.LoggerFactory;
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

public class iOSSample extends TestdroidImageRecognitionIos {
    public static TestdroidAppiumDriverIos driver;
    public static TestdroidAppiumClient client;

    public iOSSample() throws Exception {
        super();
        logger = LoggerFactory.getLogger(iOSSample.class);
        Thread.currentThread().setName("iOSSample - " + deviceName);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        client = new TestdroidAppiumClient();
        // ios, android, selendroid, chrome or safari
        client.setTestdroidTarget(client.TESTDROID_TARGET_IOS);
        driver = getDriverIos(client);
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("Quitting Appium driver");
        driver.quit();
    }

    @Test
    public void mainPageTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        log("Image Recognition sample script started.");

        try {
          driver.hideKeyboard();
        } catch (Exception e) {
          log("Keyboard not present; going forward.");
        }

        findImageOnScreen("bitbar_logo");
    }
}
