package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentFormPageCredit;
import ru.netology.sql.SqlRequest;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCreditBuy {
    private MainPage mainPage;
    private PaymentFormPageCredit paymentFormPageCredit;

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
    //    paymentFormPageCredit = mainPage.payWithCreditCard()
    //            .clear();
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForSuccessedNotification();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SqlRequest.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDenyPurchaseWithEmptyFields() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getEmptyCardNumber();
        var month = DataHelper.getEmptyString();
        var year = DataHelper.getEmptyString();
        var cardOwner = DataHelper.getEmptyString();
        var code = DataHelper.getEmptyString();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyFieldCardNumber() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getEmptyCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithDeclinedCard() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getSecondCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForFailedNotification();
        var expected = DataHelper.getSecondCardStatus();
        var actual = SqlRequest.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDenyPurchaseWithAnotherCard() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getWrongCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForFailedNotification();
    }

    @Test
    void shouldDenyPurchaseCardNumberWith15Digits() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getShortCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseCardNumberWith1Digit() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getCardNumber1Digit();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseCardNumberWithTextAndChars() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getCardNumberChars();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyFieldMonth() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEmptyString();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithMonthOver12() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthOver12();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongCardExpirationMessage();
    }

    @Test
    void shouldDenyPurchaseWithZeroMonth() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getNullMouth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongCardExpirationMessage();
    }

    @Test
    void shouldDenyPurchaseWithWrongFormatMonth() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getRandomNumber(3);
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInMonthField() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthWithText();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyYearField() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getEmptyString();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithPastYear() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getPastYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForCardExpiredMessage();
    }

    @Test
    void shouldDenyPurchaseWithWrongFormatYear() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getRandomNumber(3);
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTooFutureYear() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getFutureYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongCardExpirationMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInYearField() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getYearWithText();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyCardOwnerField() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getEmptyString();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithoutSecondName() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getOnlyNameOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithLowercaseCardOwner() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getLowercaseLettersOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithUppercaseCardOwner() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getUppercaseLettersOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithRedundantDataCardOwner() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getRedundantDataOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithCyrillicDataCardOwner() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getCyrillicDataOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTwoLanguagesCardOwner() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getTwoAlphabetsDataOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithDigitsCardOwner() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getRandomNumber(5);
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithSpecialCharsCardOwner() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getOwnerWithSpecialChars();
        var code = DataHelper.getValidCode();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyCodeField() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getEmptyString();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithWrongFormatCode() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getRandomNumber(2);
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInCodeField() {
        paymentFormPageCredit.clearFields();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getCodeWithText();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }
}
