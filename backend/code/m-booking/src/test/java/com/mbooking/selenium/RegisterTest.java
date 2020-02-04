package com.mbooking.selenium;

import org.junit.After;
import org.junit.Before;
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
	
	
	

    @After
    public void shutDown() {
        browser.quit();
    }
}
