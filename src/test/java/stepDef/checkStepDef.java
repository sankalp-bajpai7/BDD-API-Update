package stepDef;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import java.io.IOException;

public class checkStepDef {

    RequestSpecification request = RestAssured.given();
    Response response;
    static String userId;
    String requestURL;
    String baseurl;
    String endpoint;
    String responseBody;
    public static String status;
    public static String token;

    @Before

    public void Setup() throws IOException, ParseException {
        request.log().all();
    }

    @Given("the request api is formed with valid requestbody")
    public void the_request_api_is_formed_with_valid_requestbody() {
        baseurl = "https://bookstore.toolsqa.com";
        endpoint = "/Account/v1/GenerateToken";
        request.headers("Content-Type","application/json");
        requestURL = baseurl + endpoint;
    }
    @When("User sends the request api with appropriate requestbody {string} {string}")
    public void user_sends_the_request_api_with_appropriate_requestbody(String username, String password) {
        JSONObject requestParam = new JSONObject();
        requestParam.put("userName", username);
        requestParam.put("password", password);
        request.body(requestParam.toJSONString());
        response = request.post(requestURL);
    }
    @Then("User should be able to validate success status code {string}")
    public void user_should_be_able_to_validate_success_status_code(String statuscode) {
        responseBody = response.getBody().asString();
        status = JsonPath.from(responseBody).get("status");
        token = JsonPath.from(responseBody).get("token");
        String statusCode = String.valueOf(response.getStatusCode());
        System.out.println(responseBody);
        System.out.println(statusCode);
        Assert.assertEquals(statusCode, statuscode);
    }
}
