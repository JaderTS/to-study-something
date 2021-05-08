package com.restassurance;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class FileTest {

    @Test
    public void uploadFile(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/users.pdf"))
        .when()
                .post("http://restapi.wcaquino.me/upload")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", Matchers.is("users.pdf"))
        ;
    }

    @Test
    public void shouldNotDoUploadFile(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/big.pdf"))
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .statusCode(200)
                //.time(lessThan(5000L))
                .body("name", Matchers.is("big.pdf"))
        ;
    }

}
