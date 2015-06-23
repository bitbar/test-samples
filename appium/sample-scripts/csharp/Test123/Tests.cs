using NUnit.Framework;
using System;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Remote;
using System.Collections.Generic;
using OpenQA.Selenium;
using OpenQA.Selenium.Appium.Android;

namespace TestdroidAndroidSample
{
	[TestFixture ()]
	public class Tests
	{

		AppiumDriver<AndroidElement> driver;

		[TestFixtureSetUp]
		public void BeforeAll()
		{

			String TESTDROID_USERNAME = "Testdroid username";
			String TESTDROID_PASSWORD = "Testdroid Password";

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.SetCapability("device", "Android");

			capabilities.SetCapability("deviceName", "Android");
			capabilities.SetCapability("platformName", "Android");


			capabilities.SetCapability("testdroid_username", TESTDROID_USERNAME);
			capabilities.SetCapability("testdroid_password", TESTDROID_PASSWORD);
			capabilities.SetCapability("testdroid_target", "Android");
			capabilities.SetCapability("testdroid_project", "C# Appium");
			capabilities.SetCapability("testdroid_testrun", "Android Run 1");

			// See available devices at: https://cloud.testdroid.com/#public/devices
			capabilities.SetCapability("testdroid_device", "LG Google Nexus 4 E960 4.3"); // Freemium device
			capabilities.SetCapability("testdroid_app", "sample/BitbarSampleApp.apk"); //to use existing app using "latest" as fileUUID
			driver = new AndroidDriver<AndroidElement>(new Uri("http://appium.testdroid.com/wd/hub"), capabilities, TimeSpan.FromSeconds(180));



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

			driver.FindElement(By.XPath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).Click();
			driver.FindElement(By.XPath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']")).SendKeys("C Sharp");
			driver.Navigate().Back ();
			driver.FindElement(By.XPath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).Click();

		}
	}
}
