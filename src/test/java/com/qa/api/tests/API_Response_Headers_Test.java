package com.qa.api.tests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class API_Response_Headers_Test {

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
    public void getHeadersList(){
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");

        int statusCode = apiResponse.status();
        System.out.println("API Response Code is :" +statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiResponse.ok());

//        headers() method
        Map<String,String> headerMap = apiResponse.headers();
//        headerMap.forEach((k,v) -> System.out.println(k+" : "+v));
        System.out.println("Total no. of Response Headers are :"+headerMap.size());

//        headersArray() Method
        List<HttpHeader> headerList = apiResponse.headersArray();
        for(HttpHeader e : headerList){
            System.out.println(e.name +" : "+ e.value );
        }
    }


    @AfterTest
    public void teardown(){
        playwright.close();
    }
}
