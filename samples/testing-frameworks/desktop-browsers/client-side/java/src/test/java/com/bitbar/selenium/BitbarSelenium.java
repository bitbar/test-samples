package com.bitbar.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;


public class BitbarSelenium {

    public static final String URL = "https://us-west-desktop-hub.bitbar.com/wd/hub";

    public static void main(String[] args) throws Exception {
        // IMPORTANT: Set the following parameters according to your needs.
        // You can use Capabilities creator:
        // https://cloud.bitbar.com/#public/capabilities-creator
        // Please mind apiKey is required and can be found at
        // https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)

        // user-customizable parameters start here
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "latest");

        HashMap<String, String> bitbarOptions = new HashMap<String, String>();
        bitbarOptions.put("project", "Selenium sample project");
        bitbarOptions.put("testrun", "Java sample test");
        bitbarOptions.put("apiKey", "<insert your BitBar API key here>");
        bitbarOptions.put("osVersion", "11");
        bitbarOptions.put("resolution", "1920x1080");
        capabilities.setCapability("bitbar:options", bitbarOptions);
        // user-customizable parameters end here

        WebDriver driver = new RemoteWebDriver(new URL(URL), capabilities);

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
