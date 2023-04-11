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
        takeScreenshot("start_screen");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Biometric authentication']")).click();
        takeScreenshot("second_screen");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Check if avialable']")).click();
        takeScreenshot("check_biometrics");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Ask biometric authentication']")).click();
        takeScreenshot("ask_for_authentication");
        wd.findElement(By.xpath("//*[@text=\"FAIL\"]")).click();
        takeScreenshot("fail_authenticarion");
        wd.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Ask biometric authentication']")).click();
        takeScreenshot("ask_for_authentication");
        wd.findElement(By.xpath("//*[@text=\"PASS\"]")).click();
        takeScreenshot("pass_authentication");
    }
}
