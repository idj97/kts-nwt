package com.mbooking.selenium;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

public class ReservationPageTest {
	
	private WebDriver browser;
	private ReservationPage reservationPage;
	
	private static final String baseUrl = "http://localhost:4200/manifestations/-1";
	
	@Before
    public void setUp() {

        // init browser
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        // init web page and it's elements
        browser.navigate().to(baseUrl);
        //Login as customer
  		JavascriptExecutor executor = (JavascriptExecutor) browser;
  		WebElement el = browser.findElement(By.tagName("body"));
  		executor.executeScript("window.localStorage.setItem('user', '{\"id\":-3,\"email\":\"ktsnwt.customer@gmail.com\",\"firstname\":\"Petar\",\"lastname\":\"Petrovic\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtYm9va2luZyIsInN1YiI6Imt0c253dC5jdXN0b21lckBnbWFpbC5jb20ifQ.9mpAChQDkECB5ubsM8Z-BCHEkJuQjL2XBWYqCdLkzhY\",\"authorities\":[\"ROLE_CUSTOMER\"],\"banned\":false}')", el);

        reservationPage = PageFactory.initElements(browser, ReservationPage.class);
        
        
    }
	
	@Test
	public void test_ensureReservationIsDisplayed_SelectSeatRow0Column11() {
		reservationPage.ensureManifestationLayoutIsDisplayed();
		List<WebElement> els = reservationPage.getSeatsElements();
		for (WebElement el : els) {
			int section = Integer.parseInt(el.getAttribute("data-section"));
			int manifestationSection = Integer.parseInt(el.getAttribute("data-manifestation-section"));
			int row = Integer.parseInt(el.getAttribute("data-seat-row"));
			int column = Integer.parseInt(el.getAttribute("data-seat-column"));
			if (row == 0 && column == 11 && section == -1 && manifestationSection == -1) {
				el.click();
				break;
			}
		}
		reservationPage.ensureReservationButtonIsDisplayed();
		assertTrue(reservationPage.getReservationButton().isDisplayed());
	}
	
	@After
    public void shutDown() {
        browser.quit();
    }
}









