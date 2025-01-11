package UsersAPI;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
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
                //.then().log().ifError();
                //.then().log().ifValidationFails()
                .assertThat().body("[10].country", equalTo("Cairo"));
        //  .assertThat().body("name",hasItem("Hossam Hassan"));
        // .assertThat().body("name",hasItems("Carroll Rath DDS","Jasmine Kohler","Robin Hagenes"));
        //   .assertThat().body("name",not(hasItem("Ahmed Elnemr")));
        //      .assertThat().body("name",empty());
        //  .assertThat().body("country",hasItem(startsWith("L")));
        //  .assertThat().body("[0].country",startsWith("L"));

    }


    @Test
    public void Headers() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .header("Language", "English") // FILTER with Data
                .header("skills", "thinker")  // mumkn ab3t b kza header
                .when().get()
                .then().log().all();
    }

    @Test
    public void QueryParams() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .queryParam("name", "Ahmed")
                .when().get("Users")
                .then().log().all()
                .assertThat().body("name", hasItem("Ahmed"));
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
                .assertThat().body("name", hasItem("Erika Hirthe"));
    }

    @Test
    public void Authorization() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .header("Authoriztion", "the key") // de l authorization eli fl header
                .when().get("Users")
                .then().log().all()
                .assertThat().body("name", hasItem("Erika Hirthe"));
    }

    @Test
    public void AuthorizationMethod() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .auth().oauth2("ddddddddddddddd") // da l token autho w hya de l BAREAR TOKEN F PM
                .when().get("Users")
                .then().log().all()
                .assertThat().body("name", hasItem("Erika Hirthe"));
    }

    @Test
    public void postUser() {
        String requestBody = "{\"name\":\"Ahmed Elnemr\",\"country\":\"Cairo\",\"job\":\"Doctor\",\"id\":\"88\"}";

        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .contentType(ContentType.JSON).body(requestBody)
                .when().post("Users")
                .then().log().all()
                .assertThat().statusCode(201);

    }

    @Test
    public void updateUser() {
        String requestBody = "{\"name\":\"Ahmed Mohamed\",\"country\":\"Cairo\",\"job\":\"Doctor\",\"id\":\"13\"}";

        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .contentType(ContentType.JSON).body(requestBody)
                .when().put("Users/12")
                .then().log().all()
                .assertThat().statusCode(200);

    }

    @Test
    public void deleteUser() {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .when().delete("Users/21")
                .then().log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void extractData() {
        Response response = given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .when().get("Users")
                .then().extract().response();
        String job = response.jsonPath().getString("[1].name");
    }

    @Test(priority = 1)
    public void Create() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String country = faker.country().name();
        String job = faker.job().title();

        String requestBody = "{\"name\":\"" + name + "\",\"country\":\"" + country + "\",\"job\":\"" + job + "\"}";

        Response response = given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("Users")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().response();

        String userId = response.jsonPath().getString("id");

        response.then().assertThat().body("name", equalTo(name))
                .body("country", equalTo(country))
                .body("job", equalTo(job));

        // Use the dynamically generated user ID in subsequent tests
        UpdateUser(userId);
        DeleteUser(userId);
    }

    @Test(priority = 2, dependsOnMethods = "Create")
    public void UpdateUser(String userId) {
        Faker faker = new Faker();
        String newName = faker.name().firstName();

        String requestBody = "{\"name\":\"" + newName + "\"}";

        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().put("Users/{id}", userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("name", equalTo(newName));
    }

    @Test(priority = 3, dependsOnMethods = "Create")
    public void DeleteUser(String userId) {
        given().baseUri("https://64d75b732a017531bc132bba.mockapi.io/api/v1")
                .when().delete("Users/{id}", userId)
                .then().log().all()
                .assertThat().statusCode(200);
    }


    public String documentIdStirng;

    @Test
    public void CitizenRegisterAPI() {
        Faker fake = new Faker();
        String documentId = fake.number().digits(7);
        String photoFilePath = "C:\\Users\\aalnimr\\Pictures\\Screenshots\\desktop-wallpaper-thomas-eye-thommy-peakyblinders.jpg";

        // Read the photo file as a byte array
        byte[] photoBytes = null;
        try {
            photoBytes = FileUtils.readFileToByteArray(new File(photoFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("DocumentId", documentId)
                .multiPart("Photo", new File(photoFilePath))
                .multiPart("IsValidBiometric", "true")
                .when()
                .post("http://10.3.20.119:7017/citizen/register")
                .then().log().all()
                .assertThat().body("result", equalTo(true));
                 System.out.println(documentId);
                 documentIdStirng = documentId;
                System.out.println(documentIdStirng);
    }
    @Test
    public void eeeiii(){
        Faker fake = new Faker();
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"firstName\":\"ahmed\"," +
                        "\"lastName\":\"elnemr\"," +
                        "\"email\":\"adgdgfdfaa@live.com\"," +
                        "\"dateOfBirth\":\"1999-11-17\"," +
                        "\"mobileNumber\":\"3434343453454\"," +
                        "\"genderCode\":\"M\"," +
                        "\"nationalityCode\":\"DNK\"," +
                        "\"documentId\":\"" + documentIdStirng + "\"," +
                        "\"password\":\"Asd@1230\"," +
                        "\"confirmPassword\":\"Asd@1230\"," +
                        "\"address\":\"7326237632\"," +
                        "\"phoneNumber\":\"6758493827\"" +
                        "}")
                .when()
                .post("http://10.3.20.119:9010/Applicant/CreateApplicant")
                .then().log().all();
        System.out.println(documentIdStirng);

    }


}
