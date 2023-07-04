import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.time.Duration;

/**
 * Testdroid Image Recognition Sample Test
 *
 * @author support@bitbar.com
 */
@ExtendWith(TestResultExtension.class)
public class AndroidSample extends AbstractAppiumTest {

    @BeforeAll
    public static void setUp() throws Exception {
        driver = getAndroidDriver();
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
    void mainPageTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        takeScreenshot("start");
        driver.findElement(By.xpath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).click();
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']"))
            .sendKeys("John Doe");
        takeScreenshot("after_adding_name");
        driver.navigate().back();
        driver.findElement(By.xpath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget" +
                ".LinearLayout[2]/android.widget.Button[1]"))
            .click();
        takeScreenshot("after_answer");
    }

}
