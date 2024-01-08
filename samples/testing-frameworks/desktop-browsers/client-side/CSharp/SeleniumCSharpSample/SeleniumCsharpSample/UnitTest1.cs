using NUnit.Framework;

using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.Remote;

using System;
using System.Collections.ObjectModel;
using System.IO;

namespace BitbarSeleniumSampleCSharp
{
    public class BitbarSeleniumSample
    {
        IWebDriver driver;
        string screenshot_dir;

        [OneTimeSetUp]
        public void Setup()
        {
            var capabilities = new FirefoxOptions();
            capabilities.PlatformName = "Windows";
            capabilities.BrowserVersion = "120";

            var bitbar_options = new Dictionary<string, string>();
            bitbar_options.Add("project", "Selenium sample project");
            bitbar_options.Add("testrun", "C# Sample Test");
            bitbar_options.Add("apiKey", "<insert your BitBar API key>");
            bitbar_options.Add("osVersion", "10");
            bitbar_options.Add("resolution", "1920x1080");
            bitbar_options.Add("seleniumVersion", "4");


            capabilities.AddAdditionalOption("bitbar:options", bitbar_options);
            screenshot_dir = Environment.CurrentDirectory + "/screenshots'";
            driver = new RemoteWebDriver(new Uri("https://appiumstaging.bitbar.com/wd/hub"), capabilities);
        }

        [Test]
        public void test_sample()
        {
            string test_url = "https://bitbar.github.io/web-testing-target/";
            string expected_title = "Bitbar - Test Page for Samples";
            driver.Navigate().GoToUrl(test_url);
            String title = driver.Title;

            Assert.IsTrue(title == expected_title);

            // Add console log - Website Title here:
            Console.WriteLine(driver.FindElement(By.TagName("h1")));

            // Add Save Screenshot here:
            // Screenshot ss = ((ITakesScreenshot)driver).GetScreenshot();
            // ss.SaveAsFile(screenshot_dir + "Image.png");
        }

        [OneTimeTearDown]
        public void TearDown()
        {
            driver.Quit();
        }
    }
}