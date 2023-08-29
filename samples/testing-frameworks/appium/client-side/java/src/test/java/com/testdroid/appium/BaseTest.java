package com.testdroid.appium;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public abstract class BaseTest {
    private static final String LOCAL_APPIUM_ADDRESS = "http://localhost:4723";
    private static final String APPIUM_SERVER = "https://appium.bitbar.com";
    private static final String CLOUD_SERVER = "https://cloud.bitbar.com";
    private static final String serverSideTypeDefinition = "serverside";
    private static final String clientSideTypeDefinition = "clientside";
    private static final String clientSideTypeDefinitionWithBiometry = "clientsideWithBiometry";
    protected static Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected DesiredCapabilities capabilities;

    protected abstract AppiumDriver getAppiumDriver();

    public void setUpTest() throws IOException {
        setUpAppiumDriver();
    }

    public void setUpAppiumDriver() throws IOException {
        this.capabilities = getDesiredCapabilitiesFromProperties();
        LOGGER.debug("Creating Appium session, this may take couple minutes..");
        setAppiumDriver();
    }

    private String getAppCapability() {
        String propertiesAppUUID = (String) capabilities.getCapability("bitbar_app");
        if (propertiesAppUUID == null || propertiesAppUUID.isEmpty()) {
            throw new IllegalArgumentException("bitbar_app not defined in desired capabilities");

        }
        LOGGER.debug("bitbar_app defined in properties");
        return propertiesAppUUID;
    }

private DesiredCapabilities getDesiredCapabilitiesFromProperties() throws IOException {
    LOGGER.debug("Setting desiredCapabilities defined in " + getDesiredCapabilitiesPropertiesFileName());

    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    DesiredCapabilities appiumOptions = new DesiredCapabilities();
    DesiredCapabilities bitbarOptions = new DesiredCapabilities();

    Properties desiredCapabilitiesProperties = fetchProperties(getDesiredCapabilitiesPropertiesFileName());
    Set<String> keys = desiredCapabilitiesProperties.stringPropertyNames();

    for (String key : keys) {
        String value = desiredCapabilitiesProperties.getProperty(key);

        if (key.startsWith("appium")) {
            String strippedKey = key.substring("appium_".length());
            appiumOptions.setCapability(strippedKey, value);
        } else if (key.startsWith("bitbar")) {
            String strippedKey = key.substring("bitbar_".length());
            bitbarOptions.setCapability(strippedKey, value);
        } else {
            desiredCapabilities.setCapability(key, value);
        }
    }

    if (isClientSideTestRun() || isClientSideTestRunWithBiometry()) {
        LOGGER.debug("Setting client side specific capabilities...");
        String app;
        if (isUploadApplication()) {
            LOGGER.debug("Uploading " + getTargetAppPath() + " to Testdroid Cloud");
            app = FileUploader.uploadFile(getTargetAppPath(), getCloudServerAddress(), getApiKey());
            LOGGER.debug("File uploaded. File id is " + app);
        } else {
            app = getAppCapability();
        }
        bitbarOptions.setCapability("app", app);
        bitbarOptions.setCapability("apiKey", getApiKey());
        LOGGER.debug("Setting client side specific capabilities... FINISHED");
    } else if (isServerSideTestRun()) {
        LOGGER.debug("Setting server side specific capabilities...");
        appiumOptions .setCapability("app", getServerSideApplicationPath());
        if (System.getenv("AUTOMATION_NAME") != null) {
            appiumOptions.setCapability("automationName", System.getenv("AUTOMATION_NAME"));
        }
        LOGGER.debug("Setting server side specific capabilities... FINISHED");
    }

    desiredCapabilities.setCapability("appium:options", appiumOptions);
    desiredCapabilities.setCapability("bitbar:options", bitbarOptions);
    return desiredCapabilities;
}
    protected abstract void setAppiumDriver() throws IOException;

    private Properties fetchProperties(String filename) {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                LOGGER.error("Sorry, unable to find " + filename);
                throw new FileNotFoundException("Unable to find/open file: " + filename);
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

    public boolean isClientSideTestRun() {
        return getExecutionType().equals(clientSideTypeDefinition);
    }

    public boolean isClientSideTestRunWithBiometry() {
        return getExecutionType().equals(clientSideTypeDefinitionWithBiometry);
    }

    private boolean isUploadApplication() {
        String targetAppPath = getTargetAppPath();
        return targetAppPath != null && !targetAppPath.isEmpty();
    }

    private String getTargetAppPath() {
        String propertyName = "applicationPath";
        return System.getProperty(propertyName);
    }

    protected abstract String getServerSideApplicationPath();

    private String getApiKey() {
        String propertyName = "apiKey";
        String property = System.getProperty(propertyName);
        if (property == null || property.isEmpty()) {
            LOGGER.warn(propertyName + " mvn argument is not defined. To define it, use the following mvn argument: -D" + propertyName + "=<insert_here>");
        }
        return property;
    }

    protected String getCloudServerAddress() {
        String property = System.getProperty("cloudServerAddress");
        if (StringUtils.isNotBlank(property)) {
            return property;
        } else {
            return CLOUD_SERVER;
        }
    }

    protected String getAppiumServerAddress() {
        String property = System.getProperty("appiumServerAddress");
        if (StringUtils.isNotBlank(property)) {
            return property;
        } else if (isClientSideTestRun() || isClientSideTestRunWithBiometry()) {
            return APPIUM_SERVER;
        }
        return LOCAL_APPIUM_ADDRESS;
    }

    private String getExecutionType() {
        String propertyName = "executionType";
        String property = System.getProperty(propertyName);
        if (property == null || property.isEmpty()) {
            LOGGER.warn(propertyName + " mvn argument is not defined. To define it, use the following mvn argument: -D" + propertyName + "=<insert_here>");
        }
        return property;
    }

    protected void quitAppiumSession() {
        if (exportTestResultsToCloud()) {
            try {
                PrintWriter writer = new PrintWriter("target/sessionid.txt", "UTF-8");
                writer.println(getAppiumDriver().getSessionId().toString());
                writer.close();
            } catch (IOException e) {
                LOGGER.error("IOError: could not store sessionId for result exporting");
            }
        }
        if (getAppiumDriver() != null) {
            getAppiumDriver().quit();
        }
    }

    private boolean exportTestResultsToCloud() {
        boolean isExportResults = System.getProperty("exportResults") != null && System.getProperty("exportResults").equals("true");
        return isClientSideTestRun() && isExportResults;
    }

    protected File takeScreenshot(String screenshotName) {
        String fullFileName = System.getProperty("user.dir") + "/screenshots/" + screenshotName + ".png";
        LOGGER.debug("Taking screenshot...");
        File scrFile = getAppiumDriver().getScreenshotAs(OutputType.FILE);

        try {
            File testScreenshot = new File(fullFileName);
            FileUtils.copyFile(scrFile, testScreenshot);
            LOGGER.debug("Screenshot stored to " + testScreenshot.getAbsolutePath());

            return testScreenshot;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
