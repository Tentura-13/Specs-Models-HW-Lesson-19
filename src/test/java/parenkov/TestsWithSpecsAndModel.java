package parenkov;

import org.junit.jupiter.api.Test;
import parenkov.models.User;
import parenkov.models.UserSupport;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static parenkov.Specs.baseRequest;
import static parenkov.Specs.createUserResponse;
import static parenkov.Specs.baseSuccessfulResponse;
import static parenkov.Specs.unsuccessfulRegistrationResponse;

public class TestsWithSpecsAndModel {
    @Test
    void createUser() {
        User data = given()
                .spec(baseRequest)
                .body("{ \"name\": \"Ivan\", \"job\": \"Developer\" }")
                .when()
                .post("/users")
                .then()
                .spec(createUserResponse)
                .log().status()
                .log().body()
                .extract().as(User.class);
        assertEquals("Ivan", data.getName());
        assertEquals("Developer", data.getJob());
    }

    @Test
    void updateUser() {
        User data = given()
                .spec(baseRequest)
                .body("{ \"name\": \"Alex\", \"job\": \"QA Engineer\" }")
                .when()
                .put("/users/{id}", 10)
                .then()
                .spec(baseSuccessfulResponse)
                .log().status()
                .log().body()
                .extract().as(User.class);
        assertEquals("Alex", data.getName());
        assertEquals("QA Engineer", data.getJob());
    }

    @Test
    void successfulRegistration() {
        User data = given()
                .spec(baseRequest)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"123\" }")
                .when()
                .post("/register")
                .then()
                .spec(baseSuccessfulResponse)
                .log().status()
                .log().body()
                .extract().as(User.class);
        assertEquals(4, data.getId());
        assertEquals("QpwL5tke4Pnpja7X4", data.getToken());
    }

    @Test
    void unsuccessfulRegistration() {
        User data = given()
                .spec(baseRequest)
                .body("{ \"email\": \"\", \"password\": \"123\" }")
                .when()
                .post("/register")
                .then()
                .spec(unsuccessfulRegistrationResponse)
                .log().status()
                .log().body()
                .extract().as(User.class);
        assertEquals("Missing email or username", data.getError());
    }

    @Test
    void singleUser() {
        UserSupport support = given()
                .spec(baseRequest)
                .when()
                .get("/users/2")
                .then()
                .spec(baseSuccessfulResponse)
                .log().status()
                .log().body()
                .extract().as(UserSupport.class);
        assertEquals("To keep ReqRes free, contributions towards " +
                "server costs are appreciated!", support.getSupport().getText());
    }
}
