package UsersAPI;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class RA {


    @Test
    public void Base() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .when().get("Users")
                .then().log().all();  // de 3shan ygeb l Log bt3t l response
    }

    @Test
    public void Assertion() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .when().get("Users")
                .then().log().all()
                .assertThat().statusCode(200);

    }

    @Test
    public void Equalus() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .when().get("Users")
                .then().log().all()
                .assertThat().body("[15].country", equalTo("Egypt"));
        //  .assertThat().body("name",hasItem("Hossam Hassan"));
        // .assertThat().body("name",hasItems("Carroll Rath DDS","Jasmine Kohler","Robin Hagenes"));
        //   .assertThat().body("name",not(hasItem("Ahmed Elnemr")));
    }

    @Test
    public void Headers() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .header("Language", "English") // law hab3t request b header mo3yana
                .header("skills", "thinker")  // mumkn ab3t b kza header
                .when().get()
                .then().log().all();
    }

    @Test
    public void QueryParams() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .queryParam("name", "Jamal")
                .when().get("Users")
                .then().log().all()
                .assertThat().body("name", hasItem("Jamal"));
        // msh haynf3 m3aha equalto 3shan hya array f msh hatshof l esm 3'er [Jasmine Kohler]
    }

    @Test
    public void QueryParamsMethodHasMap() {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("country", "Oman");
        queries.put("Mode", "Paid");

        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .queryParam(String.valueOf(queries))
                .when().get("Users")
                .then().log().all()
                .assertThat().body("name", hasItem("Jasmine Kohler"));
    }

    @Test
    public void Authorization() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .header("Authoriztion", "the key") // de l authorization eli fl header
                .when().get("Users")
                .then().log().all()
                .assertThat().body("name", hasItem("Jasmine Kohler"));
    }

    @Test
    public void AuthorizationMethod() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .auth().oauth2("ddddddddddddddd") // da l token autho w hya de l BAREAR TOKEN F PM
                .when().get("Users")
                .then().log().all()
                .assertThat().body("name", hasItem("Jasmine Kohler"));
    }

    @Test
    public void postUser() {
        String requestBody = "{\"name\":\"Nabil Gamal\",\"country\":\"brazil\",\"job\":\"Doctor\",\"id\":\"88\"}";

        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .contentType(ContentType.JSON).body(requestBody)
                .when().post("Users")
                .then().log().all()
                .assertThat().statusCode(201);

    }

    @Test
    public void updateUser() {
        String requestBody = "{\"name\":\"Nadin Khaled\",\"country\":\"brazil\",\"job\":\"Doctor\",\"id\":\"21\"}";

        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .contentType(ContentType.JSON).body(requestBody)
                .when().put("Users/21")
                .then().log().all()
                .assertThat().statusCode(200);

    }

    @Test
    public void deleteUser() {
        given()
                .baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .when().delete("Users/21")
                .then().log().all()
                .assertThat().statusCode(204);
    }

}