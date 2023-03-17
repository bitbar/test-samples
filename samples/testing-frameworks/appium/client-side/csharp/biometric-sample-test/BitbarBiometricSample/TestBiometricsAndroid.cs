using OpenQA.Selenium.Appium.Android;

namespace BitbarBiometricSample
{
    [TestFixture]
    public class TestBiometricsAndroid : BaseBiometricTest
    {
        [OneTimeSetUp]
        public void InitDriver()
        {
            //Platform dependent
            capabilities.AddAdditionalCapability("platformName", "Android");
            capabilities.AddAdditionalCapability("deviceName", "Android Phone");
            capabilities.AddAdditionalCapability("automationName", "Appium");
            //Customizable
            capabilities.AddAdditionalCapability("bitbar_project", "C# Appium Automated Test");
            capabilities.AddAdditionalCapability("bitbar_testrun", "Android Run");
            capabilities.AddAdditionalCapability("bitbar_device", "Google Pixel 2");
            //App ID
            capabilities.AddAdditionalCapability("bitbar_app", "<APP_ID>");

            driver = new AndroidDriver<AndroidElement>(new Uri(AppiumHubURL), capabilities, TimeSpan.FromSeconds(300));
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

            TakeScreenshot("HomeScreen");
            var buttonBiometrics = driver.FindElement(By.XPath("//android.view.ViewGroup[@content-desc=\"Biometric authentication\"]"));
            buttonBiometrics.Click();

            var textSensorType = driver.FindElement(MobileBy.AccessibilityId("Sensor type value"));
            Assert.That(textSensorType.Text.ToUpper, Is.EqualTo("ANDROID BIOMETRICS"));

            var buttonAskBiometrics = driver.FindElement(By.XPath("//android.view.ViewGroup[@content-desc=\"Ask biometric authentication\"]"));
            buttonAskBiometrics.Click();
            TakeScreenshot("Dialog");

            var buttonPassBiometrics = driver.FindElement(By.XPath("//*[@text=\"Pass\"]"));
            buttonPassBiometrics.Click();

            var textResult = driver.FindElement(MobileBy.AccessibilityId("Authentication status value"));
            TakeScreenshot("Result");
            Assert.That(textResult.Text.ToUpper, Is.EqualTo("SUCCEEDED"));
        }
    }
}
