package com.testdroid.appium.ios.sample;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.testdroid.appium.BaseIOSTest;

import java.util.concurrent.TimeUnit;

public class IosAppiumExampleTest extends BaseIOSTest {

    @BeforeClass
    public void setUp() throws Exception {
        setUpTest();
    }
    @AfterClass
    public void tearDown()
    {
        quitAppiumSession();
    }

    @Test
    public void mainPageTest() throws Exception {
        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        wd.findElement(By.xpath("//*[contains(@name, 'answer1')]")).click();
        WebElement element = wd.findElement(By.xpath("//*[contains(@name, 'userName')]"));
        takeScreenshot("example_screenshot");
        element.click();
        element.sendKeys("Testdroid");
        wd.findElement(By.xpath("//*[contains(@name, 'Return')]")).click();
        wd.findElement(By.xpath("//*[contains(@name, 'sendAnswer')]")).click();
    }


}
