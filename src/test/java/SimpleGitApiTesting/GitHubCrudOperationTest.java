package SimpleGitApiTesting;

import BaseUtility.BaseTest;
import RequestSpecificationUtility.RequestSpecUtility;
import Utility.JsonFileReader;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
//PERFORMED CRUD OPERATION OF REST API USING EXTERNAL JSON FILE
public class GitHubCrudOperationTest extends BaseTest {
    Response response;

    @Test(priority = 1)
    public void createRepositoryTest(){
        //Fetch repository data from jsonFileReader utility
        JSONObject jsonObject= JsonFileReader.readJsonFile(properties.getProperty("gitJsonFilePath"));

        //PRECONDITION
       response =given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .body(jsonObject)

                //HTTP METHOD WILL RETURN RESPONSE
                .when()
                .post("/user/repos");

        //Few assertion for validation
        response.then().assertThat().statusCode(201);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("Created"));
        response.then().assertThat().body("name",equalTo(JsonFileReader.getRepositoryName()));
        response.then().assertThat().body("private",equalTo(false));
        response.then().assertThat().body("owner.login",equalTo(properties.getProperty("ownerName")));
        response.then().assertThat().time(Matchers.lessThan(60000l));
        response.then().assertThat().time(Matchers.both(Matchers.lessThan(60000l)).and(Matchers.greaterThan(300l)));

    }

    @Test(priority = 2)
    public void testTest(){
        given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.getProperty("ownerName"))
                .pathParam("repo",response.jsonPath().get("name"))

                .when()
                .get("repos/{owner}/{repo}/hooks")

                .then()
                .log().all();

    }

    @Test(dependsOnMethods = {"createRepositoryTest"},priority = 3)
    public void getRepositoryTest(){
        //Precondition
        response=given()
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.getProperty("ownerName"))
                .pathParam("repo",response.jsonPath().get("name"))
                .log().all()


                //HTTP METHODS
                .when()
                .get("/repos/{owner}/{repo}");

        //Few assertion for validation
        response.then().assertThat().statusCode(200);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("Ok"));
        response.then().assertThat().body("name",equalTo(JsonFileReader.getRepositoryName()));
        response.then().assertThat().body("full_name",equalTo(properties.getProperty("ownerName")+"/"+JsonFileReader.getRepositoryName()));
        response.then().assertThat().body("private",equalTo(false));
    }

    @Test(dependsOnMethods = {"createRepositoryTest"},priority = 4)
    public void updateRepositoryTest(){

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("private",true);
        jsonObject.put("description","demo update description");

        //Pre-Condition
        response=given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.get("ownerName"))
                .pathParam("repo",response.jsonPath().get("name"))
                .body(jsonObject)

                //HTTP Methods
                .when()
                .patch("/repos/{owner}/{repo}");

        //Few assertion for validation
        response.then().assertThat().statusCode(200);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("Ok"));
        response.then().assertThat().body("name",equalTo(JsonFileReader.getRepositoryName()));
        response.then().assertThat().body("full_name",equalTo(properties.getProperty("ownerName")+"/"+JsonFileReader.getRepositoryName()));
        response.then().assertThat().body("private",equalTo(true));
        response.then().assertThat().body("description",equalTo(jsonObject.get("description")));
    }


    @Test(priority = 5)
    public void deleteRepositoryTest(){
       response=given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.getProperty("ownerName"))
                .pathParam("repo",JsonFileReader.getRepositoryName())

                .when()
                .delete("/repos/{owner}/{repo}");

        //Few assertion for validation
        response.then().assertThat().statusCode(204);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("No Content"));
        response.then().assertThat().time(Matchers.lessThan(60000l));
    }



}
