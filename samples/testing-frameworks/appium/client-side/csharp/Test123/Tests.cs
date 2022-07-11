using NUnit.Framework;
using System;
using System.Drawing.Imaging;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Remote;
using System.Collections.Generic;
using System.IO;
using OpenQA.Selenium;
using OpenQA.Selenium.Appium.Android;

namespace TestdroidAndroidSample
{
	[TestFixture]
	public class Tests
	{
		//Default screenshots output folder (testdroid-samples/appium/sample-scripts/csharp/screenshots)
		//Make sure that the screenshots folder exists already.
		readonly String SCREENSHOT_FOLDER = Directory.GetParent(Directory.GetCurrentDirectory()).Parent.Parent.FullName + "/screenshots/";
		AppiumDriver<AndroidElement> driver;

		[TestFixtureSetUp]
		public void BeforeAll()
		{

			//String BITBAR_USERNAME = "Testdroid username";
			//String BITBAR_PASSWORD = "Testdroid Password";
			String BITBAR_APIKEY = "Your bitbar api key";	//either username & password or apikey

			DesiredCapabilities capabilities = new DesiredCapabilities();

			capabilities.SetCapability("deviceName", "Android"); //"Android"
			capabilities.SetCapability("platformName", "Android");


			//capabilities.SetCapability("testdroid_username", BITBAR_USERNAME);
			//capabilities.SetCapability("testdroid_password", BITBAR_PASSWORD);
			capabilities.SetCapability("bitbar_apiKey", BITBAR_APIKEY);
			capabilities.SetCapability("bitbar_target", "Android");
			capabilities.SetCapability("bitbar_project", "C# Appium");
			capabilities.SetCapability("bitbar_testrun", "Android Run 1");

			// See available devices at: https://cloud.bitbar.com/#public/devices
			capabilities.SetCapability("device", "(new) Motorola Moto G100 5G /03"); // Freemium device
			capabilities.SetCapability("bitbar_app", "sample/bitbar-sample-app.apk"); //to use existing app using "latest" as fileUUID

			Console.WriteLine ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins");
			driver = new AndroidDriver<AndroidElement>(new Uri("https://appium.bitbar.com/wd/hub"), capabilities, TimeSpan.FromSeconds(300)); 
			Console.WriteLine ("WebDriver response received.");

		}

		[TestFixtureTearDown]
		public void AfterAll()
		{
			driver.Quit();
		}


		[Test]
		public void TestSampleApp()
		{
			driver.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(60);
			TakeScreenshot ("First");
			driver.FindElement(By.XPath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).Click();
			driver.FindElement(By.XPath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']")).SendKeys("C Sharp");
			TakeScreenshot ("Second");
			driver.Navigate().Back ();
			TakeScreenshot ("Third");
			driver.FindElement(By.XPath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).Click();
			TakeScreenshot ("Fourth");
		}

		public void TakeScreenshot(String filename)
		{
			Screenshot ss = ((ITakesScreenshot)driver).GetScreenshot();
			String filepath = SCREENSHOT_FOLDER + filename + ".png";
			Console.WriteLine ("Taking screenshot: " + filepath);
			ss.SaveAsFile (filepath, ScreenshotImageFormat.Png);
		}
	}
}
