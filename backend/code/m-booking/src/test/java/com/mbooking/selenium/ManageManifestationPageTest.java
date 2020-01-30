package com.mbooking.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ManageManifestationPageTest {

    private WebDriver browser;
    private ManageManifestationPage manageManifestPage;

    private static final String baseUrl = "http://localhost:4200/manage-manifestation";

    @Before
    public void setUp() {

        // init browser
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        // init web page and it's elements
        browser.navigate().to(baseUrl);
        manageManifestPage = PageFactory.initElements(browser, ManageManifestationPage.class);

        manageManifestPage.ensureIsDisplayed();
    }


    @Test
    public void givenEmptyData_whenSubmitting_displayErrorMessages() {

        manageManifestPage.getSubmitButton().click();

        List<WebElement> errorMessages = browser.findElements(By.className("error-message"));

        for(WebElement errMsg: errorMessages) {
            assertTrue(errMsg.isDisplayed());
        }

        assertEquals(5, errorMessages.size());

        // displaying 2 additional fields when checkbox is clicked
        Actions actions = new Actions(browser);
        actions.moveToElement(manageManifestPage.getReservationsAllowedCheckBox()).click().build().perform();

        errorMessages = browser.findElements(By.className("error-message"));
        assertEquals(7, errorMessages.size());

    }


    @Test
    public void givenSelectedDate_whenAddDayClicked_displayAddedDay() {

        selectTodaysDate();
        assertTrue(manageManifestPage.getManifestDaysHolder().isDisplayed());

    }


    private void selectTodaysDate() {

        // select current date
        browser.findElement(
                By.cssSelector(".col.dl-abdtp-active.dl-abdtp-selected"))
                .click();

        //select time
        browser.findElement(By.cssSelector(".dl-abdtp-hour")).click();

        manageManifestPage.getAddDayButton().click();

    }

    @After
    public void shutDown() {
        browser.quit();
    }

}
