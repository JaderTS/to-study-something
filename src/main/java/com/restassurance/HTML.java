package com.restassurance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.Matchers.is;

public class HTML {

    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "https://restapi.wcaquino.me";

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.log(LogDetail.ALL);
        reqSpec = requestSpecBuilder.build();
        responseSpecification = resSpec;
    }

    @Test
    public void searchingHTML(){
        given()
                .log().all()
        .when()
                .get("/v2/users")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body("html.body.div.table.tbody.tr.size()", is(3))
                .body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
                .appendRootPath("html.body.div.table.tbody")
                .body("tr.find{it.toString().startsWith('2')}.td[1]",is("Maria Joaquina"))
        ;
    }


}
