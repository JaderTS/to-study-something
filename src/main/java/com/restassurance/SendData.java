package com.restassurance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.Matchers.containsString;

public class SendData {

    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "https://restapi.wcaquino.me";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        reqSpec = reqBuilder.build();
        responseSpecification = resSpec;
    }

    @Test
    public void sendingQuery(){

        given()
        .when()
                .get("/v2/users?format=xml")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
        ;
    }

    @Test
    public void sendingQueryForParameters(){

        given()
                .log().all()
                .queryParam("format","xml")
                .queryParam("outra","coisa")
        .when()
                .get("/v2/users")
        .then()
                .statusCode(200)
                .contentType(ContentType.XML)
                .contentType(containsString("utf-8"))
        ;
    }

    @Test
    public void seandingQueryForHeader(){

        given()
                .log().all()
                .accept(ContentType.HTML)
        .when()
                .get("/v2/users")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
        ;
    }
}
