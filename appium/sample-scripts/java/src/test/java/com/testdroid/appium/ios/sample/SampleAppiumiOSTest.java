package com.testdroid.appium.ios.sample;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.testdroid.api.http.MultipartFormDataContent;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleAppiumiOSTest {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TESTDROID_USERNAME = "admin@localhost";
    private static final String TESTDROID_PASSWORD = "admin";

    private static final String TESTDROID_SERVER = "http://appium.testdroid.com";

    private static final String TARGET_APP_PATH = "/your/application/path";
    private static AppiumDriver wd;

    @BeforeClass
    public static void setUp() throws Exception {

        String fileUUID = uploadFile(TARGET_APP_PATH);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");


        capabilities.setCapability("device", "iphone");
        capabilities.setCapability("deviceName", "iOS Phone");


        capabilities.setCapability("testdroid_username", TESTDROID_USERNAME);

        capabilities.setCapability("testdroid_password", TESTDROID_PASSWORD);
        capabilities.setCapability("testdroid_project", "LocaliOSAppium");
        capabilities.setCapability("testdroid_description", "Appium project description");
        capabilities.setCapability("testdroid_testrun", "iOS Run");
        capabilities.setCapability("testdroid_device", "iPhone 4S A1387 6.1.3");
        capabilities.setCapability("testdroid_app", fileUUID);
        capabilities.setCapability("app", "com.bitbar.testdroid.BitbarIOSSample");
        System.out.println("Capabilities:" + capabilities.toString());
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
        FileContent fileContent = new FileContent("application/zip", new File(filePath));

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
        wd.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIAButton[1]")).click();
        wd.findElement(By.name("userName")).sendKeys("sakari");
        wd.findElement(By.name("Return")).click();
        wd.findElement(By.name("sendAnswer")).click();

    }


}
