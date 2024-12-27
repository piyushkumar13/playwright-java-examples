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
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Piyush Kumar.
 * @since 25/12/24.
 */

/* Instead of explictly creating Playwright, browser and page object, we can use @UsePlaywright annotation
 * which will take care of creating these object and to access these objects in test method, we can pass these objects
 * as parameters in test method. */

@UsePlaywright(_5BrowserConfigWithUsePlaywrightAnnotation.CustomBrowserOptions.class)
public class _5BrowserConfigWithUsePlaywrightAnnotation {

    public static class CustomBrowserOptions implements OptionsFactory {

        @Override
        public Options getOptions() {
            return new Options().setLaunchOptions(
                new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setArgs(List.of("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            ).setTestIdAttribute("data-test");
        }
    }

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
}
