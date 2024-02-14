package com.qa.api.tests.POST;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class POST_CreateUser_Test {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;



    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @AfterTest
    public void teardown(){

        playwright.close();
    }


    public static String randomStringSimple() {
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomString = null;
        return  randomString = RandomStringUtils.random(10, useLetters, useNumbers);
    }

    @Test
    public void createUser_POST_Test() throws IOException {

        String userName = POST_CreateUser_Test.randomStringSimple();
        System.out.println("User Name is :"+userName);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name" , userName);
        data.put("email" , userName+"@gmail.com");
        data.put("gender" , "male");
        data.put("status" , "active");


        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create().
                        setHeader("Content-Type", "application/json").
                        setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb").
                        setData(data)
        );

        System.out.println("Response Status is : "+apiResponse.status());
        Assert.assertEquals(apiResponse.status(), 201);
        Assert.assertEquals(apiResponse.statusText(), "Created");
        System.out.println("******Response Text is :"+ apiResponse.text());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResp = objectMapper.readTree(apiResponse.body());
        System.out.println("***********Json Response is :"+postJsonResp.toPrettyString());
//        capture id from above json response to further validate/assert
        String userId = postJsonResp.get("id").asText();
        System.out.println("User id is :"+userId);

//        Get Call to validate whether above user is created or not
        APIResponse getApiResponse = requestContext.get("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create().
                        setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb")
                );
        Assert.assertEquals(getApiResponse.status(), 200);
        Assert.assertEquals(getApiResponse.statusText(), "OK");
        System.out.println("Get Call API Response is: "+getApiResponse.text());
        Assert.assertTrue(apiResponse.text().contains(userId));
        Assert.assertTrue(apiResponse.text().contains(userName));


    }
}
