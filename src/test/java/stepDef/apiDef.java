package stepDef;


import java.io.FileInputStream;
import java.io.InputStream;

import Util.Configuration;
import Util.Headers;
import Util.mongoDb;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


public class apiDef {

    RequestSpecification request = RestAssured.given();
    Response response;
    static String userId;
    String requestURL;
    String baseurl;
    String endpoint;
    String requestparam;
    JsonNode addressNode;

    Configuration config;
    String responseBody;

    public static String token;
    public static String status;
    public static String result;
    mongoDb Mongodb = new mongoDb();

    public apiDef() throws FileNotFoundException {
    }

    @Before

    public void Setup() throws IOException, ParseException {
        request.log().all();
    }


    @Given("the request api is formed with {string}")
    public void createRequestWithyaml(String  requestBody) throws IOException {
         ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
         InputStream inputStream = new FileInputStream("src/test/resources/testDataResources/data.yaml");
         config = mapper.readValue(inputStream, Configuration.class);
         baseurl = config.getBaseurl();
         endpoint = config.getCreateTokenendpoint();



         // set request Headers via yaml
         request.headers(Headers.headers);

        //get required requestbody based on scenario
        ObjectMapper jsonmapper = new ObjectMapper();
        JsonNode jsonNode = jsonmapper.readTree(Paths.get("src/test/resources/testDataResources/requestbody.json").toFile());

        //Extract the "required requestbody" node
        JsonNode addressNode = jsonNode.get(requestBody);
        requestparam = jsonmapper.writeValueAsString(addressNode);
        System.out.println("new request param is " + requestparam);
        RestAssured.baseURI = baseurl;
        //request.headers("Content-Type","application/json");
        requestURL = baseurl + endpoint;
    }


    @When("User sends the request api with valid requestbody {string} {string}")
    public void sendValidRequestBody(String username, String password) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        addressNode = mapper.readTree(requestparam);
        ((ObjectNode) addressNode).put("userName", username);
        ((ObjectNode) addressNode).put("password", password);

        String updatedBody = mapper.writeValueAsString(addressNode);
        System.out.println(updatedBody);
        response = request.body(updatedBody).post(requestURL);


//        Map<String, String> updatedBody = new HashMap<String, String>();
//        if (!(username == null || username.trim().isEmpty())) {
//            updatedBody.put("userName", username);
//            updatedBody.put("password", password);
//        } else {
//            updatedBody.put("password", password);
//        }
//
//        System.out.println(updatedBody);
//        response = request.body(updatedBody).post(requestURL);

    }

    @When("User sends the request api with invalid requestbody {string}")
    public void user_sends_the_request_api_with_invalid_requestbody(String password) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        addressNode = mapper.readTree(requestparam);

        ((ObjectNode) addressNode).put("password", password);

        String updatedBody = mapper.writeValueAsString(addressNode);
        System.out.println(updatedBody);
        response = request.body(updatedBody).post(requestURL);
//        response = request.body(updatedBody).delete(requestURL);
//        response = request.body(updatedBody).put(requestURL);

    }


    @Then("User should be able to validate valid status code {string}")
    public void validateResponse(String statuscode) {
        responseBody = response.getBody().asString();
        token = JsonPath.from(responseBody).get("token");
        status = JsonPath.from(responseBody).get("status");
        result = JsonPath.from(responseBody).get("result");
        String statusCode = String.valueOf(response.getStatusCode());
        System.out.println(responseBody);
        System.out.println(statusCode);
        Assert.assertEquals(statusCode, statuscode);

        Mongodb.setResponseDbCon();
        Mongodb.addUpdatedResponse(token,status,result);
        Mongodb.closeConnection();

      //  response2  = request.body(responseBody).post(requestURL);
    }

    @Then("User should be able to validate invalid status code {string}")
    public void invalidResponse(String invalidstatuscode) {
        responseBody = response.getBody().asString();
        String code = JsonPath.from(responseBody).get("code");
        String message = JsonPath.from(responseBody).get("message");
        String statusCode = String.valueOf(response.getStatusCode());
        System.out.println(responseBody);
        Assert.assertEquals(statusCode, invalidstatuscode);
        if (responseBody.contains("UserName and Password required")){
            System.out.println("Request sent was invalid, missing parameters");
        }
        Mongodb.setResponseDbCon();
        Mongodb.addInvalidResponsetoDb(responseBody,code,message);
        Mongodb.closeConnection();
    }


    @Then("I want to append the {string} to the db")
    public void addUsernametoDB(String username) {
        Mongodb.establishConnection();
        //Search the db for the username, if there are entries then delete them all
        Mongodb.deleteManyRecords(username);
        //When all entries for the username are deleted then enter the new username record
        Mongodb.addRecords(username);
        Mongodb.closeConnection();
    }

    @After
    public void teardown() throws InterruptedException {

    }

}
