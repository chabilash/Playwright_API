package com.qa.api.tests.GET;

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
import java.util.Map;

public class GETAPICall {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;



    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }


    @Test
    public void getUsersAPITest() throws IOException {

        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");

        int statusCode = apiResponse.status();
        System.out.println("API Response Code is :" +statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiResponse.ok());

        String statusText = apiResponse.statusText();
        System.out.println("API Response status text is :" +statusText);
        Assert.assertEquals(statusText, "OK");

        System.out.println("------API url is :"+apiResponse.url());
        System.out.println("------Response in text is :"+apiResponse.text());

        System.out.println("-----API Response Headers are : -----");
        Map<String , String> headersMap = apiResponse.headers();
        System.out.println(headersMap);
        Assert.assertEquals(headersMap.get("content-type") , "application/json; charset=utf-8");
        Assert.assertEquals(headersMap.get("x-download-options") , "noopen");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println(jsonPrettyResponse);
    }

    @Test
    public void getAPIQueryParam() throws IOException {
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create().
                        setQueryParam("id",     6313322).
                        setQueryParam("status", "active").
                        setQueryParam("gender", "male")
                );

        int statusCode = apiResponse.status();
        System.out.println("API Response Code is :" +statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiResponse.ok());

        String statusText = apiResponse.statusText();
        System.out.println("API Response status text is :" +statusText);
        Assert.assertEquals(statusText, "OK");

        System.out.println("------API url is :"+apiResponse.url());
        System.out.println("------Response in text is :"+apiResponse.text());

        System.out.println("-----API Response Headers are : -----");
        Map<String , String> headersMap = apiResponse.headers();
        System.out.println(headersMap);
        Assert.assertEquals(headersMap.get("content-type") , "application/json; charset=utf-8");
        Assert.assertEquals(headersMap.get("x-download-options") , "noopen");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println(jsonPrettyResponse);


    }

    @AfterTest
    public void teardown(){

        playwright.close();
    }
}
