package com.qa.api.booking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;


public class TokenTest {


    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @AfterTest
    public void teardown() {

        playwright.close();
    }

    @Test
    public void Token_Gen_Test() throws IOException {

//        String Json
        String tokenJsonBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        APIResponse apiPOSTTokenResponse = requestContext.post("https://restful-booker.herokuapp.com/auth",
                RequestOptions.create().
                        setHeader("Content-Type", "application/json").
                        setData(tokenJsonBody)
        );

        System.out.println("Response Status is : " + apiPOSTTokenResponse.status());
        Assert.assertEquals(apiPOSTTokenResponse.status(), 200);
        Assert.assertEquals(apiPOSTTokenResponse.statusText(), "OK");
        System.out.println("******Response Text is :" + apiPOSTTokenResponse.text());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResp = objectMapper.readTree(apiPOSTTokenResponse.body());
        System.out.println("***********Json Response is :" + postJsonResp.toPrettyString());

//        capture token from above json response to further validate/assert
        String tokenID = postJsonResp.get("token").asText();
        System.out.println("Token id is :" + tokenID);
        Assert.assertNotNull(tokenID);

    }
}