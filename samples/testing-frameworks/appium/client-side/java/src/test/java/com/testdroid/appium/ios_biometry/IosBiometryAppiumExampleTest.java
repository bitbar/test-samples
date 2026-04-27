package com.testdroid.appium.ios_biometry;

import com.google.common.collect.ImmutableMap;
import com.testdroid.appium.BaseIOSTest;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;


import java.time.Duration;

class IosBiometryAppiumExampleTest extends BaseIOSTest {



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
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        takeScreenshot("start_screen");
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wd.executeScript("mobile: alert", ImmutableMap.of("action", "accept", "buttonLabel", "OK"));
        wd.findElement(By.xpath("(//*[@name='Biometric authentication'])[3]")).click();
        wd.findElement(By.xpath("//XCUIElementTypeOther[@name='Ask biometric authentication']")).click();
        takeScreenshot("ask_Biometrics");
        wd.executeScript("mobile: alert", ImmutableMap.of("action", "accept", "buttonLabel", "Pass"));
        takeScreenshot("pass_Biometrics");
    }
}
