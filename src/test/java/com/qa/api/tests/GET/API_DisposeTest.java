package com.qa.api.tests.GET;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class API_DisposeTest {

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
    public void disposeResponseTest(){

        //Request No.-1
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

        apiResponse.dispose(); // Will dispose only the response body , but statuscode , url , statustext will remain same in cache

//        System.out.println("------API Response after dispose is :"+apiResponse.text());
        try{
            System.out.println("------API Response after dispose is :"+apiResponse.text());
        }
        catch (PlaywrightException e){
            System.out.println("API Response body is disposed");
        }
        int statusCodeDispose = apiResponse.status();
        System.out.println("API Response Code is :" +statusCodeDispose);

//        requestContext dispose
        requestContext.dispose();

    }


    @AfterTest
    public void teardown(){

        playwright.close();
    }
}
