using NUnit.Framework;
using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using System;
using System.IO;
using System.Net;
using System.Collections.Generic;

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
			string BITBAR_APIKEY = "<YOUR BITBAR API KEY>";
			string BITBAR_APP =  "<ID OF YOUR APP>";

			AppiumOptions capabilities = new AppiumOptions();
			capabilities.AddAdditionalCapability("platformName", "Android");
			capabilities.AddAdditionalCapability("appium:automationName", "uiautomator2");
			capabilities.AddAdditionalCapability("bitbar:options", new Dictionary<string, string>
			{
				{"project", "C# Appium"},
				{"testrun", "Android Run 1"},
				{"device", "Google Pixel 6"}, // See available devices at: https://cloud.bitbar.com/#public/devices
				{"apiKey", BITBAR_APIKEY},
				{"app", BITBAR_APP},
				//{"appiumVersion", "1.22.3"} //launch tests on appium 1
			});
			
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
