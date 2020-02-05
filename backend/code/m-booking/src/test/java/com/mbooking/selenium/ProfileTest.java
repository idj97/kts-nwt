package com.mbooking.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Test;

public class ProfileTest {
	
	
	public static final String NEW_PASSWORD_REQUIRED_ERROR_MSG = "New password is required";
	public static final String OLD_PASSWORD_REQUIRED_ERROR_MSG = "Old password is required";
    public static final String INVALID_PASSWORD_ERROR_MSG = "Password should contain 7 characters";
    public static final String FIRSTNAME_REQUIRED_ERROR_MSG = "Firstname is required";
    public static final String LASTNAME_REQUIRED_ERROR_MSG = "Lastname is required";
    
	private WebDriver browser;
	
	HomePage homePage;
	
	LoginPage loginPage;
	
	ProfilePage profilePage;
	
	 private static final String baseUrl = "http://localhost:4200";
	
	@Before
	public void setupSelenium() {
		
		System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
		browser = new ChromeDriver();
		
		browser.manage().window().maximize(); 
	
		browser.navigate().to(baseUrl);
	
		homePage = PageFactory.initElements(browser, HomePage.class); 
		loginPage = PageFactory.initElements(browser, LoginPage.class);
		profilePage = PageFactory.initElements(browser, ProfilePage.class);
	}
	
	
	

    @After
    public void shutDown() {
        browser.quit();
    }
    
    
    
    private void gotoProfilePage() {
    	homePage.ensureLoginButtonIsDisplayed();
    	homePage.getLoginButton().click();

    	assertEquals(baseUrl + "/login", browser.getCurrentUrl());

    	loginPage.setUsernameInput("ktsnwt.customer@gmail.com");
    	loginPage.setPasswordInput("user");
    	loginPage.ensureIsDisplayed();
    	loginPage.getLoginButton().click();
    	(new WebDriverWait(browser, 8000))
        .until(ExpectedConditions.urlContains("/home"));
    	assertEquals(baseUrl + "/home", browser.getCurrentUrl());


    	homePage.ensureProfileButtonIsDisplayed();
    	homePage.getProfileButton().click();

    	assertEquals(baseUrl+"/profile", browser.getCurrentUrl());
    	
    } 
    
    @Test 
    public void userEditProfilePasswordTest() {
    	
    	this.gotoProfilePage();
    	
    	profilePage.setOldPasswordInput("user");
    	profilePage.setNewPasswordInput("user1");

    	profilePage.ensureChangeButtonIsDisplayed();
    	profilePage.getChangeBtn().click();
    	(new WebDriverWait(browser, 8000))
        .until(ExpectedConditions.urlContains("/login"));
    	assertEquals(baseUrl + "/login", browser.getCurrentUrl());

    }

    @Test 
    public void adminEditProfilePasswordTest() {
    	
    	this.gotoProfilePage();

    	profilePage.setOldPasswordInput("admin");
    	profilePage.setNewPasswordInput("admin1");

    	profilePage.ensureChangeButtonIsDisplayed();
    	profilePage.getChangeBtn().click();
    	(new WebDriverWait(browser, 8000))
        .until(ExpectedConditions.urlContains("/login"));
    	assertEquals(baseUrl + "/login", browser.getCurrentUrl());

    }
    
    
    
    @Test
    public void testChangeProfilePasswordEmptyFields() {
    	
    	this.gotoProfilePage();
    	profilePage.setOldPasswordInput("");
    	profilePage.setNewPasswordInput("");
    	
    	//profilePage.getChangeBtn().click();
        
    	assertTrue(profilePage.getNewPaswError().isDisplayed());
    	assertTrue(profilePage.getOldPaswError().isDisplayed());

		String errorMessage = profilePage.getNewPaswError().getText();
		assertEquals(NEW_PASSWORD_REQUIRED_ERROR_MSG, errorMessage);
		
		String errorMessage1 = profilePage.getOldPaswError().getText();
		assertEquals(OLD_PASSWORD_REQUIRED_ERROR_MSG, errorMessage1);
	
    }
    
    @Test
    public void testInvalidPasswordEditProfile() {
    	
    	this.gotoProfilePage();
    	profilePage.setOldPasswordInput("user");
    	profilePage.setNewPasswordInput("a");
    	
    	//profilePage.getChangeBtn().click();
        
    	assertTrue(profilePage.getNewPasErrorChar().isDisplayed());
    

		String errorMessage = profilePage.getNewPasErrorChar().getText();
		assertEquals(INVALID_PASSWORD_ERROR_MSG, errorMessage);
		
		
	
    }
    
    

  
    
    
}
