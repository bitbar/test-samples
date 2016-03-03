import com.testdroid.appium.TestdroidAppiumClient;
import com.testdroid.appium.TestdroidAppiumDriverAndroid;
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

public class AndroidSample extends TestdroidImageRecognitionAndroid {
    public static TestdroidAppiumDriverAndroid driver;
    public static TestdroidAppiumClient client;

    public AndroidSample() throws Exception {
        super();
        logger = LoggerFactory.getLogger(AndroidSample.class);
        Thread.currentThread().setName("AndroidSample - " + deviceName);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        client = new TestdroidAppiumClient();
        // ios, android, selendroid, chrome or safari
        client.setTestdroidTarget(client.TESTDROID_TARGET_ANDROID);
        driver = getDriverAndroid(client);
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
