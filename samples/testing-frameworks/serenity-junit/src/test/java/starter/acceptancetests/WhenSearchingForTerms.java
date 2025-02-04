package starter.acceptancetests;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.annotations.CastMember;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.questions.page.TheWebPage;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.support.ui.ExpectedConditions;
import starter.actions.navigation.NavigateTo;
import starter.actions.search.LookForInformation;

@ExtendWith(SerenityJUnit5Extension.class)
class WhenSearchingForTerms {

    @CastMember
    Actor actor;

    @Test
    @DisplayName("Should be able to search for red things")
    void searchForRedThings() {
        actor.attemptsTo(
                NavigateTo.theSearchHomePage(),
                LookForInformation.about("red"),
                WaitUntil.the(ExpectedConditions.titleContains("red")),
                Ensure.that(TheWebPage.title()).containsIgnoringCase("red")
        );
    }

    @Test
    @DisplayName("Should be able to search for green things")
    void searchForGreenThings() {
        actor.attemptsTo(
                NavigateTo.theSearchHomePage(),
                LookForInformation.about("green"),
                WaitUntil.the(ExpectedConditions.titleContains("green")),
                Ensure.that(TheWebPage.title()).containsIgnoringCase("green")
        );
    }
}
