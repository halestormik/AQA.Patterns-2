import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class APITest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTestFormWithActiveUser() { // тестирование процесса создания активного пользователя
        SelenideElement form = $("form");
        val user = DataGenerator.Registration.generateUser("us", "active");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("button").click();
        $("h2")
                .shouldHave(exactText("Личный кабинет"))
                .shouldBe((visible));
    }

    @Test
    void shouldTestFormWithBlockedUser() { // тестирование процесса создания заблокированного пользователя
        SelenideElement form = $("form");
        val user = DataGenerator.Registration.generateUser("us", "blocked");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Пользователь заблокирован"))
                .shouldBe((visible));
    }

    @Test
    void shouldTestFormWithIncorrectLogin() { // проверка валидации поля "логин"
        SelenideElement form = $("form");
        val user = DataGenerator.Registration.generateUser("us", "active");
        form.$("[data-test-id=login] input").setValue("");
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("button").click();
        $("[data-test-id=login].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"))
                .shouldBe((visible));
    }

    @Test
    void shouldTestFormWithIncorrectPassword() { // проверка валидации поля "пароль"
        SelenideElement form = $("form");
        val user = DataGenerator.Registration.generateUser("us", "active");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue("");
        form.$("button").click();
        $("[data-test-id=password].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"))
                .shouldBe((visible));
    }

    @Test
    void shouldTestFormWithUnregisteredUser_Login() { // тестирование формы при вводе логина незарегистрированного пользователя
        SelenideElement form = $("form");
        val user = DataGenerator.Registration.generateUser("us", "active");
        form.$("[data-test-id=login] input").setValue(user.getLogin() + "123");
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((visible));
    }

    @Test
    void shouldTestFormWithUnregisteredUser_Password() { // тестирование формы при вводе пароля незарегистрированного пользователя
        SelenideElement form = $("form");
        val user = DataGenerator.Registration.generateUser("us", "active");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword() + "123");
        form.$("button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((visible));
    }
}