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
        takeScreenshot("answer1_element_clicked");
        element.click();
        element.sendKeys("Testdroid");
        wd.hideKeyboard();
        takeScreenshot("answer_typed");
        wd.findElement(By.xpath("//*[contains(@name, 'Return')]")).click();
        takeScreenshot("return_clicked");
        wd.findElement(By.xpath("//*[contains(@name, 'sendAnswer')]")).click();
        takeScreenshot("answer_sent");
    }


}
