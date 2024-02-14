package com.qa.api.tests.PUT;

import com.api.java.User_Lombok;
import com.api.java.User_pojo;
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

public class PUT_UpdateUserPUT_POJO_Lombok {


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
        randomString = "Sachin"+System.currentTimeMillis();
        return randomString;
    }


    @Test
    public void createUser_POST_Test() throws IOException {

        String randomStr = getRandomString();
        System.out.println("UserName is :"+randomStr);

//        1. POST call to create User entry
        User_Lombok userLombok = User_Lombok.builder()
                .name("Sachin")
                .email(getRandomString()+"@gmail.com")
                .gender("male")
                .status("active").build();


        APIResponse apiPOSTResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create().
                        setHeader("Content-Type", "application/json").
                        setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb").
                        setData(userLombok)
        );

        System.out.println("Response Status is : "+apiPOSTResponse.status());
        Assert.assertEquals(apiPOSTResponse.status(), 201);
        Assert.assertEquals(apiPOSTResponse.statusText(), "Created");

        String responseText = apiPOSTResponse.text();
        System.out.println("******Response Text is :"+responseText );

//        Convert reponse text/json to POJO - Deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        User_Lombok actualUserPojo = objectMapper.readValue(responseText,  User_Lombok.class);

        Assert.assertEquals(actualUserPojo.getName(), userLombok.getName());
        Assert.assertEquals(actualUserPojo.getEmail(), userLombok.getEmail());
        Assert.assertEquals(actualUserPojo.getStatus(), userLombok.getStatus());
        Assert.assertEquals(actualUserPojo.getGender(), userLombok.getGender());
        Assert.assertNotNull(actualUserPojo.getId());

        String userId = actualUserPojo.getId();
        System.out.println("New User Id is :"+userId);
        System.out.println("______________________________________________________________________________________________________");

        //    2. PUT call to update user details --> here updating status of user to 'inactive & gender to 'female'

        userLombok.setStatus("inactive");
        userLombok.setGender("female");
        APIResponse apiPUTResponse = requestContext.put("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create().
                        setHeader("Content-Type", "application/json").
                        setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb").
                        setData(userLombok)
        );

        System.out.println(apiPUTResponse.status()+ " : " + apiPUTResponse.statusText());
        Assert.assertEquals(apiPUTResponse.status(),200);
        String responsePUTtext = apiPUTResponse.text();
        User_Lombok actualPUTUser = objectMapper.readValue(responsePUTtext,  User_Lombok.class);
        System.out.println(responsePUTtext);
        Assert.assertEquals(actualPUTUser.getId(), userId);
        Assert.assertEquals(actualPUTUser.getStatus(), userLombok.getStatus());
        Assert.assertEquals(actualPUTUser.getName(), userLombok.getName());

//        3. GET call to validate the changes
        APIResponse apiGETResponse = requestContext.get("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb"));

        int statusGETCode = apiGETResponse.status();
        System.out.println("API Response Code is :" +statusGETCode);
        Assert.assertEquals(statusGETCode, 200);
        Assert.assertTrue(apiGETResponse.ok());

        String statusGETText = apiGETResponse.statusText();
        System.out.println("API Response status text is :" +statusGETText);
        Assert.assertEquals(statusGETText, "OK");

//        Asserting PUT & GET responses
        User_Lombok actualGETUser = objectMapper.readValue(responsePUTtext,  User_Lombok.class);

        Assert.assertEquals(actualGETUser.getId(), userId);
        Assert.assertEquals(actualGETUser.getStatus(), userLombok.getStatus());
        Assert.assertEquals(actualGETUser.getName(), userLombok.getName());
        System.out.println("API GET Response text is :" +apiGETResponse.text());




    }











}
