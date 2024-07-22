package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import page.MainPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.CardInfo.*;
import static data.SQLHelper.getPaymentId;

public class DebitCardTest {
    MainPage mainPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        mainPage = open("http://localhost:8080", MainPage.class);
        mainPage.openDebitCardForm();
    }

    @Test
    @DisplayName("Successful pay")
    void shouldSuccessfulPay() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendCorrectForm();

        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Decline pay")
    void shouldDeclinedPay() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getDeclinedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectForm();

        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Not real card")
    void notRealCard() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getIncorrectCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectForm();
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "18",
        "19"
    })
    @DisplayName("Card less 16 number")
    void cardLessLength(int length) {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getIncorrectCard().getNumber().substring(length));
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectCard("Неверный формат");
    }

    @Test
    @DisplayName("Send form with empty card")
    void emptyCard() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectCard("Поле обязательно для заполнения");
    }

    @ParameterizedTest
    @CsvSource({
        "01",
        "02",
        "11",
        "12"
    })
    @DisplayName("Positive limit values for months")
    void successesLimitValuesMonth(String cardMonth) {
        var cardYear = DataGenerator.getNowYear() + 1;
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(cardMonth);
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendCorrectForm();

        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "00",
        "13",
        "99"
    })
    @DisplayName("Negative limit values for months")
    void unsuccessfulLimitValuesMonth(String cardMonth) {
        var cardYear = DataGenerator.getNowYear() + 1;
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(cardMonth);
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectMonth("Неверно указан срок действия карты");
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "6",
        "9"
    })
    @DisplayName("Incorrect format for months")
    void incorrectFormatValuesMonth(String cardMonth) {
        var cardYear = DataGenerator.getNowYear() + 1;
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(cardMonth);
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectMonth("Неверный формат");
    }

    @Test
    @DisplayName("Send form with empty month")
    void emptyMonth() {
        var cardYear = DataGenerator.getYear();
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectMonth("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Now months and year")
    void sendFormWithNowMonthYear() {
        var cardYear = DataGenerator.getNowYear();
        var cardMonth = DataGenerator.getNowMonth();
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendCorrectForm();

        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Last month")
    void sendFormWithLastMonthYear() {
        List<Integer> date = DataGenerator.checkLastMonth(DataGenerator.getNowMonth(), DataGenerator.getNowYear());
        var cardYear = date.get(1);
        var cardMonth = date.get(0);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectMonth("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Next month")
    void sendFormWithNextMonthYear() {
        List<Integer> date = DataGenerator.checkNextMonth(DataGenerator.getNowMonth(), DataGenerator.getNowYear());
        var cardYear = date.get(1);
        var cardMonth = date.get(0);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendCorrectForm();

        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "4",
        "5"
    })
    @DisplayName("Positive limit values for years")
    void positiveLimitValuesYear(int addYears) {
        var cardYear = DataGenerator.getNowYear() + addYears;
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendCorrectForm();

        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Last Year")
    void negativeLastYear() {
        var cardYear = DataGenerator.getNowYear() - 1;
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectYear("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Year - 00")
    void negativeMillenniumYear() {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard("00");
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectYear("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Negative limit values for years in future")
    void negativeLimitFutureValuesYear() {
        var cardYear = DataGenerator.getNowYear() + 6;
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectYear("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Negative limit values for years in future - 99")
    void negativeFutureValuesYear() {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard("99");
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectYear("Неверно указан срок действия карты");
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "5",
        "9"
    })
    @DisplayName("Year 1 symbol")
    void negativeYearOneSymbol(String year) {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(year);
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectYear("Неверный формат");
    }

    @Test
    @DisplayName("Year empty")
    void negativeEmptyYear() {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectYear("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Owner empty")
    void negativeEmptyOwner() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectOwner("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("One symbol owner")
    void ownerOneSymbol() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        ownerCard = ownerCard.substring(ownerCard.length() - 1);
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendCorrectForm();

        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "William",
        "John Smith",
        "John-Smith"
    })
    @DisplayName("Correct owner input")
    void ownerCorrectInput(String ownerCard) {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendCorrectForm();

        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "Стас",
        "!@#$%",
        "-",
        "1234"
    })
    @DisplayName("Incorrect owner input")
    void ownerIncorrectInput(String ownerCard) {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectOwner("Неверный формат");
    }

    @ParameterizedTest
    @CsvSource({
        "12",
        "1"
    })
    @DisplayName("Incorrect CVV input")
    void cvvIncorrectInput(String cvvCard) {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectCVV("Неверный формат");
    }

    @Test
    @DisplayName("CVV empty")
    void negativeEmptyCVV() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.sendIncorrectCVV("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Hidden card number field error")
    void hiddenFieldErrorCardNumber() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard("1111");
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectCard("Неверный формат");
        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.sendHiddenCardFieldError();
    }

    @Test
    @DisplayName("Hidden month field error")
    void hiddenFieldErrorMonth() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard("13");
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectMonth("Неверно указан срок действия карты");
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.sendHiddenMonthError();
    }

    @Test
    @DisplayName("Hidden year field error")
    void hiddenFieldErrorYear() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard("1");
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectYear("Неверный формат");
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.sendHiddenYearError();
    }

    @Test
    @DisplayName("Hidden owner field error")
    void hiddenFieldErrorOwner() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard("");
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendIncorrectOwner("Поле обязательно для заполнения");
        mainPage.fillOwnerCard(ownerCard);
        mainPage.sendHiddenOwnerError();
    }

    @Test
    @DisplayName("Hidden CVV field error")
    void hiddenFieldErrorCVV() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        mainPage.fillNumberCard(getApprovedCard().getNumber());
        mainPage.fillMonthCard(DataGenerator.getFormatMonth(cardMonth));
        mainPage.fillYearCard(DataGenerator.getFormatYear(cardYear));
        mainPage.fillOwnerCard(ownerCard);
        mainPage.fillCVVCard("1");
        mainPage.sendIncorrectCVV("Неверный формат");
        mainPage.fillCVVCard(cvvCard);
        mainPage.sendHiddenCVVError();
    }
}
