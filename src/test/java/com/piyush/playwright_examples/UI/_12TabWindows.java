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
public class _12TabWindows {

    @Test
    public void testOpenPageInTab(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();

        page.navigate("https://www.orangehrm.com/");

        Page popup = page.waitForPopup(() -> {
            page.click("img[alt='linkedin logo']");
        });

        popup.waitForLoadState();
        System.out.println("Pop up title" + popup.title());

        browser.close();
        playwright.close();
    }

    @Test
    public void testOpenBlankPageInTabAndThenNavigate(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();

        page.navigate("https://www.orangehrm.com/");

        Page popup = page.waitForPopup(() -> {
            page.click("a[target='_blank']");
        });

        popup.waitForLoadState();
        popup.navigate("https://www.google.com/");

        System.out.println("Pop up title" + popup.title());
        System.out.println("Parent page title" + page.title());

        browser.close();
        playwright.close();
    }
}
