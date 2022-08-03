package ru.netology.delivery.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.UserInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldGenerateTestDataUsingUtils() {
        UserInfo info = DataGenerator.Registration.generateUser("ru");
        String date = DataGenerator.generateDate(7);
        System.out.println(info.getCity() + "\n" + info.getName() + "\n" + info.getPhone() + "\n" + date);
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        SelenideElement form = $(".form");
        form.$("[data-test-id=city] .input__control").setValue(validUser.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] .input__control").setValue(firstMeetingDate);
        form.$("[data-test-id=name] .input__control").setValue(validUser.getName());
        form.$("[data-test-id=phone] .input__control").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$(".button").find(exactText("Запланировать")).click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .icon-button__text").click();
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] .input__control").setValue(secondMeetingDate);
        form.$$(".button").find(exactText("Запланировать")).click();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] .button__content").click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
    }
}
