package com.qa.api.tests;

import com.api.java.User_pojo;
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

public class POST_CreateUser_POJO_Test {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    static String randomString = null;



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

    public static String getRandomString(){
        randomString = "Abi"+System.currentTimeMillis();
        return randomString;
    }


    @Test
    public void createUser_POST_Test() throws IOException {

        String randomStr = getRandomString();
        System.out.println("UserName is :"+randomStr);

//        Creating User class Object
        User_pojo expecteduserPojo = new User_pojo( randomStr,randomStr+"@gmail.com", "male", "active");


        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create().
                        setHeader("Content-Type", "application/json").
                        setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb").
                        setData(expecteduserPojo)
        );

        System.out.println("Response Status is : "+apiResponse.status());
        Assert.assertEquals(apiResponse.status(), 201);
        Assert.assertEquals(apiResponse.statusText(), "Created");

        String responseText = apiResponse.text();
        System.out.println("******Response Text is :"+responseText );

//        Convert reponse text/json to POJO - Deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        User_pojo actualUserPojo = objectMapper.readValue(responseText,  User_pojo.class);

        Assert.assertEquals(actualUserPojo.getName(), expecteduserPojo.getName());
        Assert.assertEquals(actualUserPojo.getEmail(), expecteduserPojo.getEmail());
        Assert.assertEquals(actualUserPojo.getStatus(), expecteduserPojo.getStatus());
        Assert.assertEquals(actualUserPojo.getGender(), expecteduserPojo.getGender());
        Assert.assertNotNull(actualUserPojo.getId());



    }
}
