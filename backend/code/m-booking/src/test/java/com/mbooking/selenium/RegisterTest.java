package com.mbooking.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterTest {
	
  
    public static final String INVALID_EMAIL_FORMAT_ERROR_MSG = "Email is not valid";
    public static final String EMAIL_REQUIRED_ERROR_MSG = "Email is required";
    public static final String INVALID_PASSWORD_ERROR_MSG = "Password should contain 7 characters";
    public static final String PASSWORD_REQUIRED_ERROR_MSG = "Password is required";
    public static final String FIRSTNAME_REQUIRED_ERROR_MSG = "First name is required";
    public static final String LASTNAME_REQUIRED_ERROR_MSG = "Lastname is required";
	
	
	
	
	
	
	
	private WebDriver browser;
	
	HomePage homePage;
	
	LoginPage loginPage;
	RegisterPage registerPage;
	
	
	 private static final String baseUrl = "http://localhost:4200";
	
	@Before
	public void setupSelenium() {
		
		System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
		browser = new ChromeDriver();
		
		browser.manage().window().maximize(); 
	
		browser.navigate().to(baseUrl);
	
		homePage = PageFactory.initElements(browser, HomePage.class); 
		loginPage = PageFactory.initElements(browser, LoginPage.class);
		registerPage = PageFactory.initElements(browser, RegisterPage.class);
	}
	
	
	@Test 
    public void registerTest() {
		homePage.ensureRegisterButtonIsDisplayed();
		homePage.getRegisterButton().click();
		
		assertEquals(baseUrl + "/register", browser.getCurrentUrl());
		registerPage.setEmailInpit("jaric@mail.com");
		registerPage.setFirstnameInpit("zivadin");
		registerPage.setLastnameInpit("jaric");
		
		registerPage.setPasswordInput("jariczivadin");
		
		registerPage.ensureRegisterButtonIsDisplayed();
		registerPage.getRegisterButton().click();
		
		browser.get(baseUrl + "/login");
    	assertEquals(baseUrl + "/login", browser.getCurrentUrl());
		
		
		
	}
	
	 public void testRegisterEmptyFields() {
			homePage.ensureRegisterButtonIsDisplayed();
			homePage.getRegisterButton().click();
			
			assertEquals(baseUrl + "/register", browser.getCurrentUrl());
			registerPage.setEmailInpit("");
			registerPage.setFirstnameInpit("");
			registerPage.setLastnameInpit("");
			
			registerPage.setPasswordInput("");
			
			registerPage.ensureRegisterButtonIsDisplayed();
			registerPage.getRegisterButton().click();
			
			
			assertTrue(registerPage.getEmailErrorReq().isDisplayed());
			assertTrue(registerPage.getFirstnameError().isDisplayed());
			assertTrue(registerPage.getLastnameError().isDisplayed());
			assertTrue(registerPage.getPasswErrorReq().isDisplayed());
			

			String errorMessage = registerPage.getEmailErrorReq().getText();
			assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);
			
			String errorMessage2 = registerPage.getFirstnameError().getText();
			assertEquals(FIRSTNAME_REQUIRED_ERROR_MSG, errorMessage);
			
			String errorMessage3 = registerPage.getLastnameError().getText();
			assertEquals(LASTNAME_REQUIRED_ERROR_MSG, errorMessage);
			
			String errorMessage1 = registerPage.getPasswErrorReq().getText();
			assertEquals(PASSWORD_REQUIRED_ERROR_MSG, errorMessage);
			
			
		}
	
	@Test
    public void testRegisterInvalidEmail() {
		homePage.ensureRegisterButtonIsDisplayed();
		homePage.getRegisterButton().click();
		
		assertEquals(baseUrl + "/register", browser.getCurrentUrl());
		registerPage.setEmailInpit("jaric");
		registerPage.setFirstnameInpit("zivadin");
		registerPage.setLastnameInpit("jaric");
		
		registerPage.setPasswordInput("jariczivadin");
		
		registerPage.ensureRegisterButtonIsDisplayed();
		registerPage.getRegisterButton().click();
		
		//assertFalse(registerPage.getRegisterButton().isEnabled());
		assertTrue(registerPage.getEmailErrorValid().isDisplayed());
		
		String errorMessage = registerPage.getEmailErrorValid().getText();
		assertEquals(INVALID_EMAIL_FORMAT_ERROR_MSG, errorMessage);
	}
	
	
	
	@Test
    public void testRegisterInvalidEmailnoneInput() {
		homePage.ensureRegisterButtonIsDisplayed();
		homePage.getRegisterButton().click();
		
		assertEquals(baseUrl + "/register", browser.getCurrentUrl());
		registerPage.setEmailInpit("");
		registerPage.setFirstnameInpit("zivadin");
		registerPage.setLastnameInpit("jaric");
		
		registerPage.setPasswordInput("jariczivadin");
		
		registerPage.ensureRegisterButtonIsDisplayed();
		registerPage.getRegisterButton().click();
		
		assertTrue(registerPage.getEmailErrorReq().isDisplayed());
		
		String errorMessage = registerPage.getEmailErrorReq().getText();
		assertEquals(EMAIL_REQUIRED_ERROR_MSG, errorMessage);
	}
	
	
	@Test
    public void testRegisterInvalidPassword() {
		homePage.ensureRegisterButtonIsDisplayed();
		homePage.getRegisterButton().click();
		
		assertEquals(baseUrl + "/register", browser.getCurrentUrl());
		registerPage.setEmailInpit("jaric@mail.com");
		registerPage.setFirstnameInpit("zivadin");
		registerPage.setLastnameInpit("jaric");
		
		registerPage.setPasswordInput("ja");
		
		registerPage.ensureRegisterButtonIsDisplayed();
		registerPage.getRegisterButton().click();
		
		assertTrue(registerPage.getPasswErrorChar().isDisplayed());
		String errorMessage = registerPage.getPasswErrorChar().getText();
		assertEquals(INVALID_PASSWORD_ERROR_MSG, errorMessage);
	}
	
	@Test
    public void testRegisterInvalidPasswordNoneInput() {
		homePage.ensureRegisterButtonIsDisplayed();
		homePage.getRegisterButton().click();
		
		assertEquals(baseUrl + "/register", browser.getCurrentUrl());
		registerPage.setEmailInpit("jaric@mail.com");
		registerPage.setFirstnameInpit("zivadin");
		registerPage.setLastnameInpit("jaric");
		
		registerPage.setPasswordInput("");
		
		registerPage.ensureRegisterButtonIsDisplayed();
		registerPage.getRegisterButton().click();
		
		assertTrue(registerPage.getPasswErrorReq().isDisplayed());
		String errorMessage = registerPage.getPasswErrorReq().getText();
		assertEquals(PASSWORD_REQUIRED_ERROR_MSG, errorMessage);
	}
	
	
	
	

    @After
    public void shutDown() {
        browser.quit();
    }
}
