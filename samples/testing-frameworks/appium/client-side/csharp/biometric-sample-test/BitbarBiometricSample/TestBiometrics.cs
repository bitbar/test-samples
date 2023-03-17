namespace BitbarBiometricSample
{
    [TestFixture]
    public class TestBiometrics
    {
        private readonly String SCREENSHOT_DIRECTORY = Directory.GetParent(Directory.GetCurrentDirectory()).Parent.Parent.FullName + "/screenshots/";
        private RemoteWebDriver driver;

        [OneTimeSetUp]
        public void InitDriver()
        {
            const String AppiumHubURL = "https://appium.bitbar.com/wd/hub";

            AppiumOptions capabilities = new();
            capabilities.AddAdditionalCapability("platformName", "Android");
            capabilities.AddAdditionalCapability("deviceName", "Android Phone");
            capabilities.AddAdditionalCapability("automationName", "Appium");

            //Customizable
            capabilities.AddAdditionalCapability("bitbar_project", "C# Appium Automated Test");
            capabilities.AddAdditionalCapability("bitbar_testrun", "Android Run");
            capabilities.AddAdditionalCapability("noReset", false);
            capabilities.AddAdditionalCapability("bitbar_device", "Google Pixel 2");
            //Required
            capabilities.AddAdditionalCapability("bitbar_apiKey", "<YOUR_BITBAR_API_KEY>");
            capabilities.AddAdditionalCapability("bitbar_app", "<APP_ID>");
            //Biometrics capability
            capabilities.AddAdditionalCapability("biometricInstrumentation", true);

            Console.WriteLine("Sending request to start a session. Waiting for response typically takes 2-3 minutes...");
            driver = new AndroidDriver<AndroidElement>(new Uri(AppiumHubURL), capabilities, TimeSpan.FromSeconds(300));
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

        public void TakeScreenshot(String fileName)
        {
            try
            {
                Console.WriteLine("Taking screenshot: " + fileName + ".png");
                var filePath = SCREENSHOT_DIRECTORY + fileName + ".png";
                var screenshot = driver.GetScreenshot();
                screenshot.SaveAsFile(filePath, ScreenshotImageFormat.Png);
            }
            catch (Exception e)
            {
                Console.WriteLine("Failed to take a screenshot: " + e.Message);
            }
        }
    }
}