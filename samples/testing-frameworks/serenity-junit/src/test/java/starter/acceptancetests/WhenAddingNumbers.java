package starter.acceptancetests;

import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(SerenityJUnit5Extension.class)
class WhenAddingNumbers {

    @Steps
    MathWizSteps michael;

    @Test
    public void addingSums() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(3);
    }
}
