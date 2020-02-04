package com.mbooking.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

public class RegisterTest {
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
		
	}

    @After
    public void shutDown() {
        browser.quit();
    }
}
