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


    /* ****************************************** CSS Selectors ******************************************* */
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

        browser.close();
        playwright.close();
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

        browser.close();
        playwright.close();
    }

    /* =========== Multiple Element Selector ============= */
    @Test
    public void testMultipleElementCssSelector() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.orangehrm.com/en/contact-sales");

        Locator countLocator = page.locator("select#Form_getForm_Country option");
        int count = countLocator.count();

        System.out.println("The count is : " + count);

        for (int i = 0; i < count; i++) {
            String country = countLocator.nth(i).textContent();
            System.out.println(country);
        }

//        OR

        System.out.println("========== All countries are ============");
        List<String> countries = countLocator.allTextContents();
        countries.forEach(System.out::println);

        browser.close();
        playwright.close();
    }

    /* =========== Text Selector ============= */
    @Test
    public void testTextCssSelector1() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.orangehrm.com/en/30-day-free-trial");

        String contactSales1 = page.locator("text='Contact Sales'").first().textContent();
        String contactSales2 = page.locator("'Contact Sales'").first().textContent(); // we can also use without using text=

        System.out.println("The text content1 is : " + contactSales1);
        System.out.println("The text content2 is : " + contactSales2);

        browser.close();
        playwright.close();
    }

    @Test
    public void testTextCssSelector2() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://demo-opencart.com/index.php?route=account/login&language=en-gb");

        String newCustomerTxt1 = page.locator("h2:has-text('New Customer')").textContent();
        String newCustomerTxt2 = page.locator("div.rounded h2:has-text('New Customer')").textContent();

        System.out.println("The new customer txt1 is : " + newCustomerTxt1);
        System.out.println("The new customer txt2 is : " + newCustomerTxt2);

        browser.close();
        playwright.close();
    }

    /* =========== Visible ELement Selector ============= */
    @Test
    public void testVisibleElement() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.amazon.in");

        Locator visibleLinks = page.locator("a:visible"); // :visible is the pseudo class
        String newCustomerTxt1 = visibleLinks.first().textContent();

        Locator visibleLinksWithVisibleFilter = page.locator("a >> visible=true");  // >> visible=true is a visible filter
        String newCustomerTxt2 = visibleLinksWithVisibleFilter.first().textContent();

        System.out.println("The new customer txt1 is : " + newCustomerTxt1);
        System.out.println("The new customer txt2 is : " + newCustomerTxt2);

        int linksCount1 = visibleLinks.count();
        int linksCount2 = visibleLinksWithVisibleFilter.count();

        System.out.println("LinksCount1 : " + linksCount1);
        System.out.println("LinksCount2 : " + linksCount2);

        browser.close();
        playwright.close();
    }

    /* ================= Element that contain other elements ============= */
    @Test
    public void testElementContainingOtherElements1() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.orangehrm.com/en/30-day-free-trial");

        List<String> countries = page.locator("select#Form_getForm_Country:has(option[value='India'])").allTextContents(); // it will return select tag that contains option element which has one of the value as India.

        countries.forEach(System.out::println);

        browser.close();
        playwright.close();
    }

    @Test
    public void testElementContainingOtherElements2() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.amazon.in");

        List<String> strings = page.locator("div.navFooterLinkCol:has(a[href='https://amazon.jobs'])").allInnerTexts();

        strings.forEach(System.out::println);

        browser.close();
        playwright.close();
    }

    /* ================= Comma Seperated Selector ============= */
    @Test
    public void testCommaSeperatedCssSelector() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://academy.naveenautomationlabs.com/");

        page.locator("a:has-text('Login'), a:has-text('login')").click(); // comma means OR

        browser.close();
        playwright.close();
    }

    @Test
    public void testCommaSeperatedCssSelectorGetMultipleElements() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://academy.naveenautomationlabs.com/");

        List<String> strings = page.locator("a:has-text('Login'), a:has-text('login'), a:has-text('webinars')").allInnerTexts();
        strings.forEach(System.out::println);

        browser.close();
        playwright.close();
    }

    /* ================= Relative Selector  ============= */
    /* https://playwright.dev/java/docs/other-locators#css-matching-elements-based-on-layout */

    @Test
    public void testRelativeCssSelector() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://selectorshub.com/xpath-practice-page/");

        page.locator("input[type='checkbox']:left-of(:text('Joe.Root'))").first().click(); // selecting first since there are lots of checkboxes left of Joe.Root
        String rightTxt = page.locator("td:right-of(:text('Joe.Root'))").first().textContent();
        String aboveTxt = page.locator("a:above(:text('Joe.Root'))").first().textContent();
        String belowTxt = page.locator("a:below(:text('Joe.Root'))").first().textContent();
        String nearTxt = page.locator("td:near(:text('Joe.Root'))").first().textContent();
        String nearTxt200 = page.locator("td:near(:text('Joe.Root'), 200)").last().textContent(); // Within 200 px

        System.out.println("rightTxt : " + rightTxt);
        System.out.println("aboveTxt : " + aboveTxt);
        System.out.println("belowTxt : " + belowTxt);
        System.out.println("nearTxt : " + nearTxt);
        System.out.println("nearTxt200 : " + nearTxt200);


        browser.close();
        playwright.close();
    }

    /* ================= Nth CSS Selector  ============= */
    /* https://playwright.dev/java/docs/other-locators#n-th-element-locator */
    @Test
    public void testNthCssSelector() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.bigbasket.com/");

        String textContent = page.locator("div.px-2 li a >> nth=2").textContent();
        System.out.println("textContent : " + textContent);


        browser.close();
        playwright.close();
    }





    /* ****************************************** React Locator ******************************************* */
    @Test
    public void testReactSelector() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.netflix.com/in/");

        page.locator("_react=PressableButton[type='submit'][data-uia='nmhp-card-cta+hero_card']").click();

        browser.close();
        playwright.close();
    }





    /* ****************************************** XPath Locator ******************************************* */
    @Test
    public void testVisibleElementViaXPath() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.amazon.in");

        Locator visibleLinks = page.locator("xpath=//img >> visible = true"); // >> visible=true is a visible filter
//        Locator visibleLinks = page.locator("//img >> visible = true"); // we can also skip xpath=
        String newCustomerTxt1 = visibleLinks.first().textContent();

        System.out.println("The new customer txt1 is : " + newCustomerTxt1);

        int linksCount1 = visibleLinks.count();

        System.out.println("LinksCount1 : " + linksCount1);

        browser.close();
        playwright.close();
    }

    @Test
    public void testXPathORing() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://academy.naveenautomationlabs.com/");

        page.locator("//a[text() = 'Login'] | //a[text() = 'login']").click(); // it is same like comma seperated css selector. We can also do and using & operator.

        browser.close();
        playwright.close();
    }

    @Test
    public void testXPathORingGetMultipleElements() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://academy.naveenautomationlabs.com/");

        List<String> strings = page.locator("//a[text() = 'Webinars'] | //a[text() = 'Login']").allInnerTexts();
        strings.forEach(System.out::println);

        browser.close();
        playwright.close();
    }





    /* ****************************************** Frame Handling ******************************************* */
    @Test
    public void testFrame() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.londonfreelance.org/courses/frames/index.html");

        String frameContent1 = page.frameLocator("frame[name='main']").locator("h2").textContent();
        System.out.println("The frame content1 is : " + frameContent1);

        // We can also just use the frame name - most of the frames usually contain frame name

        String frameContent2 = page.frame("main").locator("h2").textContent();
        System.out.println("The frame content2 is : " + frameContent2);

        browser.close();
        playwright.close();
    }

    @Test
    public void testIFrame() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://www.formsite.com/templates/registration-form-templates/vehicle-registration-form");


        page.locator("img[title='Vehicle-Registration-Forms-and-Examples']").click();

        page.frameLocator("//iframe[contains(@id, 'frame-one')]")
            .locator("#RESULT_TextField-1").fill("Hello");

        browser.close();
        playwright.close();
    }





    /* ****************************************** Shadow DOM Handling ******************************************* */

    /* Following testcase is for hierarchy:  Page -> DOM -> Shadow DOM -> Element
     * In such cases when we have shadow dom, we cannot locate it directly. First we need to find the root of the shadow root and then
     * we can locate any element in the shadow root.
     *
     * If we have a case of hierarchy:  Page -> IFrame -> DOM -> Shadow DOM -> Element
     * In such cases when we have shadow dom within iframe, we cannot locate it directly. First we need to find the iframe and then
     * we can locate any element in the shadow root.
     * Ex - page.frameLocator("#id_of_iframe_or_any_css_selector").locator("root_of_shadow ccs_locator_for_element").fill("something");
     *
     *
     * NOTE : Make sure that shadow root is open. If it is closed, then it cannot be located thereby cannot be automated.
     * */
    @Test
    public void testShadowDom() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://books-pwakit.appspot.com");

        page.locator("book-app[apptitle='BOOKS'] #input").fill("harry potter"); // here book-app[apptitle='BOOKS'] locates root of shadow root and #input is within shadow root.

        browser.close();
        playwright.close();
    }

}
