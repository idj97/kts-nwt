package com.mbooking.selenium;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter

public class LoginPage {


	private WebDriver driver;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(className = "button")
    private WebElement loginButton;

    
    public void setUsernameInput(String usernameInput) {
    	WebElement e2 = getUsernameInput();
		e2.clear();
		e2.sendKeys(usernameInput);
	}


	public void setPasswordInput(String passwordInput) {
		WebElement e2 = getPasswordInput();
		e2.clear();
		e2.sendKeys(passwordInput);
	}


	public void setLoginButton(WebElement loginButton) {
		this.loginButton = loginButton;
	}

    private static final String baseUrl = "http://localhost:4200";


    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }


    public void ensureIsDisplayed() {
        (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.visibilityOf(loginButton));

    }


    public void login(String username, String password) {

        // navigating to login page
        this.driver.navigate().to(baseUrl + "/login");
        this.ensureIsDisplayed();

        this.usernameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
        this.loginButton.click();

        // wait until login is completed and the user is redirected to home
        (new WebDriverWait(driver, 8000))
                .until(ExpectedConditions.urlContains("/home"));

    }


}
