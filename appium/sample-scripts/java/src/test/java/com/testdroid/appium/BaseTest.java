package com.testdroid.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Set;

public abstract class BaseTest {
    private static final String LOCAL_APPIUM_ADDRESS = "http://localhost:4723";
    private static final String TESTDROID_SERVER = "http://appium.testdroid.com";
    private static final String serverSideTypeDefinition = "serverside";
    private static final String clientSideTypeDefinition = "clientside";
    protected AppiumDriver<MobileElement> wd;
    protected DesiredCapabilities capabilities;
    protected static Logger logger = LoggerFactory.getLogger(BaseTest.class);

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
            if (exportTestResultsToCloud()){
                logger.debug("Exporting results enabled");
                capabilities.setCapability("testdroid_junitWaitTime", 300);
            }
            capabilities.setCapability("testdroid_app", fileUUID);
            capabilities.setCapability("testdroid_apiKey", getApiKey());
        } else if (isServerSideTestRun()){
            capabilities.setCapability("app", getServerSideApplicationPath());
        } 
        logger.debug("Creating Appium session, this may take couple minutes..");
        
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
                logger.error("Sorry, unable to find " + filename);
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
        return getExecutionType().equals(serverSideTypeDefinition);
    }

    private String getSystemProperty(String propertyName) {
        String property = System.getProperty(propertyName);
        if (property == null || property.isEmpty()){
            logger.warn(propertyName+" is not defined. To define it, use the following mvn argument: -D"+propertyName+"=<insert_here>");
        }
        return property;
    }

    public boolean isClientSideTestRun() {
        return getExecutionType().equals(clientSideTypeDefinition);
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
        return getSystemProperty("applicationPath");
    }

    protected abstract String getServerSideApplicationPath();

    private String getApiKey() {
        return getSystemProperty("apiKey");
    }
    
    private String getExecutionType() {
        return getSystemProperty("executionType");
    }
    
    protected void quitAppiumSession() {
        if (exportTestResultsToCloud()){
            try{
                PrintWriter writer = new PrintWriter("target/sessionid.txt", "UTF-8");
                writer.println(wd.getSessionId().toString());
                writer.close();
            } catch (IOException e) {
               logger.error("IOError: could not store sessionId for result exporting");
            }
        }
        if (wd != null) {
            wd.quit();
        }
    }

    private boolean exportTestResultsToCloud() {
        boolean isExportResults = getSystemProperty("exportResults") !=null && getSystemProperty("exportResults").equals("true");
        return isClientSideTestRun() && isExportResults;
    }

    protected File takeScreenshot(String screenshotName) {
        String fullFileName = System.getProperty("user.dir") + "/screenshots/"+ screenshotName + ".png";
        logger.debug("Taking screenshot...");
        File scrFile = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);

        try {
            File testScreenshot = new File(fullFileName);
            FileUtils.copyFile(scrFile, testScreenshot);
            logger.debug("Screenshot stored to " + testScreenshot.getAbsolutePath());

            return testScreenshot;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}