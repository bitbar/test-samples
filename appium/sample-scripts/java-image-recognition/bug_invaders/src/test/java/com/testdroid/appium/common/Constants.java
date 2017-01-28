package com.testdroid.appium.common;

import com.testdroid.appium.TestdroidAppiumClient;

/**
 * Constants used by DriverHandler.java
 *
 * @author Weilin Yu
 */
public class Constants {

    // To run on iOS, change to APPIUM_PLATFORM_IOS
    public static final String platformName = TestdroidAppiumClient.APPIUM_PLATFORM_ANDROID.toString();

    public static final int RETRIES_FIND_IMAGE = 30;

    public static final String APP_FILE_ANDROID = "/absolute/path/to/Testdroid.apk";
    public static final String APP_FILE_IOS = "/absolute/path/to/Testdroid.ipa";

    //Appium required
    public static final String PLATFORM_ANDROID = "Android";

    public static final String[] ANDROID_DEVICES_NAMES = {
            "Asus Google Nexus 7 (2013) ME571KL 4.4.4",
            "Samsung Galaxy Note 3 SM-N900V",
    };

    //Testdroid cloud required
    public static final String PROJECT = "appium android";
    public static final String PROJECT_NAME = "Image recognition POC";
    public static final String[] TESTRUN_NAMES = {
            "Hoobit kabam demo - " + ANDROID_DEVICES_NAMES[0]
    };

    public static final String APPIUM_URL_CLOUD = "http://appium.testdroid.com/wd/hub";
    public static final String APPIUM_URL_LOCALHOST = "http://localhost:4723/wd/hub";

    //iOS
    public static final String IOS_BUNDLE_ID = "com.no.sample.app";

    //Android
    public static final String ANDROID_APPIUM_URL = "http://localhost:4723/wd/hub";
    public static final String ANDROID_PACKAGE_NAME = "com.testdroid.sample.android";
    public static final String ANDROID_ACTIVITY_NAME = "com.testdroid.sample.android.MM_MainMenu";

} //end Constants
