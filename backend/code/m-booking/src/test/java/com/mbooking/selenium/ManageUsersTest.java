package com.mbooking.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ManageUsersTest {

    private WebDriver browser;
    private ManageUsersPage manageUsersPage;
    private LoginPage loginPage;
    private Toaster toaster;
    private static final String baseUrl = "http://localhost:4200/manage-users";

    @Before
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver");
        browser = new FirefoxDriver();
        browser.manage().window().maximize();

        manageUsersPage = PageFactory.initElements(browser, ManageUsersPage.class);
        loginPage = PageFactory.initElements(browser, LoginPage.class);
        toaster = PageFactory.initElements(browser, Toaster.class);
    }

    @Test
    public void givenUnloggedUser_whenNavigatingToManageUsers_expectNotAllowed() {
        browser.navigate().to(baseUrl);
        manageUsersPage.ensureIsDisplayed();

        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.visibilityOf(toaster.getToasterMessage()));

        Assert.assertTrue(toaster.getToaster().isDisplayed());
        Assert.assertEquals("You are not allowed to access that page", toaster.getToasterMessage().getText());
    }

    @Test
    public void givenLoggedUser_whenNavigatingToManageUsers_expectPageLoaded() {
        loginAsAdmin();
        browser.navigate().to(baseUrl);
        manageUsersPage.ensureIsDisplayed();

        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.visibilityOf(manageUsersPage.getSearchButton()));

        Assert.assertTrue(manageUsersPage.getSearchButton().isDisplayed());
        Assert.assertTrue(manageUsersPage.getSearchEmail().isDisplayed());
        Assert.assertTrue(manageUsersPage.getSearchFirstname().isDisplayed());
        Assert.assertTrue(manageUsersPage.getSearchLastname().isDisplayed());
        Assert.assertTrue(manageUsersPage.getBanButton().isDisplayed());
    }

    @Test
    public void givenEmptySearchParams_whenSearchingManageUsers_expectNumOfUsersBiggerThenZero() {
        loginAsAdmin();
        browser.navigate().to(baseUrl);
        manageUsersPage.ensureIsDisplayed();

        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.elementToBeClickable(manageUsersPage.getBanButton()));

        manageUsersPage.getSearchButton().click();
        int size = manageUsersPage.getUsersTbody().findElements(By.tagName("tr")).size();
        Assert.assertTrue(size > 0);
    }

    @Test
    public void givenUniqueSearchParams_whenSearchingManageUsers_expectOneUser() {
        loginAsAdmin();
        browser.navigate().to(baseUrl);
        manageUsersPage.ensureIsDisplayed();

        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.elementToBeClickable(manageUsersPage.getBanButton()));

        manageUsersPage.getSearchEmail().sendKeys("3");
        manageUsersPage.getSearchButton().click();
        int size = manageUsersPage.getUsersTbody().findElements(By.tagName("tr")).size();
        Assert.assertTrue(size == 1);
    }

    @Test
    public void givenUnbannedUser_whenClickBanUser_expectUserIsBanned() {
        loginAsAdmin();
        browser.navigate().to(baseUrl);
        manageUsersPage.ensureIsDisplayed();

        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.elementToBeClickable(manageUsersPage.getBanButton()));

        manageUsersPage.getSearchEmail().sendKeys("3");
        manageUsersPage.getSearchButton().click();
        WebElement trOne = manageUsersPage.getUsersTbody().findElements(By.tagName("tr")).get(0);
        WebElement banBtn = trOne.findElements(By.id("banButton")).get(0);
        banBtn.click();

        trOne = manageUsersPage.getUsersTbody().findElements(By.tagName("tr")).get(0);
        boolean unbanButtonClickable = trOne.findElements(By.id("unbanButton")).get(0).isDisplayed();
        Assert.assertTrue(unbanButtonClickable);
    }

    @Test
    public void givenBannedUser_whenClickUnbanUser_expectUserIsUnbanned() {
        loginAsAdmin();
        browser.navigate().to(baseUrl);
        manageUsersPage.ensureIsDisplayed();

        (new WebDriverWait(browser, 3000))
                .until(ExpectedConditions.elementToBeClickable(manageUsersPage.getBanButton()));

        manageUsersPage.getSearchEmail().sendKeys("3");
        manageUsersPage.getSearchButton().click();
        WebElement trOne = manageUsersPage.getUsersTbody().findElements(By.tagName("tr")).get(0);
        WebElement banBtn = trOne.findElements(By.id("unbanButton")).get(0);
        banBtn.click();

        trOne = manageUsersPage.getUsersTbody().findElements(By.tagName("tr")).get(0);
        boolean unbanButtonClickable = trOne.findElements(By.id("banButton")).get(0).isDisplayed();
        Assert.assertTrue(unbanButtonClickable);
    }

    @After
    public void shutDown() {
        browser.quit();
    }

    private void loginAsAdmin() {
        loginPage.login("sysadmin@example.com", "admin");
        browser.navigate().to(baseUrl);
        manageUsersPage.ensureIsDisplayed();
    }
}
