package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));
    public static String generateCreditCardNumber() {
        return faker.business().creditCardNumber();
    }

    @Value
    public static class Card {
        String number;
    }

    public static class CardInfo {
        private CardInfo() {
        }

        public static Card getApprovedCard() {
            return new Card("1111 2222 3333 4444");
        }

        public static Card getDeclinedCard() {
            return new Card("5555 6666 7777 8888");
        }

        public static Card getIncorrectCard() {
            return new Card(generateCreditCardNumber());
        }
    }
}
