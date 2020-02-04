package com.mbooking.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage {
	private WebDriver driver;

	@FindBy(id = "email")
	private WebElement emailInpit;

	@FindBy(id = "firstname")
	private WebElement firstnameInpit;

	@FindBy(id = "lastname")
	private WebElement lastnameInpit;
	
	@FindBy(id = "password")
	private WebElement passwordInput;
	
	@FindBy(id="register-btn")
	private WebElement registerButton;

	
	@FindBy(id="cancel-lnk")
	private WebElement cancelLnk;

	
	public WebElement getCancelLnk() {
		return cancelLnk;
	}

	public void setCancelLnk(WebElement cancelLnk) {
		this.cancelLnk = cancelLnk;
	}

	public RegisterPage(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public WebElement getEmailInpit() {
		return emailInpit;
	}

	public void setEmailInpit(String emailInpit) {
		WebElement e2 = getEmailInpit();
		e2.clear();
		e2.sendKeys(emailInpit);
	}

	public WebElement getFirstnameInpit() {
		return firstnameInpit;
	}

	public void setFirstnameInpit(String firstnameInpit) {
		WebElement e2 = getFirstnameInpit();
		e2.clear();
		e2.sendKeys(firstnameInpit);
	}

	public WebElement getLastnameInpit() {
		return lastnameInpit;
	}

	public void setLastnameInpit(String lastnameInpit) {
		WebElement e2 = getLastnameInpit();
		e2.clear();
		e2.sendKeys(lastnameInpit);
	}

	public WebElement getPasswordInput() {
		return passwordInput;
	}

	public void setPasswordInput(String passwordInput) {
		WebElement e2 = getPasswordInput();
		e2.clear();
		e2.sendKeys(passwordInput);
	}

	public WebElement getRegisterButton() {
		return registerButton;
	}

	public void setRegisterButton(WebElement registerButton) {
		this.registerButton = registerButton;
	}
	
	public void ensureRegisterButtonIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(registerButton));
	}
	

}
