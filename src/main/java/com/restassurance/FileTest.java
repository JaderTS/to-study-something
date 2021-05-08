package com.restassurance;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

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

    @Test
    public void downloadFile() throws IOException {
        byte[] image = given()
                .log().all()
                .when()
                    .get("http://restapi.wcaquino.me/download")
                .then()
                    .log().all()
                    .statusCode(200)
        .extract().asByteArray()
        ;

        File imagem = new File("src/main/resources/imagem.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        System.out.println(imagem.length());
        Assert.assertThat(imagem.length(), lessThan(100000L));
    }

}
