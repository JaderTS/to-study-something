package com.restassurance;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AccessSWAPI {

    @Test
    public void acessingSWAPI(){
        given()
                .log().all()
        .when()
                .get("https://swapi.dev/api/people/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Luke Skywalker"))
        ;

    }
}
