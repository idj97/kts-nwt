package com.mbooking.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;


public class HomePageTest {

    private WebDriver browser;
    private HomePage homePage;

    private static final String baseUrl = "http://localhost:4200";

    @Before
    public void setUp() {

        // init browser
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();

        // init web page and it's elements
        browser.navigate().to(baseUrl);
        homePage = PageFactory.initElements(browser, HomePage.class);
    }

    @Test
    public void whenExploreBtnClicked_thenRedirectToManifestations() {

        homePage.ensureIsDisplayed();
        assertEquals(baseUrl + "/home", browser.getCurrentUrl());

        homePage.getExploreBtn().click();
        assertEquals(baseUrl + "/manifestations", browser.getCurrentUrl());

    }


    @After
    public void shutDown() {
        browser.quit();
    }

}
