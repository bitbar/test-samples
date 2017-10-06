package com.testdroid.appium.ios.sample;

import com.testdroid.appium.ServerSideSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class IosAppiumExampleTest extends ServerSideSetup {

    @BeforeClass
    public void setUp() throws Exception {
        setUpAppiumIOSDriver();
    }
    @AfterClass
    public void tearDown()
    {
        quitAppiumSession();
    }

    @Test
    public void mainPageTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[contains(@name, 'answer2')]")).click();
        WebElement element = driver.findElement(By.xpath("//*[contains(@name, 'userName')]"));
        takeScreenshot("example_screenshot");
        element.click();
        element.sendKeys("Testdroid");
        driver.findElement(By.xpath("//*[contains(@name, 'Return')]")).click();
        driver.findElement(By.xpath("//*[contains(@name, 'sendAnswer')]")).click();
        takeScreenshot("after_answer");
    }
}
