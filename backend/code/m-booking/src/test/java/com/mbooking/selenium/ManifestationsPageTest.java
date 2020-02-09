package com.mbooking.selenium;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ManifestationsPageTest {
	
	private WebDriver browser;
	private ReservationPage reservationPage;
	private ManifestationsPage manifestationsPage;
	
	private static final String baseUrl = "http://localhost:4200/manifestations";
	private static final String homeUrl = "http://localhost:4200/home";
	
	@Before
    public void setUp() {

        // init browser
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        // init web page and it's elements
        browser.navigate().to(baseUrl);
        //Login as customer
        reservationPage = PageFactory.initElements(browser, ReservationPage.class);
        manifestationsPage = PageFactory.initElements(browser, ManifestationsPage.class);
    }
	
	@Test
	public void test_visitManifestationByNameSearch() {
		manifestationsPage.ensureSearchNameIsDisplayed();
		manifestationsPage.getSearchName().sendKeys("Manifestation 1");
		(new WebDriverWait(browser, 5)).until(n -> {
			return n.findElements(By.cssSelector(".button")).size() == 1;
		});
		manifestationsPage.getVisitButton().click();
		
		assertEquals(baseUrl + "/-1", browser.getCurrentUrl());
	}
	
	@Test
	public void test_visitHomePageFromManifestations() throws InterruptedException {
		manifestationsPage.ensureSearchNameIsDisplayed();
		List<WebElement> els = browser.findElements(By.cssSelector(".navbar-brand.nav-black-color"));
		els.stream().filter(n -> n.getAttribute("href") != "/");
		
		els.get(0).click();
		
		assertEquals(homeUrl, browser.getCurrentUrl());
	}
	
	@After
    public void shutDown() {
        browser.quit();
    }
}

















