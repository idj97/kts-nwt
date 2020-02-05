package com.mbooking.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class LoginTest {

	public static final String INVALID_EMAIL_FORMAT_ERROR_MSG = "Email is not valid";
	public static final String EMAIL_REQUIRED_ERROR_MSG = "This field can't be empty!";
	public static final String PASSWORD_REQUIRED_ERROR_MSG = "This field can't be empty!";

	private WebDriver browser;

	HomePage homePage;

	LoginPage loginPage;

	private static final String baseUrl = "http://localhost:4200";

	@Before
	public void setupSelenium() {

		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		browser = new ChromeDriver();

		browser.manage().window().maximize();

		browser.navigate().to(baseUrl);
		// browser.get(baseUrl);
		homePage = PageFactory.initElements(browser, HomePage.class);
		loginPage = PageFactory.initElements(browser, LoginPage.class);

	}

	private void goToLoginPage() {
		homePage.ensureLoginButtonIsDisplayed();
		homePage.getLoginButton().click();

		// navigating to login page
		// browser.navigate().to(baseUrl + "/login");
		// (new WebDriverWait(browser,
		// 8000)).until(ExpectedConditions.urlContains("/login"));

		assertEquals(baseUrl + "/login", browser.getCurrentUrl());

	}

	
	
	//ispraviti 
	@Test
	public void testLoginNoneData() {
		
		this.goToLoginPage();
		loginPage.setUsernameInput("");
		loginPage.setPasswordInput("");
		loginPage.setUsernameInput("");
		assertTrue(loginPage.getEmailErrorMessage().isDisplayed());
		assertTrue(loginPage.getPasswordErrorMessage().isDisplayed());
		String errorMessage = loginPage.getEmailErrorMessage().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);

		String errorMessage1 = loginPage.getPasswordErrorMessage().getText();
		assertEquals(PASSWORD_REQUIRED_ERROR_MSG, errorMessage1);

        assertFalse(loginPage.getLoginButton().isEnabled());
	}

	@Test
	public void testLoginEmptyPasswInput() {
		
		this.goToLoginPage();
		
		loginPage.setPasswordInput("");
		loginPage.setUsernameInput("a@mail.com");

		loginPage.getLoginButton().isEnabled();
		assertTrue(loginPage.getPasswordErrorMessage().isDisplayed());
		
		String errorMessage = loginPage.getPasswordErrorMessage().getText();
		assertEquals(PASSWORD_REQUIRED_ERROR_MSG, errorMessage);
	}

	@Test
	public void testLoginEmptyEmailInput() {
		
		this.goToLoginPage();

		loginPage.setUsernameInput("");
		loginPage.setPasswordInput("1234567");

		loginPage.getLoginButton().isEnabled();

		assertTrue(loginPage.getEmailErrorMessage().isDisplayed());
		
		String errorMessage = loginPage.getEmailErrorMessage().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);
	}

	@Test
	public void testLoginInvalidEmailInput() {
		
		this.goToLoginPage();

		loginPage.setUsernameInput("a");
		loginPage.setPasswordInput("1234567");

		loginPage.getLoginButton().isEnabled();

		assertTrue(loginPage.getEmailErrorMessageValid().isDisplayed());
		
		String errorMessage = loginPage.getEmailErrorMessageValid().getText();
		assertEquals(INVALID_EMAIL_FORMAT_ERROR_MSG, errorMessage);
	}
	
	
	 @Test
	    public void testLoginBadCredentials() {
		
		 	this.goToLoginPage();
	        
	    	loginPage.setUsernameInput("ja@example.com");
	    	loginPage.setPasswordInput("adminskii");
	       
	    	assertTrue(loginPage.getLoginButton().isEnabled());
	    	
	    	loginPage.ensureIsDisplayed();
	        loginPage.getLoginButton().click();
	    	
	        assertEquals(baseUrl + "/login", browser.getCurrentUrl());
	    }


		@Test
		//@Transactional
		//@Rollback(true)
	    public void testLoginWalidData() {
	        
	    	this.goToLoginPage();
	        
	    	loginPage.setUsernameInput("sysadmin@example.com");
	    	loginPage.setPasswordInput("admin");
	    	
	    	loginPage.ensureIsDisplayed();
	    	loginPage.getLoginButton().click();
	    	
	    	assertTrue(loginPage.getLoginButton().isEnabled());
	        
	    	loginPage.ensureIsDisplayed();
	        loginPage.getLoginButton().click();

	        (new WebDriverWait(browser, 8000))
	        .until(ExpectedConditions.urlContains("/home"));
	    	assertEquals(baseUrl + "/home", browser.getCurrentUrl());
  }

	@After
	public void shutDown() {
		browser.quit();
	}
}
