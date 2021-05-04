package com.restassurance;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;


import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWorldTest {
	
	@Test
	public void testHelloWorld() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("status code should be 200",response.statusCode() == 200);
		Assert.assertEquals(200,response.statusCode()); // modo correto
		
		//throw new RuntimeException();
		ValidatableResponse validado = response.then();
		validado.statusCode(200);
	}
	
	@Test
	public void outhersMode() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validado = response.then();
		validado.statusCode(200);
		
		get("http://restapi.wcaquino.me/ola").then().statusCode(200); // mais pratica
		
		given() // Pre condictions
		.when() // Action 
			.get("http://restapi.wcaquino.me/ola").
		then()	// Assertions
			//.assertThat() -- can be ignore
			.statusCode(200);
	}
	
	@Test
	public void workingMatcher() {
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128d, Matchers.isA(Double.class));
		
		List<Integer> odd = Arrays.asList(1,3,5);
		Assert.assertThat(odd, hasSize(3));
		Assert.assertThat(odd,containsInAnyOrder(1,3,5));
		Assert.assertThat(odd, hasItem(1));
		Assert.assertThat(odd, hasItems(1,5));
		
		assertThat("Maria", not("João"));
		assertThat("Maria", anyOf(is("Maria"),is("Pedro")));
		assertThat("João", allOf(startsWith("Joã"), endsWith("o")));
	}
	
	@Test
	public void validatedBody() {
		given() // Pre condictions
		.when() // Action 
			.get("http://restapi.wcaquino.me/ola").
		then()	// Assertions
			//.assertThat() -- can be ignore
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}
	
	@Test
    public void advancesearchWithXML() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/usersXML")
        .then()
            .statusCode(200)
                .body("users.user.size()", is(3))
                .body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))

                .rootPath("users.user")
                .body("@id", hasItems("1","2","3"))
                .body("find{it.age ==25}.name", is("Maria Joaquina"))
                .body("findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
                .body("salary.find{it != null}.toDouble()", is(1234.5678d))
                .body("age.collect{it.toInteger() * 2}", hasItems(40,50,60))
                .body("name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
        ;
    }

}
