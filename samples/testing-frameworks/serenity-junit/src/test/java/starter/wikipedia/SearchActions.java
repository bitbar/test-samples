package starter.wikipedia;

import net.serenitybdd.core.steps.UIInteractions;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.Keys;

public class SearchActions extends UIInteractions {

    @Step("Search by keyword '{0}'")
    public void searchBy(String keyword) {
        $("#searchInput").sendKeys(keyword, Keys.ENTER);
    }
}
