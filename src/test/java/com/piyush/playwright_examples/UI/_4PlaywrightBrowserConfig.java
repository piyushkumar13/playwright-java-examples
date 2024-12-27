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
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Piyush Kumar.
 * @since 25/12/24.
 */
public class _4PlaywrightBrowserConfig {

    @Test
    public void testSearchByKeywordUsingTimeOut() {

        Playwright playwright = Playwright.create();

        Browser browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setArgs(List.of("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );

        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("Pliers");

        page.locator("button:has-text('Search')").click();

        /* We should not use timeout for waiting rather we should use wait method on some event like waitForResponse, waitForSelector etc. */
        page.waitForTimeout(2000);

        int cardsCount = page.locator(".card").count();

        System.out.println("The cards count is : " + cardsCount);

        assertThat(cardsCount).isEqualTo(4);

        browser.close();
        playwright.close();
    }

    @Test
    public void testSearchByKeywordUsingWaitForResponseUsingCallback() {

        Playwright playwright = Playwright.create();

        Browser browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setArgs(List.of("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");

        page.waitForResponse(
            response -> response.url().equalsIgnoreCase("https://api.practicesoftwaretesting.com/products/search?q=pliers") && response.status() == 200,
            new Page.WaitForResponseOptions().setTimeout(2000),
            () -> page.locator("button:has-text('Search')").click()
        );

        int cardsCount = page.locator(".card").count();

        System.out.println("The cards count is : " + cardsCount);

        assertThat(cardsCount).isEqualTo(4);

        browser.close();
        playwright.close();
    }
}
