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
import java.awt.Dimension;
import java.awt.Toolkit;
import org.junit.jupiter.api.Test;

/**
 * JS has three types of pop-ups :
 * 1. Alert
 * 2. Confirm
 * 3. Prompt
 *
 * @author Piyush Kumar.
 * @since 27/12/24.
 */
public class _10MaximizeWindow {


    @Test
    public void testMaximizeWindow() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        System.out.println("Width is : " + width);
        System.out.println("Height is : " + height);

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        /* We can also refer this site to find our window size : https://whatismyviewport.com/ */
        BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));

        Page page = browserContext.newPage();

        page.navigate("https://www.amazon.in");

        browserContext.close();
        browser.close();
        playwright.close();
    }
}
