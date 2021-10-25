package com.bitbar.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;


public class BitbarSelenium {

    public static void main(String[] args) throws Exception {
        // IMPORTANT: Set the following parameters according to your needs.
        // You can use Capabilities creator:
        // https://cloud.bitbar.com/#public/capabilities-creator
        // Please mind bitbar_apiKey is required and can be found at
        // https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)

        // user-customizable parameters start here
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("bitbar_apiKey", "<insert your Bitbar API key here>");
        capabilities.setCapability("platform", "Windows");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("version", "94");
        capabilities.setCapability("resolution", "1920x1080");
        capabilities.setCapability("bitbar_project", "Selenium sample project");
        capabilities.setCapability("bitbar_testrun", "Java sample test");
        capabilities.setCapability("bitbar_testTimeout", "600");
        // user-customizable parameters end here

        String hub_url;
        String platform = capabilities.getPlatform().toString().toLowerCase();

        if (platform.equals("windows")) {
            hub_url = "https://westeurope-hub.bitbar.com/wd/hub";
        } else if (platform.equals("linux")) {
            hub_url = "https://broker-cloud.bitbar.com/wd/hub";
        } else {
            throw new Exception("Unsupported platform");
        }

        WebDriver driver = new RemoteWebDriver(new URL(hub_url), capabilities);

        // check page title
        String test_url = "https://bitbar.github.io/web-testing-target/";
        driver.get(test_url);
        String title = driver.getTitle();
        System.out.println(title);
        String expected_title = "Bitbar - Test Page for Samples";
        assert title.equals(expected_title) : "Wrong page title";

        // clicking "Click for answer" button
        WebElement button = driver.findElement(By.xpath("//button[contains(., \"Click for answer\")]"));
        button.click();

        // check answer text
        driver.findElement(By.xpath("//p[@id=\"result_element\" and contains(., \"Bitbar\")]"));
        System.out.println(driver.findElement(By.id("result_element")).getText());

        // verify button changed color
        String expected_style = "background-color: rgb(127, 255, 0);";
        String style = button.getAttribute("style");
        assert style.equals(expected_style) : "Wrong button styling";

        driver.quit();

    }
}
