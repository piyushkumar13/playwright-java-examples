/*
 *  Copyright (c) 2024 Piyush Kumar
 *  All Rights Reserved Worldwide.
 */

package com.piyush.playwright_examples;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * All the page methods which is used to locate elements like getByText, getByAltText, locater(..) etc does not have any auto waiting and also these are lazy in nature.
 * Means, unless we wont attach any locator method, it will not locate. There work is just to return locator object.
 * Whereas locator methods i.e click(), innerHTML(), innerText(), textContent() etc which performs some event operation adds auto-waiting where default timeout is 30 secs.
 * It adds auto-waiting as per this : https://playwright.dev/java/docs/actionability
 * NOTE : not all locator methods adds waiting.
 *
 * All locators are captured here : https://playwright.dev/java/docs/locators
 * Other locators like css, xpath etc are captured here : https://playwright.dev/java/docs/other-locators
 *
 * @author Piyush Kumar.
 * @since 25/12/24.
 */
public class _7Locators {




    /* ************************ Specialized Locator Methods *********************** */
    /*
        Page.getByRole() to locate by explicit and implicit accessibility attributes.
        Page.getByText() to locate by text content.
        Page.getByLabel() to locate a form control by associated label's text.
        Page.getByPlaceholder() to locate an input by placeholder.
        Page.getByAltText() to locate an element, usually image, by its text alternative.
        Page.getByTitle() to locate an element by its title attribute.
        Page.getByTestId() to locate an element based on its data-testid attribute (other attributes can be configured).
     */
    @Test
    public void testGetByText() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        /* Though on the website practicesoftwaretesting, Bolt Cutters is under h5 tag so below line will locate that tag(i.e h5) but on the website
         when we hover over this text it is clickable since its wrapped under anchor tag so here in playwright code also it is clickable. */
        Locator boltCutters = page.getByText("Bolt Cutters");
        boltCutters.click();

        System.out.println("BoltCutter opened");

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByTextWithIsVisible() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        /* click operation on page.getByText(...) or any locator method adds the auto waiting
         * Which means it will wait for the element to be found and then will perform the click operation on it.
         * If it could not find, default timeout is 30 secs.
         * */
        page.getByText("Bolt Cutters").click();

        Locator mightyCraftHardware = page.getByText("MightyCraft Hardware");

        String innerText = mightyCraftHardware.innerText();
        System.out.println("inner text" + innerText);

        /* All Playwright Assertions have default timout of 5 secs - it waits for this much amount of time to retry assertions. */
        PlaywrightAssertions.assertThat(mightyCraftHardware).isVisible();

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByAltText() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.getByAltText("Combination Pliers").click();

        String innerText = page.getByText("ForgeFlex Tools").innerText();

        System.out.println("inner text" + innerText);

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByTitle() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        /* getByTitle allows locating elements by their title attribute. */
        page.getByTitle("Practice Software Testing - Toolshop").click();

        browser.close();
        playwright.close();
    }

    /**
     * Few examples of getByRole
     * page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
     * page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setChecked(true));
     * page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Something"));
     * page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Something").setLevel(5));
     */
    @Test
    public void testGetByRole() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("pliers");

        /* This will locate the button which has text Search even though aria-label=Search is present or not.
         * As playwright consider their text as the aria-label as well. */
        Locator search = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
        search.click();

        PlaywrightAssertions.assertThat(page.locator("span:has-text('pliers')")).isVisible();

        /* Even though we have isVisible method in above line which adds waiting of 5 secs but still we would need to add waiting as below
         * becoz right after we click on search button we get the span with text pliers but search result takes sometime. */
        page.waitForResponse("**/products/search**", () -> {
        });

        Locator cards = page.locator(".card");
        List<String> strings = cards.allInnerTexts();

        System.out.println("Inner texts : " + strings);

        assertThat(cards.count()).isEqualTo(4);

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByTestId() {

        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test"); // NOTE this, we need to define what is the name of the test id attribute.
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        Locator byTestId = page.getByTestId("search-query");
        byTestId.fill("Pliers");

        String inputValue = byTestId.inputValue();
        System.out.println("Entered value is : " + inputValue);

        assertThat(inputValue).isEqualTo("Pliers");

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByLabel() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com/contact");

        Locator firstName = page.getByLabel("First name");
        firstName.fill("Piyush");

        PlaywrightAssertions.assertThat(firstName).hasValue("Piyush");

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByPlaceholder() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com/contact");

        Locator firstName = page.getByPlaceholder("Your first name"); // getByPlaceholder does partial matching i.e matches substring.
        firstName.fill("Piyush");

        PlaywrightAssertions.assertThat(firstName).hasValue("Piyush");

        browser.close();
        playwright.close();
    }




    /* ************************************* Testing auto-waiting of Locators ********************************** */

    /* Following test is meant to test that operations(like click, innerHTML etc) perform on locators like getByText etc add waiting.
     * Otherwise no waiting is added. Following test
     * */
    @Test
    public void testGetByTextToTestTimeout() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.getByText("Bolt Cutters").click();

        Locator mightyCraftHardware = page.getByText("MightyCraft Hardware......"); // this will not add any waiting.

        String innerText = "Cant find inner text";
        try {
            innerText = mightyCraftHardware.innerText(); // this will add waiting and wait tor find text="MightyCraft Hardware......". Default is 30 secs.
        } catch (Exception e) {
            e.printStackTrace();
            assertThat(e).isInstanceOf(TimeoutError.class);
        }
        System.out.println("inner text" + innerText);

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByTextWaitingOnInnerTextOrTextContentMethod() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        Locator boltCuttersLocator = page.getByRole(
            AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("Bolt Cutters").setLevel(5)
        );

//        String boltCutters = boltCuttersLocator.innerText();
        String boltCutters = boltCuttersLocator.textContent();

        System.out.println("The boltcuttuers text is : " + boltCutters);

        browser.close();
        playwright.close();
    }

    @Test
    public void testInnerTextOrTextContentMethodHasWaitingTime() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        Locator boltCuttersLocator = page.getByRole(
            AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("Not existing text").setLevel(5)
        );

        String boltCutters = null;
        try {
            boltCutters = boltCuttersLocator.innerText();
//            boltCutters = boltCuttersLocator.textContent();

        } catch (Exception e) {
            e.printStackTrace();
            assertThat(e).isInstanceOf(TimeoutError.class);
        }

        System.out.println("The boltcuttuers text is : " + boltCutters);

        browser.close();
        playwright.close();
    }




    /* ************************************* Form Elements ********************************** */
    @Test
    public void testFillForm() throws URISyntaxException {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com/contact");

        Locator firstName = page.getByLabel("First name");
        Locator lastName = page.getByLabel("Last name");
        Locator emailAddress = page.getByLabel("Email address");
        Locator subject = page.getByLabel("Subject");
        Locator message = page.getByLabel("Message *");
        Locator attachment = page.getByLabel("Attachment");

        firstName.fill("Piyush");
        lastName.fill("Kumar");
        emailAddress.fill("pi@gmail.com");
        message.fill("Hello to the world of Playwright!!");
        subject.selectOption("warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        page.setInputFiles("#attachment", fileToUpload);

        PlaywrightAssertions.assertThat(firstName).hasValue("Piyush");
        PlaywrightAssertions.assertThat(lastName).hasValue("Kumar");
        PlaywrightAssertions.assertThat(emailAddress).hasValue("pi@gmail.com");
        PlaywrightAssertions.assertThat(subject).hasValue("warranty");
        PlaywrightAssertions.assertThat(message).hasValue("Hello to the world of Playwright!!");

        String attachmentInputValue = attachment.inputValue();

        System.out.println("Input value of attachment is : " + attachmentInputValue);

        assertThat(attachmentInputValue).endsWith("sample-data.txt");

        browser.close();
        playwright.close();
    }




    /* ********************************** Filtering and Nested Locators ***************************** */
    @Test
    public void testLocatorFiltering() {

        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.waitForTimeout(2000); // either we add this wait or below line which adds implicit wait.
//        page.getByTestId("product-name").first().innerText(); // or this line which adds implicit wait since when page is navigated it takes some time to load the products.

        List<String> hammers = page.getByTestId("product-name")
            .filter(new Locator.FilterOptions().setHasText("Hammer"))
            .allInnerTexts();

        System.out.println("Hammers are " + hammers);


        List<String> hammersByCardCss = page.locator(".card")
            .filter(new Locator.FilterOptions().setHasText("Hammer"))
            .allTextContents(); // allInnerTests and allTextContents are same.

        System.out.println("HammersByCardCss are " + hammersByCardCss);

        browser.close();
        playwright.close();
    }

    @Test
    public void testNestedLocators1() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main menu"))
            .getByRole(AriaRole.MENUITEM, new Locator.GetByRoleOptions().setName("Home"))
            .click();

        browser.close();
        playwright.close();
    }

    @Test
    public void testNestedLocators2() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main menu"))
            .getByText("Home")
            .click();

        browser.close();
        playwright.close();
    }

    @Test
    public void testNestedLocatorsWithFiltering() {

        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com");

        page.waitForTimeout(2000); // either we add this wait or below line which adds implicit wait.
//        page.getByTestId("product-name").first().innerText(); // or this line which adds implicit wait since when page is navigated it takes some time to load the products.

        List<String> outOfStock = page.locator(".card")
            .filter(new Locator.FilterOptions().setHas(page.getByText("Out of stock")))
            .getByTestId("product-name")
            .allInnerTexts();

        System.out.println("Out of stock" + outOfStock);

        browser.close();
        playwright.close();
    }




    /* ***************************************** Locating Nth element ************************************ */
    /**
     * page.locator(".card").first().click()
     * page.locator(".card").last().click();
     * page.locator(".card").nth(2).click();
     */
    @Test
    public void testGetNthElement() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        page.navigate("https://practicesoftwaretesting.com");

        Locator first = page.locator(".card").first();
        first.click();

//        page.waitForTimeout(2000);
        PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools")).isVisible();

        /* NOTE: if we don't add above wait statement or assertion isVisible(which adds implicitly 5 secs), then below fist.testContent() will not return anything.
         * But why, since this locator method is having auto waiting with timeout of 30 secs. Reason is that page.locator(..) method is on .card css and first.textContent method will
         * just wait for .card css to be loaded with its element, it will not wait for all the other elements within it to be loaded. So, when it tried fetching
         * text it didnt find anything as text element was not loaded. If we had page.locator on text itself and then call textContent it would have worked. */
        String firstCard = first.textContent();
        System.out.println("First card is : " + firstCard);

        browser.close();
        playwright.close();

    }




    /******************************************* CSS Selectors ******************************************* */
    @Test
    public void testGetByIdCssSelector() {


        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com/contact");

        Locator firstNameLocator = page.locator("#first_name");
        firstNameLocator.fill("Piyush");

        PlaywrightAssertions.assertThat(firstNameLocator).hasValue("Piyush");

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByClassCssSelector() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com/contact");

        Locator firstNameLocator = page.locator("#first_name");
        firstNameLocator.fill("Piyush");

        page.locator(".btnSubmit").click(); // get by class selector

        /* Since allTextContents() method does not add auto waiting but still we will be able to get some alert msgs after clicking on submit button.
         * Why ? Becoz here we are checking for class which appears as the client side validation which takes nano seconds of time. There is no backend
         * operation happening which could take more time. However, it may happen you wont get all the alert msgs, the result will not be deterministic. So,
         * In this case, its better to add explicit waiting. */
        page.waitForTimeout(2000);
        List<String> alertMsgs = page.locator(".alert").allTextContents();

        assertThat(alertMsgs).isNotEmpty();

        System.out.println("The alert msgs are " + alertMsgs);

        browser.close();
        playwright.close();
    }

    @Test
    public void testGetByAttributeCssSelector() {


        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com/contact");

        Locator lastNameLocator = page.locator("[placeholder='Your last name *']");
        lastNameLocator.fill("Kumar");

        PlaywrightAssertions.assertThat(lastNameLocator).hasValue("Kumar");
    }

    @Test
    public void testGetByElementAttributeCssSelector() {


        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://practicesoftwaretesting.com/contact");

        Locator lastNameLocator = page.locator("input[placeholder='Your last name *']");
        lastNameLocator.fill("Kumar");

        PlaywrightAssertions.assertThat(lastNameLocator).hasValue("Kumar");
    }
}
