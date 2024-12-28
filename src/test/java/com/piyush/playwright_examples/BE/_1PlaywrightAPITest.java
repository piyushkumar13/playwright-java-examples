/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples.BE;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.impl.RequestOptionsImpl;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Swagger documentation of practicesoftwaretesting is present here: https://api.practicesoftwaretesting.com/api/documentation.
 *
 * Playwright API does not contain following featurs :
 * 1. API retry mechanism on HTTP code. It provides only retry for connection refused error.
 * 2. It does not provide retries after a delay.
 * 3. It does not provide OAuth flows out of the box.
 *
 *
 * @author Piyush Kumar.
 * @since 28/12/24.
 */
public class _1PlaywrightAPITest {

    private static ObjectMapper MAPPER = new ObjectMapper();

    private record User(
        String first_name,
        String last_name,
        String address,
        String city,
        String state,
        String country,
        String postcode,
        String phone,
        String dob,
        String email,
        String password
    ){};

    @Test
    public void testGetAPI() throws IOException {

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequestContext = playwright
            .request()
            .newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://api.practicesoftwaretesting.com")
                .setExtraHTTPHeaders(
                    Map.of(
                        "Accept", "application/json",
                        "Content-Type", "application/json")
                )
            );

        APIResponse apiResponse = apiRequestContext.get("/products?page=2");

        byte[] body = apiResponse.body();

        JsonNode jsonNode = MAPPER.readTree(body);

        System.out.println("=================== API Json Response Starts ==================");
        System.out.println("Json string : " + jsonNode.toPrettyString());
        System.out.println("=================== API Json Response Ends ==================");

        System.out.println("=================== API Response as Text Starts ==================");
        System.out.println("Text api response : " + apiResponse.text());
        System.out.println("=================== API Json Response as Text Ends ==================");

        System.out.println("=================== API URL Starts ==================");
        System.out.println("API Url : " + apiResponse.url());
        System.out.println("=================== API URL Ends ==================");

        System.out.println("=================== API Headers Starts ==================");
        System.out.println("API Headers : " + apiResponse.headers());
        System.out.println("=================== API Headers Ends ==================");

        System.out.println("=================== API Status Code Starts ==================");
        System.out.println("API Headers : " + apiResponse.status());
        System.out.println("=================== API Status Code Ends ==================");


        assertTrue(apiResponse.ok()); // Checks if status is in the range of 200-299

        apiResponse.dispose(); // either do this
//        apiRequestContext.dispose(); // or do this. This will dispose the whole request context so you cannot make further calls on request context.
        playwright.close();
    }

    @Test
    public void testGetAPIWithRetry() throws IOException {

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequestContext = playwright
            .request()
            .newContext(new APIRequest.NewContextOptions().setBaseURL("https://api.practicesoftwaretesting.com"));

        APIResponse apiResponse = apiRequestContext.get(
            "/products",
            new RequestOptionsImpl()
                .setQueryParam("page", 2)
                .setMaxRetries(2) // Retries only in case of connectivity issue not on http code
                .setFailOnStatusCode(true) // Throws exception in case of failure however by default it does not throw error if response code is error code.
//                .setIgnoreHTTPSErrors()
        );

        byte[] body = apiResponse.body();

        JsonNode jsonNode = MAPPER.readTree(body);

        System.out.println("=================== API Json Response Starts ==================");
        System.out.println("Json string : " + jsonNode.toPrettyString());
        System.out.println("=================== API Json Response Ends ==================");


        assertTrue(apiResponse.ok()); // Checks if status is in the range of 200-299

        apiResponse.dispose(); // either do this
//        apiRequestContext.dispose(); // or do this. This will dispose the whole request context so you cannot make further calls on request context.

        playwright.close();
    }

    @Test
    public void testPOSTAPI() throws IOException {

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequestContext = playwright
            .request()
            .newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://api.practicesoftwaretesting.com")
                .setExtraHTTPHeaders(
                    Map.of(
                        "Accept", "application/json",
                        "Content-Type", "application/json")
                )
            );

        APIResponse apiResponse = apiRequestContext.post("/users/register", new RequestOptionsImpl()
            .setData(createUser())
            .setHeader("Content-Type", "application/json")
        );

        byte[] body = apiResponse.body();

        JsonNode jsonNode = MAPPER.readTree(body);

        System.out.println("=================== API Json Response Starts ==================");
        System.out.println("Json string : " + jsonNode.toPrettyString());
        System.out.println("=================== API Json Response Ends ==================");

        System.out.println("Status code : " + apiResponse.status());

        assertThat(apiResponse.status()).isEqualTo(201);

        assertTrue(apiResponse.ok()); // Checks if status is in the range of 200-299


        apiResponse.dispose(); // either do this
//        apiRequestContext.dispose(); // or do this. This will dispose the whole request context so you cannot make further calls on request context.
        playwright.close();
    }

    private User createUser(){

        Faker faker = new Faker();

        User user = new User(faker.name().firstName(),
            faker.name().lastName(),
            faker.address().fullAddress(),
            faker.address().city(),
            faker.address().state(),
            faker.country().countryCode2(),
            faker.address().zipCode(),
            faker.phoneNumber().phoneNumber(),
            "1990-01-01",
            faker.name().firstName() + "@" + faker.name().lastName() + ".com",
            faker.name().firstName() + "@AK190");

        System.out.println("Created user is : " + user);

        return user;
    }


}
