package com.testdroid.appium.android_biometry;

import com.testdroid.appium.BaseAndroidTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;

class AndroidBiometryAppiumExampleTest extends BaseAndroidTest {

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
        takeScreenshot("start");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Biometric authentication']")).click();
        takeScreenshot("First_screen");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Check if avialable']")).click();
        takeScreenshot("Check_biometrics");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Ask biometric authentication']")).click();
        takeScreenshot("Ask_for_authentication");
        wd.findElement(By.xpath("//*[@text=\"FAIL\"]")).click();
        takeScreenshot("Fail_authenticarion");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Ask biometric authentication']")).click();
        takeScreenshot("Ask_for_authentication");
        wd.findElement(By.xpath("//*[@text=\"PASS\"]")).click();
        takeScreenshot("Pass_authentication");
    }
}
