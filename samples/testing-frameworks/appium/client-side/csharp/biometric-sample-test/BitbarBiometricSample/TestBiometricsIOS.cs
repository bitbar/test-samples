using OpenQA.Selenium.Appium.iOS;
using OpenQA.Selenium.Support.UI;

namespace BitbarBiometricSample
{
    [TestFixture]
    public class TestBiometricsIOS : BaseBiometricTest
    {
        [OneTimeSetUp]
        public void InitDriver()
        {
            //Platform dependent
            capabilities.AddAdditionalCapability("platformName", "iOS");
            capabilities.AddAdditionalCapability("deviceName", "iPhone device");
            capabilities.AddAdditionalCapability("automationName", "XCUITest");
            //Customizable
            capabilities.AddAdditionalCapability("bitbar_project", "C# Appium Automated Test");
            capabilities.AddAdditionalCapability("bitbar_testrun", "iOS Run");
            capabilities.AddAdditionalCapability("bitbar_device", "Apple iPhone 11");
            //App ID
            capabilities.AddAdditionalCapability("bitbar_app", "<APP_ID>");

            Console.WriteLine("Sending request to start a session. Waiting for response typically takes 2-3 minutes...");
            driver = new IOSDriver<IOSElement>(new Uri(AppiumHubURL), capabilities, TimeSpan.FromSeconds(300));
            Console.WriteLine("Received response: ");
        }

        [OneTimeTearDown]
        public void QuitDriver()
        {
            driver.Quit();
        }

        [Test]
        public void Test_Biometric_Authentication()
        {
            Console.WriteLine("Test execution started:");

            //Accept initial biometric dialog
            var wait = new WebDriverWait(driver, TimeSpan.FromSeconds(10)).Until(SeleniumExtras.WaitHelpers.ExpectedConditions.ElementExists(By.XPath("//XCUIElementTypeAlert[@name=\"Biometric Authentication\"]")));
            driver.ExecuteScript("mobile: alert", new Dictionary<string, string> { { "action", "accept" }, { "buttonLabel", "OK" } });

            TakeScreenshot("HomeScreen");
            var buttonBiometrics = driver.FindElement(By.XPath("(//*[@name=\"Biometric authentication\"])[3]"));
            buttonBiometrics.Click();

            var textSensorType = driver.FindElement(MobileBy.AccessibilityId("Sensor type value"));
            Assert.That(textSensorType.Text.ToUpper, Is.EqualTo("TOUCHID").Or.EqualTo("FACEID"));

            var buttonAskBiometrics = driver.FindElement(By.XPath("//XCUIElementTypeOther[@name=\"Ask biometric authentication\"]"));
            buttonAskBiometrics.Click();
            TakeScreenshot("Dialog");

            driver.ExecuteScript("mobile: alert", new Dictionary<string, string> { { "action", "accept" }, { "buttonLabel", "Pass" } });

            var textResult = driver.FindElement(MobileBy.AccessibilityId("Authentication status value"));
            TakeScreenshot("Result");
            Assert.That(textResult.Text.ToUpper, Is.EqualTo("SUCCEEDED"));
        }
    }
}
