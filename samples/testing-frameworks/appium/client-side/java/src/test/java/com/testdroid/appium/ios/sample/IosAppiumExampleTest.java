package com.testdroid.appium.ios.sample;

import com.testdroid.appium.BaseIOSTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

class IosAppiumExampleTest extends BaseIOSTest {

    @BeforeEach
    public void setUp() throws Exception {
        setUpTest();
    }

    @AfterEach
    public void tearDown() {
        quitAppiumSession();
    }

    @Test
    public void mainPageTest() {
        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        wd.findElement(By.xpath("//*[contains(@name, 'answer1')]")).click();
        WebElement element = wd.findElement(By.xpath("//*[contains(@name, 'userName')]"));
        takeScreenshot("answer1_element_clicked");
        element.click();
        element.sendKeys("Testdroid");
        takeScreenshot("answer_typed");
        wd.findElement(By.xpath("//*[contains(@name, 'Return')]")).click();
        takeScreenshot("return_clicked");
        wd.findElement(By.xpath("//*[contains(@name, 'sendAnswer')]")).click();
        takeScreenshot("answer_sent");
    }
}
