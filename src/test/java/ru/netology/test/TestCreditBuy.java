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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
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
        paymentFormPageCredit = mainPage.payWithCreditCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getCodeWithText();
        paymentFormPageCredit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageCredit.waitForInvalidCharactersMessage();
    }
}
