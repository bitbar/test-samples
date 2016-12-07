package com.testdroid.appium;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public abstract class BaseTest {
    private static final String LOCAL_APPIUM_ADDRESS = "http://localhost:4723";
    private static final String TESTDROID_SERVER = "http://appium.testdroid.com";
    private static final String serverSideTypeDefinition = "serverside";
    private static final String clientSideTypeDefinition = "clientside";
    protected static AppiumDriver wd;
    protected DesiredCapabilities capabilities;

    public void setUpTest() throws IOException {
        setUpAppiumDriver();
    }
    
    public void setUpAppiumDriver() throws IOException {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilitiesFromProperties();
        this.capabilities = desiredCapabilities;

        if (isClientSideTestRun()) {
            String fileUUID = "latest";
            if (isUploadApplication()){
                fileUUID = FileUploader.uploadFile(getTargetAppPath(), getAppiumServerAddress(),
                        getApiKey());
            } 
            capabilities.setCapability("testdroid_app", fileUUID);
            capabilities.setCapability("testdroid_apiKey", getApiKey());
        } else if (isServerSideTestRun()){
            capabilities.setCapability("app", getServerSideApplicationPath());
        } 
        System.out.println("Creating Appium session, this may take couple minutes..");
        setAppiumDriver();
    }
    
    private DesiredCapabilities getDesiredCapabilitiesFromProperties() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        Properties desiredCapabilitiesProperties = fetchProperties(getDesiredCapabilitiesPropertiesFileName());
        Set<String> keys = desiredCapabilitiesProperties.stringPropertyNames();
        for (String key : keys) {
            String value = desiredCapabilitiesProperties.getProperty(key);
            desiredCapabilities.setCapability(key, value);
        }
        return desiredCapabilities;
    }
    
    protected abstract void setAppiumDriver() throws IOException;

    private Properties fetchProperties(String filename) {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                throw new FileNotFoundException("Unable to find/open file: "+filename);
            }
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    protected abstract String getDesiredCapabilitiesPropertiesFileName();

    public boolean isServerSideTestRun() {
        return System.getProperty("executionType").equals(serverSideTypeDefinition);
    }

    public boolean isClientSideTestRun() {
        return System.getProperty("executionType").equals(clientSideTypeDefinition);
    }

    protected String getAppiumServerAddress() {
        if (isClientSideTestRun()){
            return TESTDROID_SERVER;
        }
        return LOCAL_APPIUM_ADDRESS;
    }

    private boolean isUploadApplication() {
        String targetAppPath = getTargetAppPath();
        return targetAppPath != null && !targetAppPath.isEmpty();
    }

    private String getTargetAppPath() {
        String property = System.getProperty("applicationPath"); // TODO virheviesti tai try catch
        return property;
    }

    protected abstract String getServerSideApplicationPath();

    private String getApiKey() {
        String property = System.getProperty("apiKey"); // TODO virheviesti
        return property;
    }

    protected void quitAppiumSession() {
        if (wd != null) {
            wd.quit();
        }
    }

    protected File takeScreenshot(String screenshotName) {
        String fullFileName = System.getProperty("user.dir") + "/screenshots/"+ screenshotName + ".png";
        System.out.println("Taking screenshot...");
        File scrFile = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);

        try {
            File testScreenshot = new File(fullFileName);
            FileUtils.copyFile(scrFile, testScreenshot);
            System.out.println("Screenshot stored to " + testScreenshot.getAbsolutePath());

            return testScreenshot;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}