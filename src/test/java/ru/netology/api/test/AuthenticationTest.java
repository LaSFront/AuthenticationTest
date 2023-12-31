package ru.netology.api.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.api.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.api.data.DataGenerator.Registration.getUser;
import static ru.netology.api.data.DataGenerator.getRandomLogin;
import static ru.netology.api.data.DataGenerator.getRandomPassword;

public class AuthenticationTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfullyLoginWithActiveRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").sendKeys(registeredUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(registeredUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(Condition.exactText("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorMessageIfLoginWithNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(Condition.exactText("Продолжить")).click();
        $(".notification__title").shouldHave(Condition.exactText("Ошибка"));
        $(".notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorMessageIfLoginWithBlockedRegisteredUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").sendKeys(blockedUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(blockedUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(Condition.exactText("Продолжить")).click();
        $(".notification__title").shouldHave(Condition.exactText("Ошибка"));
        $(".notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorMessageIfLoginWithWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").sendKeys(wrongLogin);
        $("[data-test-id='password'] input").sendKeys(registeredUser.getPassword());
        $("[data-test-id='action-login']").shouldHave(Condition.exactText("Продолжить")).click();
        $(".notification__title").shouldHave(Condition.exactText("Ошибка"));
        $(".notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorMessageIfLoginWithWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").sendKeys(registeredUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(wrongPassword);
        $("[data-test-id='action-login']").shouldHave(Condition.exactText("Продолжить")).click();
        $(".notification__title").shouldHave(Condition.exactText("Ошибка"));
        $(".notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}

