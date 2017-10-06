package com.testdroid.appium.android.sample;

import com.testdroid.appium.ServerSideSetup;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AndroidAppiumExampleTest  extends ServerSideSetup {

    @BeforeClass
    public void setUp() throws Exception {
        setUpAppiumAndroidDriver();
    }
    @AfterClass
    public void tearDown()
    {
        quitAppiumSession();
    }


    @Test
    public void mainPageTest() throws IOException, InterruptedException {
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        takeScreenshot("start");
        driver.findElement(By.xpath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).click();
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']")).sendKeys("John Doe");
        takeScreenshot("after_adding_name");
        driver.hideKeyboard();
        driver.findElement(By.xpath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).click();
        takeScreenshot("after_answer");
    }

}
