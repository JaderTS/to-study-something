package com.restassurance;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers.*;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class SchemaTest {

    @Test
    public void validateSchemaXML() {
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

    @Test(expected = SAXParseException.class)
    public void invalidSchemaXML() {
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

    @Test
    public void validateSchemaJson() {
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
        ;
    }

    @Test
    public void getWeather() {
        given()
                .log().all()
                .queryParam("q", "porto alegre,BR")
                .queryParam("appid", "38d980f2d054619562724cdd8f0dd773")
                .queryParam("units", "metric")
                .when()
                .get("http://api.openweathermap.org/data/2.5/weather")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Porto Alegre"))
                .body("main.temp", greaterThan(3f))
        ;
    }

    @Test
    public void noAccessWitoutPassword() {
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(401)
        ;
    }

    @Test
    public void basicAccess() {
        given()
                .log().all()
                .when()
                .get("http://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void basicAccess2() {
        given()
                .log().all()
                .auth().basic("admin","senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void basicChallengeAccess() {
        given()
                .log().all()
                .auth().preemptive().basic("admin","senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }
}
