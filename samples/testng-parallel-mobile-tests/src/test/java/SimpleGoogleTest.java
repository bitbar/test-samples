import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;


public class SimpleGoogleTest extends BaseTest {

    private static final String BITBAR_TEST_URL = "https://bitbar.github.io/web-testing-target/";

    private static By clickForAnswerButton = By.id("submit_button");
    private static By resultElement = By.xpath("//p[@id=\"result_element\" and contains(., \"Bitbar\")]");



    @Test
    public void searchForBitbar() throws InterruptedException {

        // Get driver instance
        WebDriver driver = BaseTest.getDriver();

        // Open Bitbar testing target site
        driver.get(BITBAR_TEST_URL);
        Assert.assertEquals(driver.getCurrentUrl(), BITBAR_TEST_URL);
        Thread.sleep(3000);

        // Press "Click for answer" button
        driver.findElement(clickForAnswerButton).click();
        Thread.sleep(3000);

        // Check answer text
        driver.findElement(resultElement);

        // Check if button color has changed
        String style = driver.findElement(clickForAnswerButton).getAttribute("style");
        String expectedStyle = "rgb(127, 255, 0";

        Assert.assertTrue(style.contains(expectedStyle));
        
    }
}
