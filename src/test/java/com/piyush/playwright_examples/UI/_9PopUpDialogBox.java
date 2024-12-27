/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples.UI;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
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
public class _9PopUpDialogBox {


    /* By default, Playwright handles the pop ups automatically. */
    @Test
    public void testHandlePopUpDefaultBehaviour() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://the-internet.herokuapp.com/javascript_alerts");

        page.click("button:has-text('Click for JS Alert')"); // by default, Playwright clicks OK.
        String alertTxt = page.locator("#result").textContent();
        System.out.println("alertTxt : " + alertTxt);

        page.click("button:has-text('Click for JS Confirm')"); // by default, Playwright clicks Cancel.
        String confirmTxt = page.locator("#result").textContent();
        System.out.println("confirmTxt : " + confirmTxt);

        page.click("button:has-text('Click for JS Prompt')"); // by default, Playwright does not provide any msg and clicks Cancel.
        String promptTxt = page.locator("#result").textContent();
        System.out.println("confirmTxt : " + promptTxt);

        browser.close();
        playwright.close();
    }

    @Test
    public void testHandlePopUpWithSpecificBehaviour() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.onDialog(dialog -> {

            String message = dialog.message();
            System.out.println("Message is : " + message);

            dialog.accept("Hello! This is Piyush");// This is use for confirm and prompt as well. Confirm will be accepted and prompt will be accepted with message.
//            dialog.dismiss(); // is used to dismiss the pop up.
        });

        page.navigate("https://the-internet.herokuapp.com/javascript_alerts");

        page.click("button:has-text('Click for JS Alert')"); // by default, Playwright clicks OK.
        String alertTxt = page.locator("#result").textContent();
        System.out.println("alertTxt : " + alertTxt);

        page.click("button:has-text('Click for JS Confirm')"); // Now, Playwright clicks Confirm.
        String confirmTxt = page.locator("#result").textContent();
        System.out.println("confirmTxt : " + confirmTxt);

        page.click("button:has-text('Click for JS Prompt')"); // Now, Playwright provide msg and clicks Ok.
        String promptTxt = page.locator("#result").textContent();
        System.out.println("confirmTxt : " + promptTxt);

        browser.close();
        playwright.close();
    }
}
