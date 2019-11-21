import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class SimpleGoogleTest extends BaseTest {

    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/";

    private static By searchInput = By.cssSelector("[name='q']");
    private static By searchButton = By.cssSelector(".Tg7LZd");

    private static By searchResultTable = By.id("cnt");
    private static By searchResultItemHeader = By.cssSelector("#rso div.zlBHuf");

    @Test
    public void searchForBitbar() throws InterruptedException {

        // Get driver instance
        WebDriver driver = BaseTest.getDriver();

        // Open main Google Search page
        driver.get(GOOGLE_SEARCH_URL);
        Assert.assertEquals(driver.getCurrentUrl(), GOOGLE_SEARCH_URL);

        // Input "Bitbar" search term and click on search button
        driver.findElement(searchInput).sendKeys("Bitbar");
        driver.findElement(searchButton).click();
        Thread.sleep(3000);
        // Check that Search result table present with search results
        Assert.assertTrue(driver.findElements(searchResultTable).size() > 0);

        // Check that at least one search result item contains word "Bitbar"
        List<WebElement> resultItems = driver.findElements(searchResultItemHeader);
        boolean isPresent = false;
        for (WebElement item : resultItems) {
            if (item.getText().contains("Bitbar"))
                isPresent = true;
        }
        Assert.assertTrue(isPresent);
    }
}
