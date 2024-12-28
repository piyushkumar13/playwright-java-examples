/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples.UI;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

/**
 * @author Piyush Kumar.
 * @since 28/12/24.
 */
public class _14MockAPIsForUI {


    @Test
    public void testWithoutMockAPIOfUIApp(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("pliers"); // Basically, this makes api call i.e https://api.practicesoftwaretesting.com/products/search?q=pliers
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        page.waitForResponse("**/products/search**", () -> {});

        int count = page.locator(".card").count();
        assertThat(count).isEqualTo(4);

        browser.close();
        playwright.close();
    }

    @Test
    public void testWithMockAPIOfUIApp(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.route("**/products/search?q=pliers", route -> { // Mocking the API here with single entry response
            route.fulfill(new Route.FulfillOptions()
                .setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                .setStatus(200)
            );
        });

        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("pliers"); // Basically, this makes api call i.e https://api.practicesoftwaretesting.com/products/search?q=pliers
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

//        page.waitForResponse("**/products/search**", () -> {}); // Now, this is not required since we have mocked the API.

        int count = page.locator(".card").count();
        assertThat(count).isEqualTo(1);

        browser.close();
        playwright.close();
    }

    @Test
    public void testWithMockAPIsOfUIAppWithEmptyResponse(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.route("**/products/search?q=pliers", route -> { // Mocking the API here with empty entry response
            route.fulfill(new Route.FulfillOptions()
                .setBody(MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
                .setStatus(200)
            );
        });

        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("pliers"); // Basically, this makes api call i.e https://api.practicesoftwaretesting.com/products/search?q=pliers
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

//        page.waitForResponse("**/products/search**", () -> {}); // Now, this is not required since we have mocked the API.

        int count = page.locator(".card").count();
        assertThat(count).isEqualTo(0);

        browser.close();
        playwright.close();
    }

    public static class MockSearchResponses {
        public static final String RESPONSE_WITH_A_SINGLE_ENTRY = """
            {
                "current_page": 1,
                "data": [
                    {
                        "id": "01JBSC2JBTD1HY15BZQR9RMBB8",
                        "name": "Super Pliers",
                        "description": "A really good pair of pliers",
                        "price": 14.15,
                        "is_location_offer": false,
                        "is_rental": false,
                        "in_stock": true,
                        "product_image": {
                            "id": "01JBSC2JBJ445KGXKVSE1VDE69",
                            "by_name": "Helinton Fantin",
                            "by_url": "https://unsplash.com/@fantin",
                            "source_name": "Unsplash",
                            "source_url": "https://unsplash.com/photos/W8BNwvOvW4M",
                            "file_name": "pliers01.avif",
                            "title": "Super pliers"
                        }
                    }
                ],
                "from": 1,
                "last_page": 1,
                "per_page": 9,
                "to": 1,
                "total": 1
            }
            """;

        public static final String RESPONSE_WITH_NO_ENTRIES = """
            {
                "current_page": 1,
                "data": [],
                "from": 1,
                "last_page": 1,
                "per_page": 9,
                "to": 1,
                "total": 0
            }
            """;

    }
}
