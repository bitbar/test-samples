package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    private static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    private String deviceName;
    private String bitbarDevice;
    private String browserName;
    private String platformName;
    private String automationName;

    @BeforeMethod
    @Parameters(value = {"Device"})
    public void setUp(String device, ITestContext context) throws MalformedURLException {
        setupRemoteDriver(device, context.getCurrentXmlTest().getName());

        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private void setupRemoteDriver(String device, String xmlTestName) throws MalformedURLException {
        loadProperties(device);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("bitbar_apiKey", System.getProperty("BITBAR_API_KEY"));
        capabilities.setCapability("bitbar_testrun", this.getClass().getSimpleName());
        capabilities.setCapability("bitbar_project", xmlTestName);
        capabilities.setCapability("bitbar_device", bitbarDevice);
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", deviceName);
        if (automationName != null)
            capabilities.setCapability("automationName", automationName);

        driver.set(new RemoteWebDriver(new URL("https://appium.bitbar.com/wd/hub/"), capabilities));
    }

    private void loadProperties(String device) {
        FileInputStream propertiesFIS;
        Properties properties = new Properties();
        String propertiesFilePath = String.format("src/test/resources/properties/%s.properties", device);

        try {
            propertiesFIS = new FileInputStream(propertiesFilePath);
            properties.load(propertiesFIS);

            bitbarDevice = properties.getProperty("bitbar_device");
            browserName = properties.getProperty("browser_name");
            platformName = properties.getProperty("platform_name");
            deviceName = properties.getProperty("device_name");
            if (properties.getProperty("automation_name") != null)
                automationName = properties.getProperty("automation_name");

        } catch (IOException e) {
            System.out.println("Properties file is missing or invalid! Check path to file: " + propertiesFilePath);
            System.exit(0);
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null)
            getDriver().quit();
    }
}
