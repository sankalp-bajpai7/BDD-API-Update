package stepDef;


import Util.Headers;
import Util.mongoDb;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.json.Json;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static stepDef.apiDef.*;

public class postloginStepDef {

    RequestSpecification request = RestAssured.given();
    Response response;

    String requestURL;

    String jsonString;
    String bookId;
    apiDef Apidef = new apiDef();
    mongoDb Mongodb = new mongoDb();


    public postloginStepDef() throws FileNotFoundException {
    }


    @Given("User has a valid auth token with  {string} {string} {string} {string}")
    public void createAuthToken(String requestbody, String username, String password, String statuscode) throws IOException {
        Apidef.createRequestWithyaml(requestbody);
        Apidef.sendValidRequestBody(username,password);
        Apidef.validateResponse(statuscode);
        System.out.println(token);
    }

    @When("A list of books is available")
    public void retrievebooklist() throws IOException {
        RestAssured.baseURI = Apidef.baseurl;
        String addbookendpoint = Apidef.config.getGetbooksendpoint();
        requestURL = Apidef.baseurl + addbookendpoint;
        response = request.get(requestURL);
        jsonString = response.asString();
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        System.out.println(books);
        Assert.assertTrue(books.size() > 0);
        bookId = books.get(0).get("isbn");


    }

    @And("User adds a book to the reading list {string}{string}")
    public void userAddsABookToTheReadingList(String requestBody1, String arg1) throws IOException {
        RestAssured.baseURI = Apidef.baseurl;
        String addbookendpoint = Apidef.config.getGetbooksendpoint();
        requestURL = Apidef.baseurl + addbookendpoint;
        Headers.headers.put("Authorization", "Bearer"+token);
        request.headers(Headers.headers);

        //updating Add a book request body with userid and bookid
        ObjectMapper jsonmapper = new ObjectMapper();
        JsonNode jsonNode = jsonmapper.readTree(Paths.get("src/test/resources/testDataResources/addABookRequestBody.json").toFile());

        //Extract the "required requestbody" node
        JsonNode addressNode = jsonNode.get(requestBody1);
        String requestparam = jsonmapper.writeValueAsString(addressNode);

        Map<String, String> newrequestbody = new HashMap<String, String>();
        newrequestbody.put("userId","9b5f49ab-eea9-45f4-9d66-bcf56a531b85");
        newrequestbody.put("$.collectionOfIsbns[0].isbn",bookId);

        response = request.body(newrequestbody).post(requestURL);



    }
    @Then("Validate that the book is added successfully")
    public void validateAddABook() {
        String statusCode = String.valueOf(response.getStatusCode());
        Assert.assertEquals(statusCode, 201);
    }

    @Given("I establish a connection")
    public void i_establish_a_connection() {
        Mongodb.establishConnection();
        Mongodb.createClusterCon();
    }
    @When("I access the record needed")
    public void i_access_the_record_needed() {
        Mongodb.accessRecords();
    }
    @Then("I am displayed the record")
    public void i_am_displayed_the_record() {
        Mongodb.deleteRecordfromDB();
        Mongodb.closeConnection();
    }

}
