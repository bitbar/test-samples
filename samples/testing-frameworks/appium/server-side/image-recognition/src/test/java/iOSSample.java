import objects.ImageSearchResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Testdroid Image Recognition Sample Test
 *
 * @author support@bitbar.com
 */
@ExtendWith(TestResultExtension.class)
public class iOSSample extends TestdroidImageRecognition {

    @BeforeAll
    public static void setUp() throws Exception {
        driver = getIOSDriver();
    }

    @AfterAll
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
        log("Image Recognition sample script started.");
        takeScreenshot("Before hideKeyboard");
        hideKeyboard();
        ImageSearchResult result = findImageOnScreen("bitbar_logo.png");
        assert (result.isFound());
        log("Success.");
    }
}
