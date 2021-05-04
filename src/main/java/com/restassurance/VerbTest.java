package com.restassurance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class VerbTest {
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
    public void shouldBeSave(){
        given()
                .contentType("application/json")
                .body("{\"name\": \"Jose\", \"age\": 50}")
        .when()
                .post("/users")
        .then()
//                .spec(resSpec)
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Jose"))
                .body("age", is(50))
        ;
    }

    @Test
    public void SaveUserUsingMap(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name","Frederico");
        params.put("age",18);

        given()
                .contentType("application/json")
                .pathParam("entidade", "users")
                .body(params)
        .when()
                .post("/{entidade}")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Frederico"))
                .body("age", is(18))
        ;
    }

    @Test
    public void SaveUserUsingobject(){
        User user = new User("Usuario via objeto",35);

        given()
                .contentType("application/json")
                .pathParam("entidade","users")
                .body(user)
        .when()
                .post("{entidade}")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Usuario via objeto"))
            .body("age", is(35))
        ;
    }

    @Test
    public void shouldNotHaveUserWithoutName(){
        given()
                .contentType("application/json")
                .body("{\"age\":50}")
        .when()
                .post("/users")
        .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void shouldSaveUserByXML(){
        given()
                .log().all()
                .contentType("application/xml")
                .body("<user><name>Jose</name><age>50</age><salary>5678</salary></user>")
        .when()
                .post("/usersXML")
        .then()
                .log().all()
                .statusCode(201)
                .body("@id",is(notNullValue()))
                .body("user.name", is("Jose"))
                .body("user.age",is("50"))
        ;
    }

    @Test
    public void shouldBeUser(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\":\"Usuário alterado\",\"age\":80}")
        .when()
                .put("/users/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id",is(1))
                .body("name",is("Usuário alterado"))
                .body("age", is(80))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void customizeURLFirstPart(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\":\"Usuário alterado\",\"age\":80}")
        .when()
                .put("/{entidade}/{userId}", "users", "1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id",is(1))
                .body("name",is("Usuário alterado"))
                .body("age", is(80))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void customizeURLSecondPart(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\":\"Usuário alterado\",\"age\":90}")
                .pathParam("entidade", "users")
                .pathParam("userId",1)
        .when()
                .put("/{entidade}/{userId}")
        .then()
                .log().all()
                .statusCode(200)
                .body("id",is(1))
                .body("name",is("Usuário alterado"))
                .body("age", is(90))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void removeUser(){
        given()
                .log().all()
                .pathParam("entidade", "users")
                .pathParam("userId",1)
        .when()
                .delete("/{entidade}/{userId}")
        .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test
    public void removeFollUser(){
        given()
                .log().all()
                .contentType("application/json")
                .pathParam("entidade","users")
        .when()
                .delete("/{entidade}/1000")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", is("Registro inexistente"))
        ;
    }
}