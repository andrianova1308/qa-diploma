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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getEmptyCardNumber();
        var month = DataHelper.getNullMouth();
        var year = DataHelper.getEmptyYear();
        var cardOwner = DataHelper.getEmptyOwner();
        var code = DataHelper.getEmptyCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithEmptyCardNumberField() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getEmptyCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithDeclinedCard() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEmptyMouth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithMonthOver12() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getInvalidFormatMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInMonthField() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getEmptyYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithPastYear() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getInvalidFormatYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTooFutureYear() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getEmptyOwner();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithoutSecondName() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getOwnerWithDigits();
        var code = DataHelper.getValidCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }

    @Test
    void shouldDenyPurchaseWithSpecialCharsCardOwner() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
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
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getEmptyCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForMandatoryFieldMessage();
    }

    @Test
    void shouldDenyPurchaseWithWrongFormatCode() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getInvalidFormatCode();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForWrongFormatMessage();
    }

    @Test
    void shouldDenyPurchaseWithTextInCodeField() {
        paymentFormPageDebit = mainPage.payWithDebitCard()
                .clear();
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getCorrectMonth();
        var year = DataHelper.getValidYear();
        var cardOwner = DataHelper.getValidOwner();
        var code = DataHelper.getCodeWithText();
        paymentFormPageDebit.fillForm(cardNumber, month, year, cardOwner, code);
        paymentFormPageDebit.waitForInvalidCharactersMessage();
    }
}