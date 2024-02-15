package com.qa.api.tests.DELETE;

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

public class DeleteUserAPI_Test {

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
    public void deleteUser_DELETE_Test() throws IOException {

        String randomStr = getRandomString();
        System.out.println("UserName is :"+randomStr);

//        1.Creating new user POST Api --> 201
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
        System.out.println("*************Created User details*************");
        System.out.println("Response Status is : "+apiPOSTResponse.status());
        Assert.assertEquals(apiPOSTResponse.status(), 201);
        Assert.assertEquals(apiPOSTResponse.statusText(), "Created");

        String responseText = apiPOSTResponse.text();
        System.out.println("******Response Text is :"+responseText );

//        Convert reponse text/json to POJO - Deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        User_pojo actualUserPojo = objectMapper.readValue(responseText,  User_pojo.class);
        System.out.println("New User details from response are :"+actualUserPojo);
        Assert.assertEquals(actualUserPojo.getName(), userLombok.getName());
        Assert.assertEquals(actualUserPojo.getEmail(), userLombok.getEmail());
        Assert.assertEquals(actualUserPojo.getStatus(), userLombok.getStatus());
        Assert.assertEquals(actualUserPojo.getGender(), userLombok.getGender());
        Assert.assertNotNull(actualUserPojo.getId());

        String userId = actualUserPojo.getId();
        System.out.println("New User userId is :"+userId);

//        2.Delete the User using DELETE API  --> 204
        APIResponse apiDELETEresponse = requestContext.delete("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create().
                        setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb").
                        setData(userLombok)
                );
        System.out.println("*****************Deleted User details*******************");
        System.out.println(apiDELETEresponse.status());
        System.out.println(apiDELETEresponse.statusText());
        Assert.assertEquals(apiDELETEresponse.status(), 204);
        Assert.assertEquals(apiDELETEresponse.statusText(), "No Content");

        System.out.println("Delete User response body is :"+apiDELETEresponse.text());

//        GET call to validate deleted user -->  404

        APIResponse apiGETResponse = requestContext.get("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 740f35a2f0aa61d576ce9fa45123dda2c2d8271f7f9ed36bcab02914f91f0cbb"));

        System.out.println("*****************GET User details*******************");
        int statusGETCode = apiGETResponse.status();
        System.out.println("API Response Code is :" +statusGETCode);
        Assert.assertEquals(statusGETCode, 404);
        String statusGETText = apiGETResponse.statusText();
        System.out.println("API Response status text is :" +statusGETText);
        Assert.assertEquals(statusGETText, "Not Found");
        System.out.println("GET call response body is :"+apiGETResponse.text());
        Assert.assertTrue(apiGETResponse.text().contains("Resource not found"));
    }
}
