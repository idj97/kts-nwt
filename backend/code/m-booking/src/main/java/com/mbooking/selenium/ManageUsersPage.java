package com.mbooking.selenium;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
@Setter
public class ManageUsersPage {
    private WebDriver webDriver;

    @FindBy(id = "searchFirstname")
    private WebElement searchFirstname;

    @FindBy(id = "searchLastname")
    private WebElement searchLastname;

    @FindBy(id = "searchEmail")
    private WebElement searchEmail;

    @FindBy(id = "searchButton")
    private WebElement searchButton;

    @FindBy(id="banButton")
    private WebElement banButton;

    @FindBy(id="unbanButton")
    private WebElement unbanButton;

    @FindBy(id="navbar-main-btn")
    private WebElement navbarMainButton;

    @FindBy(id="user-tbody")
    private WebElement usersTbody;

    public ManageUsersPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void ensureIsDisplayed() {
        (new WebDriverWait(webDriver, 5))
                .until(ExpectedConditions.elementToBeClickable(navbarMainButton));
    }
}
