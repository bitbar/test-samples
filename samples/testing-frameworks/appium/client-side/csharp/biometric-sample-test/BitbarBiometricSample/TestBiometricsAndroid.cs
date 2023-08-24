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
            capabilities.AddAdditionalCapability("appium:automationName", "uiautomator2");
            //Customizable
            capabilities.AddAdditionalCapability("bitbar:options", new Dictionary<string, string>
            {
                {"project", "C# Appium Automated Test"},
                {"testrun", "Android Run" },
                {"device", "Google Pixel 2"},
                {"app", "<APP_ID>" }
                //{"appiumVersion", "1.22.3"} //launch tests on appium 1
            });

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

            var buttonPassBiometrics = driver.FindElement(By.XPath("//*[@text=\"PASS\"]"));
            buttonPassBiometrics.Click();

            var textResult = driver.FindElement(MobileBy.AccessibilityId("Authentication status value"));
            TakeScreenshot("Result");
            Assert.That(textResult.Text.ToUpper, Is.EqualTo("SUCCEEDED"));
        }
    }
}
