package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement cardButton = $("button[class='button button_size_m button_theme_alfa-on-white']");
    private final SelenideElement creditCardButton = $("body button:nth-of-type(2)");
    private final SelenideElement headingCard = $("h3[class='heading heading_size_m heading_theme_alfa-on-white']");
    private final SelenideElement cardNumberField = $("input[maxlength='19']");
    private final SelenideElement cardMonthField = $("input[placeholder='08']");
    private final SelenideElement cardYearField = $("input[placeholder='22']");
    private final SelenideElement cardOwnerField = $("fieldset div:nth-of-type(3) > span > span:nth-child(1) input");
    private final SelenideElement cardCVVField = $("input[placeholder='999']");
    private final SelenideElement acceptButton = $("fieldset div:nth-of-type(4) button");
    private final SelenideElement successNotificationTitleBuy = $("div[class*='ok'] .notification__title");
    private final SelenideElement successNotificationTextBuy = $("div[class*='ok'] .notification__content");
    private final SelenideElement errorNotificationTitleBuy = $("div[class*='error'] .notification__title");
    private final SelenideElement errorNotificationTextBuy = $("div[class*='error'] .notification__content");
    private final SelenideElement errorNumberCardField = $("fieldset div:nth-of-type(1) span:nth-of-type(3)");
    private final SelenideElement errorMonthField = $("div:nth-of-type(2) > span > span:nth-of-type(1) span:nth-of-type(3)");
    private final SelenideElement errorYearField = $("div:nth-of-type(2) span:nth-of-type(2) span:nth-of-type(3)");
    private final SelenideElement errorOwnerField = $("div:nth-of-type(3) > span > span:nth-of-type(1) span:nth-of-type(3)");
    private final SelenideElement errorCVVField = $("div:nth-of-type(3) span:nth-of-type(2) span:nth-of-type(3)");

    public void openDebitCardForm() {
        cardButton.click();
        headingCard.shouldBe(Condition.visible).shouldHave(Condition.text("Оплата по карте"));
    }

    public void openCreditCardForm() {
        creditCardButton.click();
        headingCard.shouldBe(Condition.visible).shouldHave(Condition.text("Кредит по данным карты"));
    }

    public void fillNumberCard(String numberCard) {
        cardNumberField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        cardNumberField.setValue(numberCard);
    }

    public void fillMonthCard(String month) {
        cardMonthField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        cardMonthField.setValue(month);
    }

    public void fillYearCard(String year) {
        cardYearField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        cardYearField.setValue(year);
    }

    public void fillOwnerCard(String owner) {
        cardOwnerField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        cardOwnerField.setValue(owner);
    }

    public void fillCVVCard(String cvv) {
        cardCVVField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        cardCVVField.setValue(cvv);
    }

    public void sendCorrectForm() {
        acceptButton.click();

        successNotificationTitleBuy.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text("Успешно"));
        successNotificationTextBuy.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text("Операция одобрена Банком."));
    }

    public void sendIncorrectForm() {
        acceptButton.click();

        errorNotificationTitleBuy.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text("Ошибка"));
        errorNotificationTextBuy.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."));
    }

    public void sendIncorrectCard(String errorText) {
        acceptButton.click();

        errorNumberCardField.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text(errorText));
    }

    public void sendIncorrectMonth(String errorText) {
        acceptButton.click();

        errorMonthField.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text(errorText));
    }

    public void sendIncorrectYear(String errorText) {
        acceptButton.click();

        errorYearField.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text(errorText));
    }

    public void sendIncorrectOwner(String errorText) {
        acceptButton.click();

        errorOwnerField.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text(errorText));
    }

    public void sendIncorrectCVV(String errorText) {
        acceptButton.click();

        errorCVVField.shouldBe(Condition.visible, Duration.ofSeconds(15))
            .shouldHave(Condition.text(errorText));
    }

    public void sendHiddenCardFieldError() {
        acceptButton.click();

        errorNumberCardField.shouldNot(Condition.visible, Duration.ofSeconds(15));
    }

    public void sendHiddenMonthError() {
        acceptButton.click();

        errorMonthField.shouldNot(Condition.visible, Duration.ofSeconds(15));
    }

    public void sendHiddenYearError() {
        acceptButton.click();

        errorYearField.shouldNot(Condition.visible, Duration.ofSeconds(15));
    }

    public void sendHiddenOwnerError() {
        acceptButton.click();

        errorOwnerField.shouldNot(Condition.visible, Duration.ofSeconds(15));
    }

    public void sendHiddenCVVError() {
        acceptButton.click();

        errorCVVField.shouldNot(Condition.visible, Duration.ofSeconds(15));
    }
}
