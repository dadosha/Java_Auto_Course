package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import data.RequestsHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static data.DataGenerator.CardInfo.getApprovedCard;
import static data.SQLHelper.*;

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
    @DisplayName("Successful pay")
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

        assert "APPROVED".equals(status);
        assert "APPROVED".equals(SQLHelper.getStatusPayment());
        assert getPaymentId().equals(SQLHelper.getPaymentTransaction());
    }
}
