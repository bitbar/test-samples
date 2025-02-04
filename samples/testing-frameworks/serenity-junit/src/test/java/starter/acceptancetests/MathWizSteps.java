package starter.acceptancetests;

import net.serenitybdd.annotations.Step;
import starter.Calculator;

import static org.assertj.core.api.Assertions.assertThat;

public class MathWizSteps {

    String actor;

    Calculator calculator;

    @Step("#actor starts with {0}")
    public void startsWith(int amount) {
        calculator = new Calculator(amount);
    }

    @Step("#actor adds {0}")
    public void adds(int amount) {
        calculator.add(amount);
    }

    @Step("#actor should have {0}")
    public void shouldHave(int expectedTotal) {
        assertThat(calculator.getTotal()).isEqualTo(expectedTotal);
    }
}
