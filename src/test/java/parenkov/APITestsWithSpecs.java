package parenkov;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static parenkov.Specs.baseRequest;
import static parenkov.Specs.createUserResponse;
import static parenkov.Specs.baseSuccessfulResponse;
import static parenkov.Specs.unsuccessfulRegistrationResponse;

public class APITestsWithSpecs {
    @Test
    void createUser() {
        given()
                .spec(baseRequest)
                .body("{ \"name\": \"Ivan\", \"job\": \"Developer\" }")
                .when()
                .post("/users")
                .then()
                .spec(createUserResponse)
                .body("name", is("Ivan"))
                .body("job", is("Developer"))
                .log().status()
                .log().body();
    }

    @Test
    void updateUser() {
        given()
                .spec(baseRequest)
                .body("{ \"name\": \"Alex\", \"job\": \"QA Engineer\" }")
                .when()
                .put("/users/{id}", 10)
                .then()
                .spec(baseSuccessfulResponse)
                .body("name", is("Alex"))
                .body("job", is("QA Engineer"))
                .log().status()
                .log().body();
    }

    @Test
    void successfulRegistration() {
        given()
                .spec(baseRequest)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"123\" }")
                .when()
                .post("/register")
                .then()
                .spec(baseSuccessfulResponse)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"))
                .log().status()
                .log().body();
    }

    @Test
    void unsuccessfulRegistration() {
        given()
                .spec(baseRequest)
                .body("{ \"email\": \"\", \"password\": \"123\" }")
                .when()
                .post("/register")
                .then()
                .spec(unsuccessfulRegistrationResponse)
                .log().status()
                .log().body();
    }
}
