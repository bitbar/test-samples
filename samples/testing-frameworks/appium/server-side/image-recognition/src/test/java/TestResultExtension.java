import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import static java.lang.System.getProperty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Michał Szpruta <michal.szpruta@smartbear.com>
 */
public class TestResultExtension implements AfterTestExecutionCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestResultExtension.class);

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            String className = context.getTestClass().map(Class::getSimpleName).orElse(EMPTY);
            String method = context.getTestMethod().map(Method::getName).orElse(EMPTY);
            String details = context.getDisplayName();
            if (details.equals(String.format("%s()", method))) {
                LOGGER.error("Failed test: {}#{}", className, method);
            } else {
                LOGGER.error("Failed test: {}#{}, details: {}", className, method, details);
            }

            String fullFileName = String.format("%s/%s%s_failure.png",
                    getProperty("user.dir"), getProperty("SCREENSHOT_FOLDER"), "_failure.png");
            try {
                AppiumDriver driver = AbstractAppiumTest.driver;
                File scrFile = driver.getScreenshotAs(OutputType.FILE);
                File testScreenshot = new File(fullFileName);
                FileUtils.copyFile(scrFile, testScreenshot);
                LOGGER.info("Screenshot stored to {}", testScreenshot.getAbsolutePath());
                LOGGER.info("PAGE SOURCE:");
                LOGGER.info(driver.getPageSource());
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
