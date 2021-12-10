package parenkov;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static parenkov.Specs.baseRequest;
import static parenkov.Specs.baseSuccessfulResponse;

public class TestsWithGroovy {
    @Test
    void createUser() {
        given()
                .spec(baseRequest)
                .when()
                .get("/users?page=1")
                .then()
                .spec(baseSuccessfulResponse)
                .log().status()
                .log().body()
                // выборка имен юзеров, начинающихся на букву "E" и проверка наличия конкретного имени
                .body("data.findAll{it.first_name =~/^E/}.first_name.flatten()",
                        hasItem("Emma"))
                // выборка всех фамилий юзеров и проверка наличия конкретной фамилии
                .body("data.findAll{it.last_name =~/./}.last_name.flatten()",
                        hasItem("Morris"));
    }
}
