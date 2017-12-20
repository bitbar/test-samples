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
	[TestFixture ()]
	public class Tests
	{

		AppiumDriver<AndroidElement> driver;
		// Make sure that the screenshots folder exists already (testdroid-samples/appium/sample-scripts/csharp/screenshots).

		[TestFixtureSetUp]
		public void BeforeAll()
		{

			//String TESTDROID_USERNAME = "Testdroid username";
			//String TESTDROID_PASSWORD = "Testdroid Password";
			String TESTDROID_APIKEY = "Testdroid apikey";	//either username & password or apikey

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.SetCapability("device", "Android");

			capabilities.SetCapability("deviceName", "Android");
			capabilities.SetCapability("platformName", "Android");


			//capabilities.SetCapability("testdroid_username", TESTDROID_USERNAME);
			//capabilities.SetCapability("testdroid_password", TESTDROID_PASSWORD);
			capabilities.SetCapability("testdroid_apiKey", TESTDROID_APIKEY);
			capabilities.SetCapability("testdroid_target", "Android");
			capabilities.SetCapability("testdroid_project", "C# Appium");
			capabilities.SetCapability("testdroid_testrun", "Android Run 1");

			// See available devices at: https://cloud.bitbar.com/#public/devices
			capabilities.SetCapability("testdroid_device", "Dell Venue 7 3730"); // Freemium device
			capabilities.SetCapability("testdroid_app", "sample/BitbarSampleApp.apk"); //to use existing app using "latest" as fileUUID

			Console.WriteLine ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins");
			driver = new AndroidDriver<AndroidElement>(new Uri("http://appium.bitbar.com/wd/hub"), capabilities, TimeSpan.FromSeconds(300)); 
			Console.WriteLine ("WebDriver response received.");


		}

		[TestFixtureTearDown]
		public void AfterAll()
		{
			driver.Quit();
		}


		[Test ()]
		public void TestSampleApp()
		{
			driver.Manage ().Timeouts ().ImplicitlyWait (TimeSpan.FromSeconds(60));
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
			String path = Directory.GetParent(Directory.GetCurrentDirectory()).Parent.Parent.FullName;
			String filepath = path + "/screenshots/" + filename + ".png";
			Console.WriteLine ("Taking screenshot: " + filepath);
			ss.SaveAsFile (filepath, ImageFormat.Png);
		}
	}
}
