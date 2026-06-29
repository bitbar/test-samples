package com.smartbear.appium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Testdroid Sample Test
 */
@ExtendWith(TestResultExtension.class)
class iOSSampleTest extends AbstractAppiumTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(iOSSampleTest.class);

    @BeforeAll
    static void setUp() throws Exception {
        driver = getIOSDriver();
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            LOGGER.info("Quitting Appium driver at tearDown");
            driver.quit();
        } else {
            LOGGER.warn("Driver was null at tearDown");
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
