namespace BitbarBiometricSample
{
    public class BaseBiometricTest
    {
        protected RemoteWebDriver driver;
        protected AppiumOptions capabilities;
        protected readonly String ScreenshotDirectory = AppDomain.CurrentDomain.BaseDirectory + "/screenshots/";
        protected readonly String AppiumHubURL = "https://appium.bitbar.com/wd/hub";

        public BaseBiometricTest() 
        {
            capabilities = new();
            //Required
            capabilities.AddAdditionalCapability("bitbar_apiKey", "<YOUR_BITBAR_API_KEY>");
            capabilities.AddAdditionalCapability("biometricInstrumentation", true);
        }

        public void TakeScreenshot(String fileName)
        {
            try
            {
                Console.WriteLine("Taking screenshot: " + fileName + ".png");
                var filePath = ScreenshotDirectory + fileName + ".png";
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
