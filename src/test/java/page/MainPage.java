package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private SelenideElement cardButton = $("body > div > div > button:nth-of-type(1)");
    private SelenideElement creditCardButton = $("body > div > div > button:nth-of-type(2)");
    private SelenideElement headingCard = $("h3[class='heading heading_size_m heading_theme_alfa-on-white']");
    private SelenideElement cardNumberField = $("input[maxlength='19']");
    private SelenideElement cardMonthField = $("input[placeholder='08']");
    private SelenideElement cardYearField = $("input[placeholder='22']");
    private SelenideElement cardOwnerField = $("fieldset > div:nth-of-type(3) > span > span:nth-child(1)");
    private SelenideElement cardCVVField = $("input[placeholder='999']");
    private SelenideElement acceptButton = $("fieldset > div:nth-of-type(4) > button");

    public void openDebitCardForm() {
        cardButton.click();
        headingCard.shouldBe(Condition.visible).shouldHave(Condition.text("Оплата по карте"));
    }

    public void openCreditCardForm() {
        cardButton.click();
        headingCard.shouldBe(Condition.visible).shouldHave(Condition.text("Кредит по данным карты"));
    }


}
