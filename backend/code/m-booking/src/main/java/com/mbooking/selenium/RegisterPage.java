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
	
	@FindBy(id="email-error-req")
	private WebElement emailErrorReq;

	@FindBy(id="email-error-v")
	private WebElement emailErrorValid;
	
	@FindBy(id = "firstname-error")
	private WebElement firstnameError;
	
	@FindBy(id = "lastname-error")
	private WebElement lastnameError;
	
	
	@FindBy(id="password-error")
	private WebElement passwErrorReq;
	
	@FindBy(id="password-error-con")
	private WebElement passwErrorChar;
	
	
	@FindBy(id="password-error-conMax")
	private WebElement passwErrorCharMax;
	

	
	
	
	
	public WebElement getPasswErrorCharMax() {
		return passwErrorCharMax;
	}

	public void setPasswErrorCharMax(WebElement passwErrorCharMax) {
		this.passwErrorCharMax = passwErrorCharMax;
	}

	public WebElement getEmailErrorReq() {
		return emailErrorReq;
	}

	public void setEmailErrorReq(WebElement emailErrorReq) {
		this.emailErrorReq = emailErrorReq;
	}

	public WebElement getEmailErrorValid() {
		return emailErrorValid;
	}

	public void setEmailErrorValid(WebElement emailErrorValid) {
		this.emailErrorValid = emailErrorValid;
	}

	public WebElement getFirstnameError() {
		return firstnameError;
	}

	public void setFirstnameError(WebElement firstnameError) {
		this.firstnameError = firstnameError;
	}

	public WebElement getLastnameError() {
		return lastnameError;
	}

	public void setLastnameError(WebElement lastnameError) {
		this.lastnameError = lastnameError;
	}

	public WebElement getPasswErrorReq() {
		return passwErrorReq;
	}

	public void setPasswErrorReq(WebElement passwErrorReq) {
		this.passwErrorReq = passwErrorReq;
	}

	public WebElement getPasswErrorChar() {
		return passwErrorChar;
	}

	public void setPasswErrorChar(WebElement passwErrorChar) {
		this.passwErrorChar = passwErrorChar;
	}

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
