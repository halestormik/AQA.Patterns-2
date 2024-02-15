import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    public static String generateUsername(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().username();
    }

    public static String generatePassword(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationForm generateUser(String locale, String status) {
            RegistrationForm user = new RegistrationForm(generateUsername(locale), generatePassword(locale), status);
            sendRequest(user);
            return user;
        }

        private static void sendRequest(RegistrationForm user) {
            given()
                    .spec(requestSpec)
                    .body(user)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }
    }

    @Value
    public static class RegistrationForm {
        String login;
        String password;
        String status;
    }
}
