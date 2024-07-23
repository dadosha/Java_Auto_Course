package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import data.RequestsHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Year;
import java.util.List;

import static data.DataGenerator.CardInfo.getApprovedCard;
import static data.DataGenerator.CardInfo.getDeclinedCard;
import static data.SQLHelper.*;
import static data.SQLHelper.getPaymentId;

public class ApiDebitTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("APPROVED pay")
    void shouldSuccessfulPay() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("APPROVED", status);
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("DECLINED pay")
    void shouldUnsuccessfulPay() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getDeclinedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("DECLINED", status);
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "1111 2222 3333 444",
        "1111 2222 3333 4444 5",
        "1234 5678 1234 5678"
    })
    @DisplayName("Send incorrect card")
    void shouldIncorrectCardDeclinedPay(String cardNumber) {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String field = RequestsHelper.sendnInorrectDebitCardPay(cardNumber,
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @ParameterizedTest
    @CsvSource({
        "01",
        "02",
        "11",
        "12"
    })
    @DisplayName("Send correct month")
    void shouldSuccessfulPay(String month) {
        var cardYear = DataGenerator.getYear() + 1;
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            month,
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("APPROVED", status);
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "00",
        "13",
        "99"
    })
    @DisplayName("Send incorrect month")
    void shouldIncorrectMothCardDeclinedPay(String month) {
        var cardYear = DataGenerator.getYear() + 1;
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            month,
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "6",
        "9",
        "100",
        "101",
        "999"
    })
    @DisplayName("Send incorrect month format")
    void shouldIncorrectMothFormatCardDeclinedPay(String month) {
        var cardYear = DataGenerator.getYear() + 1;
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            month,
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @Test
    @DisplayName("Send now month and year")
    void shouldSuccessfulPayWithNowDate() {
        var cardYear = DataGenerator.getNowYear();
        var cardMonth = DataGenerator.getNowMonth();
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("APPROVED", status);
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "asdad",
        "!@#$"
    })
    @DisplayName("Send incorrect month - text")
    void shouldIncorrectMothFormatText(String month) {
        var cardYear = DataGenerator.getYear() + 1;
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            month,
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @Test
    @DisplayName("Send next month and now year")
    void shouldSuccessfulPayWithNextMonth() {
        List<Integer> date = DataGenerator.checkNextMonth(DataGenerator.getNowMonth(), DataGenerator.getNowYear());
        var cardYear = date.get(1);
        var cardMonth = date.get(0);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("APPROVED", status);
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Send last month and now year")
    void shouldSuccessfulPayWithLastMonth() {
        List<Integer> date = DataGenerator.checkLastMonth(DataGenerator.getNowMonth(), DataGenerator.getNowYear());
        var cardYear = date.get(1);
        var cardMonth = date.get(0);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("DECLINED", status);
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPayment());
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

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("APPROVED", status);
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

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("DECLINED", status);
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Year - 00")
    void negativeMillenniumYear() {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            "00",
            ownerCard,
            cvvCard);

        Assertions.assertEquals("DECLINED", status);
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Negative limit values for years in future")
    void negativeLimitFutureValuesYear() {
        var cardYear = DataGenerator.getNowYear() + 6;
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("DECLINED", status);
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @Test
    @DisplayName("Negative limit values for years in future - 99")
    void negativeFutureValuesYear() {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            "99",
            ownerCard,
            cvvCard);

        Assertions.assertEquals("DECLINED", status);
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPayment());
        Assertions.assertEquals(getPaymentId(), SQLHelper.getPaymentTransaction());
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "5",
        "9",
        "100",
        "101",
        "999"
    })
    @DisplayName("Year not 2 symbol")
    void negativeYearOneSymbol(String year) {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            year,
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @ParameterizedTest
    @CsvSource({
        "ads",
        "!@#%"
    })
    @DisplayName("Year - text")
    void negativeYearText(String year) {
        var cardMonth = DataGenerator.getMonth(DataGenerator.getNowYear() + 1);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            year,
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @Test
    @DisplayName("One symbol owner")
    void ownerOneSymbol() {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();
        ownerCard = ownerCard.substring(ownerCard.length() - 1);
        var cvvCard = DataGenerator.generateCreditCardCVV();

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("APPROVED", status);
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

        String status = RequestsHelper.sendCorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard);

        Assertions.assertEquals("APPROVED", status);
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

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @ParameterizedTest
    @CsvSource({
        "12",
        "1234",
        "1"
    })
    @DisplayName("Incorrect CVV input")
    void cvvIncorrectInput(String cvvCard) {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }

    @ParameterizedTest
    @CsvSource({
        "dsfaasdf",
        "!@#$"
    })
    @DisplayName("Incorrect CVV input - text")
    void cvvIncorrectInputText(String cvvCard) {
        var cardYear = DataGenerator.getYear();
        var cardMonth = DataGenerator.getMonth(cardYear);
        var ownerCard = DataGenerator.generateCreditCardOwner();

        String field = RequestsHelper.sendnInorrectDebitCardPay(getApprovedCard().getNumber(),
            DataGenerator.getFormatMonth(cardMonth),
            DataGenerator.getFormatYear(cardYear),
            ownerCard,
            cvvCard,
            400,
            "error");

        Assertions.assertEquals("Bad Request", field);
    }
}
