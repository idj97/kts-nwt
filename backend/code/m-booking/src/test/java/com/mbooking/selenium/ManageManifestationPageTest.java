package com.mbooking.selenium;

import com.mbooking.utility.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ManageManifestationPageTest {

    private WebDriver browser;
    private ManageManifestationPage manageManifestPage;
    private LoginPage loginPage;
    private Toaster toaster;

    private static final String baseUrl = "http://localhost:4200/manage-manifestation";

    @Before
    public void setUp() {

        // init browser
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        // init web page and it's elements
        manageManifestPage = PageFactory.initElements(browser, ManageManifestationPage.class);
        loginPage = PageFactory.initElements(browser, LoginPage.class);
        toaster = PageFactory.initElements(browser, Toaster.class);

        // navigate to test page
        browser.navigate().to(baseUrl);
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

        // displaying 2 additional fields by clicking allow reservations checkbox
        this.clickAllowReservationsCheckbox();

        errorMessages = browser.findElements(By.className("error-message"));
        assertEquals(7, errorMessages.size());

    }

    @Test
    public void givenSelectedDate_whenAddDayClicked_displayAddedDay() {

        selectActiveDate();
        assertTrue(manageManifestPage.getManifestDaysHolder().isDisplayed());

        List<WebElement> addedDays = manageManifestPage
                .getManifestDaysHolder()
                .findElements(By.tagName("li"));

        assertEquals(1, addedDays.size());

    }

    @Test
    public void givenSelectedDate_whenRemoveDateClicked_removeDateFromDisplay() {

        // given
        selectActiveDate();
        assertTrue(manageManifestPage.getManifestDaysHolder().isDisplayed());

        // wait for the remove date button to appear
        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.elementToBeClickable(manageManifestPage.getRemoveDayIcon()));

        // when
        manageManifestPage.getRemoveDayIcon().click();
        List<WebElement> addedDays = manageManifestPage
                .getManifestDaysHolder()
                .findElements(By.tagName("li"));

        //then
        assertEquals(0, addedDays.size());

    }

    @Test
    public void givenSelectedDate_whenAddingSameDate_NotifyUser() {
        selectActiveDate();
        assertTrue(manageManifestPage.getManifestDaysHolder().isDisplayed());
        manageManifestPage.getAddDayButton().click(); //adding the same date again

        // wait for the toaster message to appear
        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.visibilityOf(toaster.getToasterMessage()));

        assertTrue(toaster.getToaster().isDisplayed());
        assertEquals("The day you selected has already been added",
                toaster.getToasterMessage().getText());

    }

    @Test
    public void givenReservableUntilBeforeStart_whenSubmitting_NotifyUser() {

        loginAsAdmin();

        fillOutManifestationForm(true);
        this.manageManifestPage.getSubmitButton().click();

        // wait for the toaster message to appear
        (new WebDriverWait(browser, 8000))
                .until(ExpectedConditions.visibilityOf(toaster.getToasterMessage()));

        assertEquals(Constants.INVALID_RESERV_DAY_MSG, toaster.getToasterMessage().getText());

    }

    @After
    public void shutDown() {
        browser.quit();
    }


    /*******************************
     * Auxiliary methods used in tests
     *******************************/

    private void loginAsAdmin() {
        loginPage.login("testadmin@example.com", "admin");
        browser.navigate().to(baseUrl);
        manageManifestPage.ensureIsDisplayed();
    }

    private void selectActiveDate() {

        // point to next month
        browser.findElement(
                By.cssSelector(".right-icon"))
                .click();

        // select active date
        browser.findElement(
                By.cssSelector(".col.dl-abdtp-date-button.dl-abdtp-active"))
                .click();

        //select time
        browser.findElement(By.cssSelector(".col.dl-abdtp-date-button.dl-abdtp-hour.dl-abdtp-active")).click();

        manageManifestPage.getAddDayButton().click();

    }

    private void clickAllowReservationsCheckbox() {

        Actions actions = new Actions(browser);
        actions.moveToElement(manageManifestPage.getReservationsAllowedCheckBox())
                .click().build().perform();

        // wait for additional fields to pop up after
        (new WebDriverWait(browser, 2000))
                .until(ExpectedConditions.elementToBeClickable(manageManifestPage.getMaxReservationsInput()));

    }

    private void fillOutManifestationForm(boolean allowReservations) {

        manageManifestPage.ensureOptionsLoaded();

        this.manageManifestPage.getNameInput().sendKeys("selenium manifestation");
        this.manageManifestPage.getDescriptionInput().sendKeys("selenium description");
        this.selectActiveDate();

        this.manageManifestPage.getTypeSelect().click();
        this.manageManifestPage.getTypeSelect().findElement(By.tagName("option")).click();

        this.manageManifestPage.getLocationSelect().click();
        this.manageManifestPage.getLocationSelect().findElement(By.tagName("option")).click();

        if(allowReservations) {

            this.clickAllowReservationsCheckbox();

            manageManifestPage.getMaxReservationsInput().sendKeys("5");

            // fill out date input
            manageManifestPage.getReservableUntilInput().sendKeys("05");
            manageManifestPage.getReservableUntilInput().sendKeys("Aug");
            manageManifestPage.getReservableUntilInput().sendKeys(Keys.TAB);
            manageManifestPage.getReservableUntilInput().sendKeys("2520");
        }

    }


}
