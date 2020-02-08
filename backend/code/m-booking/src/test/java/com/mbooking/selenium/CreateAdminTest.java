package com.mbooking.selenium;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.catalina.valves.CrawlerSessionManagerValve;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateAdminTest {

	public static final String INVALID_EMAIL_FORMAT_ERROR_MSG = "Email is not valid";
	public static final String EMAIL_REQUIRED_ERROR_MSG = "Email is required";
	public static final String INVALID_PASSWORD_MIN_ERROR_MSG = "Password should contain 7 characters";
	public static final String INVALID_PASSWORD_MAX_ERROR_MSG = "Password should contain 30 characters";
	public static final String PASSWORD_REQUIRED_ERROR_MSG = "Password is required";
	public static final String FIRSTNAME_REQUIRED_ERROR_MSG = "First name is required";
	public static final String LASTNAME_REQUIRED_ERROR_MSG = "Lastname is required";

	private WebDriver browser;

	HomePage homePage;
	CreateAdminPage createAdminPage;
	LoginPage loginPage;
	RegisterPage registerPage;

	private static final String baseUrl = "http://localhost:4200";

	@Before
	public void setupSelenium() {

		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		browser = new ChromeDriver();

		browser.manage().window().maximize();

		browser.navigate().to(baseUrl);

		homePage = PageFactory.initElements(browser, HomePage.class);
		loginPage = PageFactory.initElements(browser, LoginPage.class);
		createAdminPage = PageFactory.initElements(browser, CreateAdminPage.class);

	}

	@After
	public void shutDown() {
		browser.quit();
	}

	private void goToCreateAdminPage() {

	}

	@Test
	public void testCreateAdminEmptyFields() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();
		createAdminPage.setEmailInpit("");
		createAdminPage.setFirstnameInpit("");
		createAdminPage.setLastnameInpit("");
		createAdminPage.setPasswordInput("");
		// assertFalse(createAdminPage.getCreateButton().isEnabled());

		assertTrue(createAdminPage.getEmailErrorReq().isDisplayed());
		assertTrue(createAdminPage.getFirstnameError().isDisplayed());
		assertTrue(createAdminPage.getLastnameError().isDisplayed());
		assertTrue(createAdminPage.getPasswErrorReq().isDisplayed());

		String errorMessage = createAdminPage.getEmailErrorReq().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);

		String errorMessage1 = createAdminPage.getFirstnameError().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage1);

		String errorMessage2 = createAdminPage.getLastnameError().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage2);

		String errorMessage3 = createAdminPage.getPasswErrorReq().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage3);

	}

	@Test
	public void testCreateAdminInvalidEmail() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();

		createAdminPage.setEmailInpit("dda");
		// assertFalse(createAdminPage.getCreateButton().isEnabled());
		WebElement erMsg = (new WebDriverWait(browser, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("email-error-v")));

		assertTrue(createAdminPage.getEmailErrorValid().isDisplayed());
		String errorMessage = createAdminPage.getEmailErrorValid().getText();
		assertEquals(INVALID_EMAIL_FORMAT_ERROR_MSG, errorMessage);

	}

	@Test
	public void testCreateAdminInvalidEmailEmptyField() {

		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();

		createAdminPage.setEmailInpit("");
		// assertFalse(createAdminPage.getCreateButton().isEnabled());

		assertTrue(createAdminPage.getEmailErrorReq().isDisplayed());
		String errorMessage = createAdminPage.getEmailErrorReq().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);

	}

	@Test
	public void testCreateAdminFirstnameEmptyField() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();

		createAdminPage.setFirstnameInpit("");
		// assertFalse(createAdminPage.getCreateButton().isEnabled());

		assertTrue(createAdminPage.getFirstnameError().isDisplayed());
		String errorMessage = createAdminPage.getFirstnameError().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);

	}

	@Test
	public void testCreateAdminLastnameEmptyField() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();

		createAdminPage.setLastnameInpit("");
		// assertFalse(createAdminPage.getCreateButton().isEnabled());

		assertTrue(createAdminPage.getLastnameError().isDisplayed());

		String errorMessage = createAdminPage.getLastnameError().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);
	}

	@Test
	public void testCreateAdminPasswordEmptyField() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();

		createAdminPage.setPasswordInput("");
		;
		// assertFalse(createAdminPage.getCreateButton().isEnabled());

		assertTrue(createAdminPage.getPasswErrorReq().isDisplayed());

		String errorMessage = createAdminPage.getPasswErrorReq().getText();
		assertEquals(PASSWORD_REQUIRED_ERROR_MSG, errorMessage);
	}

	@Test
	public void testCreateAdminInvalidPasswordMinChar() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();

		createAdminPage.setPasswordInput("aa");
		;
		// assertFalse(createAdminPage.getCreateButton().isEnabled());

		assertTrue(createAdminPage.getPasswErrorCharMin().isDisplayed());

		String errorMessage = createAdminPage.getPasswErrorCharMin().getText();
		assertEquals(INVALID_PASSWORD_MIN_ERROR_MSG, errorMessage);
	}

	@Test
	public void testCreateAdminInvalidPasswordMaxChar() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();

		createAdminPage.setPasswordInput("jaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		;
		// assertFalse(createAdminPage.getCreateButton().isEnabled());

		assertTrue(createAdminPage.getPasswErrorCharMax().isDisplayed());

		String errorMessage = createAdminPage.getPasswErrorCharMax().getText();
		assertEquals(INVALID_PASSWORD_MAX_ERROR_MSG, errorMessage);
	}

	@Test
	public void testCreateAdminValidCredential() {
		createAdminPage.ensureCreateButtonIsDisplayed();
		createAdminPage.getCreateButton().click();
		createAdminPage.setEmailInpit("ana@mail.com");
		createAdminPage.setFirstnameInpit("ana");
		createAdminPage.setLastnameInpit("ivanovic");
		createAdminPage.setPasswordInput("ivanovic123");
		assertTrue(createAdminPage.getCreateButton().isEnabled());
		// assertEquals(, );

	}
}
