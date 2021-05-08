package com.restassurance;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class SchemaTest {

    @Test
    public void validateSchemaXML(){
        given()
                .log().all()
        .when()
                .get("http://restapi.wcaquino.me/usersXML")
        .then()
                .log().all()
                .statusCode(200)
                .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
        ;
    }

    @Test
    public void invalidSchemaXML(){
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/invalidusersXML")
                .then()
                .log().all()
                .statusCode(200)
                .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
        ;
    }
}
