package com.testdroid.appium.android.sample;

import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import com.testdroid.api.http.MultipartFormDataContent;
import io.appium.java_client.AppiumDriver;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleAppiumTest {
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TESTDROID_USERNAME = "accountof@testdroidcloud.com";
    private static final String TESTDROID_PASSWORD = "password";

    private static final String TARGET_APP_PATH = "../../../apps/builds/BitbarSampleApp.apk";
    private static final String TESTDROID_SERVER = "http://appium.testdroid.com";
    private static int counter;

    private static AppiumDriver wd;

    @BeforeClass
    public static void setUp() throws Exception {
        counter = 0;

        String fileUUID = uploadFile(TARGET_APP_PATH);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("testdroid_target", "Android");
        capabilities.setCapability("deviceName", "Android Device");
        
        capabilities.setCapability("testdroid_username", TESTDROID_USERNAME);
        capabilities.setCapability("testdroid_password", "******");
        
        capabilities.setCapability("testdroid_project", "LocalAppium");
        capabilities.setCapability("testdroid_testrun", "Android Run 1");
        
        // See available devices at: https://cloud.testdroid.com/#public/devices
        //capabilities.setCapability("testdroid_device", "Dell Venue 7 3730"); // Freemium device
        capabilities.setCapability("testdroid_device", "Asus Memo Pad 8 K011"); // Freemium device
        capabilities.setCapability("testdroid_app", fileUUID); //to use existing app using "latest" as fileUUID

        // Optional
        //capabilities.setCapability("testdroid_description", "Appium project description");
        //capabilities.setCapability("platformVersion", "4.4.2");
        //capabilities.setCapability("app-activity", ".BitbarSampleApplicationActivity");
        //capabilities.setCapability("app", "com.bitbar.testdroid");
        
        System.out.println("Capabilities:" + capabilities.toString());
        capabilities.setCapability("testdroid_password", TESTDROID_PASSWORD);
        
        System.out.println("Creating Appium session, this may take couple minutes..");
        wd = new AppiumDriver(new URL(TESTDROID_SERVER+"/wd/hub"), capabilities);
    }
    @AfterClass
    public static void tearDown()
    {
        wd.quit();
    }
    private static String uploadFile(String filePath) throws IOException {
        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(TESTDROID_USERNAME, TESTDROID_PASSWORD);

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                        request.setHeaders(headers);
                    }


                });
        MultipartFormDataContent multipartContent = new MultipartFormDataContent();
        FileContent fileContent = new FileContent("application/octet-stream", new File(filePath));

        MultipartFormDataContent.Part filePart = new MultipartFormDataContent.Part("file", fileContent);
        multipartContent.addPart(filePart);

        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(TESTDROID_SERVER+"/upload"), multipartContent);

        HttpResponse response = request.execute();
        System.out.println("response:" + response.parseAsString());

        AppiumResponse appiumResponse = request.execute().parseAs(AppiumResponse.class);
        System.out.println("File id:" + appiumResponse.uploadStatus.fileInfo.file);

        return appiumResponse.uploadStatus.fileInfo.file;

    }

    public static class AppiumResponse {
        Integer status;
        @Key("sessionId")
        String sessionId;

        @Key("value")
        UploadStatus uploadStatus;

    }

    public static class UploadedFile {
        @Key("file")
        String file;
    }

    public static class UploadStatus {
        @Key("message")
        String message;
        @Key("uploadCount")
        Integer uploadCount;
        @Key("expiresIn")
        Integer expiresIn;
        @Key("uploads")
        UploadedFile fileInfo;
    }

    @Test
    public void mainPageTest() throws IOException, InterruptedException {

        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        takeScreenshot("First screen");
        wd.findElement(By.xpath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).click();
        wd.findElement(By.xpath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']")).sendKeys("John Doe");
        takeScreenshot("Text placed");
        wd.navigate().back();
        wd.findElement(By.xpath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).click();
        takeScreenshot("Answer given");
    }
    
    public void takeScreenshot(String screenshotName) {
        counter = counter + 1;
        String fullFileName = System.getProperty("user.dir") + "/Screenshots/" + getScreenshotsCounter() + "_" + screenshotName + ".png";

        screenshot(fullFileName);
    }
    
    public File screenshot(String name) {
        System.out.println("Taking screenshot...");
        File scrFile = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);

        try {

            File testScreenshot = new File(name);
            FileUtils.copyFile(scrFile, testScreenshot);
            System.out.println("Screenshot stored to " + testScreenshot.getAbsolutePath());

            return testScreenshot;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getScreenshotsCounter() {
        if (counter < 10) {
            return "0" + counter;
        } else {
            return String.valueOf(counter);
        }
    }

}
