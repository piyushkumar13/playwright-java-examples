/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples.UI;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import io.github.uchagani.jp.BrowserConfig;
import io.github.uchagani.jp.PlaywrightBrowserConfig;
import io.github.uchagani.jp.UseBrowserConfig;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * junit-playwright dependency provides way to provide custom configuration with annotation with configuration.
 * Its similar to @UsePlaywrightAnnotation.
 *
 * Check its readme for more details : https://github.com/uchagani/junit-playwright
 *
 * @author Piyush Kumar.
 * @since 25/12/24.
 */

@UseBrowserConfig(_6BrowserConfigWithBrowserConfigAnnotation.DefaultBrowserConfig.class)
public class _6BrowserConfigWithBrowserConfigAnnotation {


    /* We can also inject Playwright, Browser, BrowserContext object in the test methods as well. */
    @Test
    public void testBasicPlaywrightSetupAndSiteTitle(Page page) {

        page.navigate("https://practicesoftwaretesting.com");

        String title = page.title();
        System.out.println("title is : " + title);

        assertThat(title).isEqualTo("Practice Software Testing - Toolshop - v5.0");
    }

    @Test
    public void testSearchByKeywordUsingTimeOut(Page page) {

        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("Pliers"); // you can also search like this.

        page.locator("button:has-text('Search')").click();

        /* We should not use timeout for waiting rather we should use wait method on some event like waitForResponse, waitForSelector etc. */
        page.waitForTimeout(2000);

        int cardsCount = page.locator(".card").count();

        System.out.println("The cards count is : " + cardsCount);

        assertThat(cardsCount).isEqualTo(4);

    }

    @Test
    public void testSearchByKeywordUsingWaitForResponseUsingCallback(Page page) {

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
    }

    public static class DefaultBrowserConfig implements PlaywrightBrowserConfig {

        @Override
        public BrowserConfig getBrowserConfig() {
            return new BrowserConfig().chromium().launch();
        }
    }
}
