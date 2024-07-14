package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Date date = new Date();
    private static final Random rnd = new Random();
    public static String generateCreditCardNumber() {
        return faker.business().creditCardNumber();
    }
    public static String generateCreditCardCVV() {
        return faker.numerify("###");
    }

    public static String generateCreditCardOwner() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static int getNowMonth() {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        return month;
    }

    public static int getMonth(int year) {
        int nowMoth = getNowMonth();
        if (year == getNowYear()) {
            return rnd.nextInt(12 - nowMoth + 1) + nowMoth;
        }
        else {
            return rnd.nextInt(12) + 1;
        }
    }

    public static String getFormatMonth(int month) {
        if (month < 10) {
            return "0" + month;
        }
        else
        {
            return Integer.toString(month);
        }
    }

    public static int getNowYear() {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        return year;
    }

    public static int getYear() {
        int nowYear = getNowYear();
        return rnd.nextInt(6) + nowYear;
    }

    public static String getFormatYear(int year) {
        return Integer.toString(year % 100);
    }

    @Value
    public static class Card {
        String number;
    }

    public static class CardInfo {
        private CardInfo() {
        }

        public static Card getApprovedCard() {return new Card("1111 2222 3333 4444");}

        public static Card getDeclinedCard() {
            return new Card("5555 6666 7777 8888");
        }

        public static Card getIncorrectCard() {
            return new Card(generateCreditCardNumber());
        }
    }
}
