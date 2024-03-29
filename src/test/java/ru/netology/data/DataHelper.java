package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static Faker fakerRu = new Faker(new Locale("ru"));
    private static Faker fakerEn = new Faker(new Locale("en"));

    private DataHelper(){}

    public static String getFirstCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getSecondCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getFirstCardStatus() {
        return "APPROVED";
    }

    public static String getSecondCardStatus() {
        return "DECLINED";
    }

    public static String getCardNumber1Digit() {
        return "8";
    }

    public static String getCardNumberChars() {
        return "Восемь";
    }

    public static String getWrongCardNumber() {
        return "8888 8888 8888 8888";
    }

    public static String getShortCardNumber() {
        return "8888 8888 8888 88";
    }

    public static String getCorrectMonth () {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
        return month;
    }

    public static String getNullMouth() {
        return "00";
    }


    public static String getMonthOver12() {return "13"; }



    public static String getMonthWithText() {
        return "октябрь";
    }

    public static String getValidYear() {
        String validYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
        return validYear;
    }


    public static String getPastYear() {
        String pastYear = LocalDate.now().minusYears(1).format(DateTimeFormatter
                .ofPattern("yy"));
        return pastYear;
    }


    public static String getFutureYear() {
        String futureYear = LocalDate.now().plusYears(10).format(DateTimeFormatter.ofPattern("yy"));
        return futureYear;
    }

    public static String getYearWithText() {
        return "двадцать третий";
    }

    public static String getValidOwner() {
        return fakerEn.name().firstName() + " " + fakerEn.name().lastName();
    }


    public static String getOnlyNameOwner() {
        return fakerEn.name().firstName();
    }

    public static String getLowercaseLettersOwner() {
        return fakerEn.name().firstName().toLowerCase(Locale.ROOT) + " " + fakerEn.name()
                .lastName().toLowerCase(Locale.ROOT);
    }

    public static String getUppercaseLettersOwner() {
        return fakerEn.name().firstName().toUpperCase(Locale.ROOT) + " " + fakerEn.name().lastName().toUpperCase(Locale.ROOT);
    }

    public static String getRedundantDataOwner() {
        return "Petrov Oleg Vladimirovich";
    }

    public static String getCyrillicDataOwner() {
        return fakerRu.name().fullName();
    }

    public static String getTwoAlphabetsDataOwner() {
        return fakerRu.name().firstName() + " " + fakerEn.name().lastName();
    }

    public static String getOwnerWithSpecialChars() {
        return "!№%?*";
    }

   public static String getValidCode() {
      return fakerEn.number().digits(3);
   }


    public static String getCodeWithText() {
        return "код";
    }
    public static String getRandomNumber(int length) {
        return fakerEn.number().digits(length);
    }
     public static String getEmptyString() {
        return "";}
    
}
