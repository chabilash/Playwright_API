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

public class UpdateBookingTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    private static String TOKEN_ID = null;



    @BeforeTest
    public void setup() throws IOException {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

//        Get the Token
        String tokenJsonBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        APIResponse apiPOSTTokenResponse = requestContext.post("https://restful-booker.herokuapp.com/auth",
                RequestOptions.create().
                        setHeader("Content-Type", "application/json").
                        setData(tokenJsonBody)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResp = objectMapper.readTree(apiPOSTTokenResponse.body());
        System.out.println("***********Json Response is :" + postJsonResp.toPrettyString());

//        capture token from above json response to further validate/assert
        TOKEN_ID = postJsonResp.get("token").asText();
        System.out.println("Token id is :" + TOKEN_ID);

    }

    @AfterTest
    public void teardown(){

        playwright.close();
    }

    @Test
    public void updateBookingTest(){
        String bookingJsonReqBody = "{\n" +
                "    \"firstname\" : \"James\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2025-01-01\",\n" +
                "        \"checkout\" : \"2025-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Lunch\"\n" +
                "}";

        APIResponse apiPUTResponse = requestContext.put("https://restful-booker.herokuapp.com/booking/1",
                RequestOptions.create().
                        setHeader("Content-Type", "application/json").
                        setHeader("Cookie", "token="+TOKEN_ID).
                        setData(bookingJsonReqBody)  );
//        PUT call response
        System.out.println("***********************************API PUT call response");
        System.out.println(apiPUTResponse.status() +" : "+ apiPUTResponse.statusText());
        Assert.assertEquals(apiPUTResponse.status(),200);
        System.out.println(apiPUTResponse.text());


    }
}
