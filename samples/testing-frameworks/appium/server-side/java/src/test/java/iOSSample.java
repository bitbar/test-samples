import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Testdroid Image Recognition Sample Test
 *
 * @author support@bitbar.com
 */
@ExtendWith(TestResultExtension.class)
public class iOSSample extends AbstractAppiumTest {

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
    void mainPageTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.findElement(By.xpath("//*[contains(@name, 'answer1')]")).click();
        WebElement element = driver.findElement(By.xpath("//*[contains(@name, 'userName')]"));
        takeScreenshot("answer1_element_clicked");
        element.click();
        element.sendKeys("Testdroid");
        takeScreenshot("answer_typed");
        driver.findElement(By.xpath("//*[contains(@name, 'Return')]")).click();
        takeScreenshot("return_clicked");
        driver.findElement(By.xpath("//*[contains(@name, 'sendAnswer')]")).click();
        takeScreenshot("answer_sent");
    }
}
