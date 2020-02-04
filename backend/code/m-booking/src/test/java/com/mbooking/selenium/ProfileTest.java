package com.mbooking.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Test;

public class ProfileTest {
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
    
    
    @Test 
    public void editProfilePasswordTest() {
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
    	
    	profilePage.setOldPasswordInput("user");
    	profilePage.setNewPasswordInput("user1");
    	
    	profilePage.ensureChangeButtonIsDisplayed();
    	profilePage.getChangeBtn().click();
    	(new WebDriverWait(browser, 8000))
        .until(ExpectedConditions.urlContains("/login"));
    	assertEquals(baseUrl + "/login", browser.getCurrentUrl());
    	
    }
    
    
}
