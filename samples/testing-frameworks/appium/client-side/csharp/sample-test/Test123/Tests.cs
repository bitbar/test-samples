using NUnit.Framework;
using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using System;
using System.IO;
using System.Net;

namespace TestdroidAndroidSample
{
	[TestFixture]
	public class Tests
	{
		// Make sure that the screenshots folder exists already (testdroid-samples/appium/sample-scripts/csharp/screenshots).
		readonly String SCREENSHOT_FOLDER = Directory.GetParent(Directory.GetCurrentDirectory()).Parent.Parent.FullName + "/screenshots/";
		AndroidDriver<AndroidElement> driver;

		[OneTimeSetUp]
		public void BeforeAll()
		{
			ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
			String BITBAR_APIKEY = "<YOUR BITBAR API KEY>";

			AppiumOptions capabilities = new AppiumOptions();
			capabilities.AddAdditionalCapability("device", "Android");

			capabilities.AddAdditionalCapability("deviceName", "Android");
			capabilities.AddAdditionalCapability("platformName", "Android");
			capabilities.AddAdditionalCapability("bitbar_apiKey", BITBAR_APIKEY);
			capabilities.AddAdditionalCapability("bitbar_project", "C# Appium");
			capabilities.AddAdditionalCapability("bitbar_testrun", "Android Run 1");

			// See available devices at: https://cloud.bitbar.com/#public/devices
			capabilities.AddAdditionalCapability("bitbar_device", "Google Pixel 6");
			capabilities.AddAdditionalCapability("bitbar_app", "<APP ID>");

			Console.WriteLine("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins");
			driver = new AndroidDriver<AndroidElement>(new Uri("https://appium.bitbar.com/wd/hub"), capabilities, TimeSpan.FromSeconds(300));
			Console.WriteLine("WebDriver response received.");

		}

		[OneTimeTearDown]
		public void AfterAll()
		{
			driver.Quit();
		}


		[Test]
		public void TestSampleApp()
		{
			driver.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(30);
			TakeScreenshot("First");
			driver.FindElement(By.XPath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).Click();
			driver.FindElement(By.XPath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']")).SendKeys("C Sharp");
			TakeScreenshot("Second");
			driver.Navigate().Back();
			TakeScreenshot("Third");
			driver.FindElement(By.XPath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).Click();
			TakeScreenshot("Fourth");
		}

		public void TakeScreenshot(String filename)
		{
			Screenshot ss = ((ITakesScreenshot)driver).GetScreenshot();
			String filepath = SCREENSHOT_FOLDER + filename + ".png";
			Console.WriteLine("Taking screenshot: " + filepath);
			ss.SaveAsFile(filepath, ScreenshotImageFormat.Png);
		}
	}
}
