/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples.UI;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * @author Piyush Kumar.
 * @since 27/12/24.
 */
public class _11RecordVideo {

    @Test
    public void testRecordVideoOfOperationPerformed(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        BrowserContext browserContext = browser.newContext(
            new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("test-execution-videos/"))
                .setRecordVideoSize(640, 480)
        );

        Page page = browserContext.newPage();

        page.navigate("https://academy.naveenautomationlabs.com/");

        page.locator("a:has-text('Login')").click();

        page.waitForTimeout(2000);

        browser.close();
        playwright.close();
    }
}
