package SimpleGitApiTestingUsingPojoClass;

import BaseUtility.BaseTest;
import PojoClass.GitPojoClass;
import RequestSpecificationUtility.RequestSpecUtility;
import Utility.MaskingFilter;
import Utility.PojoObject;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class GitHubCrudOperationUsingPojoClassTest extends BaseTest {
   private static Response response;
   private static GitPojoClass gitPojoObject;

    @Test(priority = 1)
    public void createRepositoryUsingPojoClassTest(){
        //Fetch repository data from Pojo Object utility
       gitPojoObject= PojoObject.createPojoObject();

        //PRECONDITION
        response =given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .body(gitPojoObject)

                //HTTP METHOD WILL RETURN RESPONSE
                .when()
                .post("/user/repos");

        //Few assertion for validation
        response.then().log().all();
        response.then().assertThat().statusCode(201);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("Created"));
        response.then().assertThat().body("name",equalTo(gitPojoObject.getName()));
        response.then().assertThat().body("private",equalTo(false));
        response.then().assertThat().body("owner.login",equalTo(properties.getProperty("ownerName")));
        response.then().assertThat().time(Matchers.lessThan(60000l));
        response.then().assertThat().time(Matchers.both(Matchers.lessThan(60000l)).and(Matchers.greaterThan(300l)));
    }

    @Test(dependsOnMethods = {"createRepositoryUsingPojoClassTest"},priority = 2)
    public void getRepositoryUsingPojoClassTest(){
        //Precondition
        response=given()
               // .filter(new MaskingFilter())
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.getProperty("ownerName"))
                .pathParam("repo",response.jsonPath().get("name"))
                .log().all()

                //HTTP METHODS
                .when()
                .get("/repos/{owner}/{repo}");


        //Few assertion for validation
        response.getBody().prettyPrint();
        response.then().body(matchesJsonSchemaInClasspath("schema.json"));
        response.then().assertThat().statusCode(200);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("Ok"));
        response.then().assertThat().body("name",equalTo(PojoObject.repositoryName()));
      //  response.then().assertThat().body("full_name",equalTo(properties.getProperty("ownerName")+"/"+PojoObject.repositoryName()));
        response.then().assertThat().body("private",equalTo(false));
    }

    @Test(dependsOnMethods = {"createRepositoryUsingPojoClassTest"},priority = 3)
    public void updateRepositoryUsingPojoClassTest(){
        //Simple updated description
        gitPojoObject.setDescription("description demo update description");

        //Pre-Condition
        response=given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.get("ownerName"))
                .pathParam("repo",response.jsonPath().get("name"))
                .body(gitPojoObject)

                //HTTP Methods
                .when()
                .patch("/repos/{owner}/{repo}");

        //Few assertion for validation
        response.then().log().all();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("Ok"));
        response.then().assertThat().body("name",equalTo(PojoObject.repositoryName()));
        response.then().assertThat().body("full_name",equalTo(properties.getProperty("ownerName")+"/"+PojoObject.repositoryName()));
        response.then().assertThat().body("private",equalTo(false));
        response.then().assertThat().body("description",equalTo(gitPojoObject.getDescription()));
    }

    @Test(dependsOnMethods = {"createRepositoryUsingPojoClassTest"},priority = 4)
    public void updateRepository2UsingPojoClassTest(){
    //update done to update the repository privatization which us not possible by pojo class
    //but solved using hashmap
      HashMap<String,Object> hashMap=new HashMap<>();
      hashMap.put("description","description 2 demo update description");
      hashMap.put("private",true);

        //Pre-Condition
        response=given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.get("ownerName"))
                .pathParam("repo",response.jsonPath().get("name"))
                .body(hashMap)

                //HTTP Methods
                .when()
                .patch("/repos/{owner}/{repo}");

        //Few assertion for validation
        response.then().log().all();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("Ok"));
        response.then().assertThat().body("name",equalTo(PojoObject.repositoryName()));
        response.then().assertThat().body("full_name",equalTo(properties.getProperty("ownerName")+"/"+PojoObject.repositoryName()));
        response.then().assertThat().body("private",equalTo(true));
        response.then().assertThat().body("description",equalTo(hashMap.get("description")));
    }

    @Test(priority = 5)
    public void deleteRepositoryUsingPojoClassTest(){
        response=given()
                .spec(RequestSpecUtility.getRequestSpecification())
                .auth().oauth2(properties.getProperty("apiToken"))
                .pathParam("owner",properties.getProperty("ownerName"))
                .pathParam("repo",PojoObject.repositoryName())

                .when()
                .delete("/repos/{owner}/{repo}");

        //Few assertion for validation
        response.then().assertThat().statusCode(204);
        response.then().assertThat().statusLine(Matchers.containsStringIgnoringCase("No Content"));
        response.then().assertThat().time(Matchers.lessThan(60000l));
    }
}
