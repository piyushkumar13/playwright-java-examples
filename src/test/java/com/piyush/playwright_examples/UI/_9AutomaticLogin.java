/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples.UI;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * @author Piyush Kumar.
 * @since 27/12/24.
 */
public class _9AutomaticLogin {

    @Test
    public void testLoginToCreateCredJson(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();


        page.navigate("https://demowebshop.tricentis.com/login");
        page.fill("#Email", "<provide email id>");
        page.fill("#Password", "<provide password>");

        page.click("input:has-text('Log in')");

        browserContext.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("credential.json")));

        browserContext.close();
        browser.close();
        playwright.close();
    }

    @Test
    public void testLoginToUseCredJson(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("credential.json")));
        Page page = browserContext.newPage();

        page.navigate("https://demowebshop.tricentis.com");

        page.waitForTimeout(4000);
        browserContext.close();
        browser.close();
        playwright.close();
    }

}
