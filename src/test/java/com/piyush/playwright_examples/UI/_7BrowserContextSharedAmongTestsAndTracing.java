/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples.UI;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;

/**
 * @author Piyush Kumar.
 * @since 25/12/24.
 */

/**
 *  When we share playwright, browser, browser context among all the testcases, testcase execution takes lesser time.
 *
 *  On trace details can be found here : https://playwright.dev/java/docs/trace-viewer-intro
 *
 *  To see the trace, you can do following :
 *
 *  1. Go to https://trace.playwright.dev/ and drag and drop trace zip file
 *  2. Or run this command from the project root directory mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace trace.zip"
 *  3. Or run npx playwright show-trace {trace zip file}
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class _7BrowserContextSharedAmongTestsAndTracing {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @BeforeAll
    public void oneTimeSetUp() {

        System.out.println("Inside before all one time setup");

        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        browserContext = browser.newContext();

    }

    @BeforeEach
    public void setUp(){
        System.out.println("Inside before each setup");

        browserContext.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true));

        page = browserContext.newPage();
    }

    @AfterEach
    public void traceEach(TestInfo testInfo){

        String testName = testInfo.getDisplayName().replace(" ", "-").toLowerCase();

        browserContext.tracing().stop(new Tracing.StopOptions()
            .setPath(Paths.get("trace-" + testName + ".zip")));
    }

    @AfterAll
    public void teardown() {

        System.out.println("Inside after all teardown");

        browserContext.close();
        browser.close();
        playwright.close();
    }


    @Test
    public void testBasicPlaywrightSetupAndSiteTitle() {


        page.navigate("https://practicesoftwaretesting.com");

        String title = page.title();
        System.out.println("title is : " + title);

        assertThat(title).isEqualTo("Practice Software Testing - Toolshop - v5.0");
    }

    @Test
    public void testSearchByKeywordUsingTimeOut() {

        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");
//        page.getByPlaceholder("Search").fill("Pliers"); // you can also search like this.

        page.locator("button:has-text('Search')").click();

        /* We should not use timeout for waiting rather we should use wait method on some event like waitForResponse, waitForSelector etc. */
        page.waitForTimeout(2000);

        int cardsCount = page.locator(".card").count();

        System.out.println("The cards count is : " + cardsCount);

        assertThat(cardsCount).isEqualTo(4);

    }

    @Test
    public void testSearchByKeywordUsingWaitForResponseWithEmptyCallback() {

        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");

        page.locator("button:has-text('Search')").click();

        page.waitForResponse(
            response -> response.url().equalsIgnoreCase("https://api.practicesoftwaretesting.com/products/search?q=pliers") && response.status() == 200,
            new Page.WaitForResponseOptions().setTimeout(2000),
            () -> {
            }
        );

        int cardsCount = page.locator(".card").count();

        System.out.println("The cards count is : " + cardsCount);

        assertThat(cardsCount).isEqualTo(4);
    }

    @Test
    public void testSearchByKeywordUsingWaitForResponseUsingCallback() {

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

    @Test
    public void testSearchByKeywordUsingWaitForResponseWithoutTimeoutUsingCallback() {

        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");

        page.waitForResponse(
            response -> response.url().equalsIgnoreCase("https://api.practicesoftwaretesting.com/products/search?q=pliers") && response.status() == 200,
            () -> page.locator("button:has-text('Search')").click()
        );

        int cardsCount = page.locator(".card").count();

        System.out.println("The cards count is : " + cardsCount);

        assertThat(cardsCount).isEqualTo(4);
    }

    @Test
    public void testSearchByKeywordUsingWaitForResponseWithoutTimeoutUsingCallback2() {

        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");

//        page.locator("button:has-text('Search')").click();

        page.waitForResponse("https://api.practicesoftwaretesting.com/products/search**",
            () -> page.locator("button:has-text('Search')").click()
        );

        // Below one will also work
//        page.waitForResponse("**/products/search**",
//            () -> page.locator("button:has-text('Search')").click()
//        );
//
        int cardsCount = page.locator(".card").count();

        System.out.println("The cards count is : " + cardsCount);

        assertThat(cardsCount).isEqualTo(4);
    }
}
