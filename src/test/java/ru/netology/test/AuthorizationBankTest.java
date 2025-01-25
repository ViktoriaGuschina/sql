package ru.netology.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static java.nio.channels.FileChannel.open;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class AuthorizationBankTest {
     @BeforeAll
     static void beTeardown() {
         cleanDatabase();
     }
    @AfterAll
    static void teardown() {
        cleanDatabase();
    }

    @Test
    void shouldSuccessfulLogin() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotificationVisiblity();
    }

    @Test
    void errorNotificationIfLoginWithExistUserAndRandomVerificationCode() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotificationVisiblity();

    }
}
