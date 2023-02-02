package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentFormPageDebit;
import ru.netology.sql.SqlRequest;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDebitBuy {
    private MainPage mainPage;
    private PaymentFormPageDebit paymentFormPageDebit;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        String url = System.getProperty("sut.url");
        mainPage = open(url, MainPage.class);
    }

    @AfterEach
    void cleanDB() {
        SqlRequest.clearDB();
    }


    @Test
    void shouldAllowPurchaseWithApprovedCard() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForSuccessedNotification();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SqlRequest.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDenyPurchaseWithEmptyFields() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getEmptyString();
        var month = DataHelper.getEmptyString();
        var year = DataHelper.getEmptyString();
        var cardOwner = DataHelper.getEmptyString();
        var code = DataHelper.getEmptyString();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyCardNumberField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getEmptyString();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithDeclinedCard() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getSecondCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForFailedNotification();
        var expected = DataHelper.getSecondCardStatus();
        var actual = SqlRequest.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDenyPurchaseWithAnotherCard() {
       paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getWrongCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForFailedNotification();
    }

    @Test
    void shouldDenyPurchaseCardNumberWith14Digits() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getShortCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseCardNumberWith1Digit() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getCardNumber1Digit();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseCardNumberWithChars() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getCardNumberChars();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyMonthField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEmptyString();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithMonthOver12() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthOver12();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongCardExpirationMessage();
    }

    @Test
    void shouldDenyPurchaseWithNullMonth() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getNullMouth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongCardExpirationMessage();
    }

    @Test
    void shouldDenyPurchaseWithWrongFormatMonth() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getRandomNumber(3);
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInMonthField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthWithText();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyYearField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getEmptyString();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithPastYear() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getPastYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForCardExpiredMessage();
    }

    @Test
    void shouldDenyPurchaseWithWrongFormatYear() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getRandomNumber(3);
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTooFutureYear() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getFutureYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongCardExpirationMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInYearField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getYearWithText();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyCardOwnerField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getEmptyString();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithoutSecondName() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getOnlyNameOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithLowercaseCardOwner() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getLowercaseLettersOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithUppercaseCardOwner() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getUppercaseLettersOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithRedundantDataCardOwner() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getRedundantDataOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithCyrillicDataCardOwner() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getCyrillicDataOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTwoLanguagesCardOwner() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getTwoAlphabetsDataOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithDigitsCardOwner() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getRandomNumber(5);
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithSpecialCharsCardOwner() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getOwnerWithSpecialChars();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyCodeField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getEmptyString();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithWrongFormatCode() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getRandomNumber(2);
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInCodeField() {
        paymentFormPageDebit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getCodeWithText();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }
}
