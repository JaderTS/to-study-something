package com.restassurance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class UserJsonTest {
	
	@Test
	public void verifyFirstLevel() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
		;
	}
	
	@Test
	public void verifyFirstLevelOtherMode() {
		Response response = RestAssured.request(Method.GET,"https://restapi.wcaquino.me/users/1");

		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	
	@Test
	public void checkSecondLevel() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
		;
	}
	
	@Test
	public void checkList() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
		;
	}

	@Test
	public void checkUserError(){
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
		.body("error", is("Usu�rio inexistente"))
		;
	}

	@Test
	public void checkUserList(){
		given()
		.when()
				.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("", hasSize(3))
			.body("name", hasItems("Jo�o da Silva","Maria Joaquina","Ana J�lia"))
			.body("age[1]", is(25))
			.body("filhos.name",hasItems(Arrays.asList("Zezinho","Luizinho")))
			.body("salary", contains(1234.5677F, 2500, null))
		;
	}

	@Test
	public void checkAdvanced(){
		given()
				.when()
				.get("https://restapi.wcaquino.me/users")
				.then()
				.statusCode(200)
				.body("$", hasSize(3))
				.body("age.findAll{it <= 25}.size()",is(2))
				.body("age.findAll{it <= 25 && it > 20}.size()",is(1))
				.body("findAll{it.age <= 25 && it.age > 20}.name",hasItem("Maria Joaquina"))
				.body("findAll{it.age <= 25}[0]",hasItem("Maria Joaquina"))
				.body("findAll{it.age <= 25}[-1]",hasItem("Ana Julia")) // -1 vai de tras em diante
				.body("find{it.age <= 25}",is("Maria Joaquina"))
				.body("findAll{it.name.contains('n')}.name",hasItems("Maria Joaquina", "Ana J�lia"))
				.body("findAll{it.name.length() > 10}.name",hasItems("Maria Joaquina", "Ana J�lia"))
				.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
				.body("name.FINDsLL{it.startsWith('Maria')}.collect{it.toUpperCase()}", allOf(hasItem("MARIA JOAQUINA")))
				.body("name.FINDsLL{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
				.body("age.collect{it * 2}",hasItems("60,50,40"))
				.body("id.max()",is(3))
				.body("salary.min()",is("1234,5678f"))
				.body("salary.sum()",is("1234,5678f"))
				.body("salary.findAll{it != null}.sum()",is(closeTo(3734.5678f,0.001)))
				.body("salary.findAll{it != null}.sum()",allOf(greaterThan(3000d), lessThan(5000d))) // boa pr�tica
		;
	}

	public void LinkJsonPathwithJava(){
		ArrayList<String> names =
		given()
				.when()
				.get("https://restapi.wcaquino.me/users")
				.then()
				.statusCode(200)
				.body("$", hasSize(3))
				.extract().path("nome.findAll{it.startsWith('Maria')}")
				;
		Assert.assertEquals(1,names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("mArIa")); // n�o � amig�vel
		Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
	}

}
