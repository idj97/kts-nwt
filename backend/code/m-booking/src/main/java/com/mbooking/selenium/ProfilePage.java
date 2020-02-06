package com.mbooking.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilePage {
	private WebDriver driver;

	@FindBy(id = "firstname")
	private WebElement firstnameInp;

	@FindBy(id = "lastname")
	private WebElement lastnameInp;

	@FindBy(id = "edit-btn")
	private WebElement editButton;

	@FindBy(xpath = "/html/body/app-root/app-profile/div/div/div[1]/form/div[3]/a")
	private WebElement cancelLink;

	@FindBy(id = "oldPassword")
	private WebElement oldPasswordInput;

	@FindBy(id = "newPassword")
	private WebElement newPasswordInput;

	@FindBy(id = "changeBtn")
	private WebElement changeBtn;
//
	
	@FindBy(id = "firstname-error")
	private WebElement firstnameError;

	@FindBy(id = "lastname-error")
	private WebElement lastnameError;

	@FindBy(id = "old-password-error")
	private WebElement oldPaswError;

	@FindBy(id = "new-password-error")
	private WebElement newPaswError;

	@FindBy(id = "new-passw-char")
	private WebElement newPasErrorChar;
	
	@FindBy(id="new-password-error-conMax")
	private WebElement newPasswErrorCharMax;
	

	public WebElement getNewPasswErrorCharMax() {
		return newPasswErrorCharMax;
	}

	public void setNewPasswErrorCharMax(WebElement newPasswErrorCharMax) {
		this.newPasswErrorCharMax = newPasswErrorCharMax;
	}

	public ProfilePage(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public WebElement getFirstnameInp() {
		return firstnameInp;
	}

	public void setFirstnameInp(String firstnameInp) {
		WebElement e2 = getFirstnameInp();
		e2.clear();
		e2.sendKeys(firstnameInp);
	}

	public WebElement getLastnameInp() {
		return lastnameInp;
	}

	public void setLastnameInp(String lastnameInp) {
		WebElement e2 = getLastnameInp();
		e2.clear();
		e2.sendKeys(lastnameInp);
	}

	public WebElement getEditButton() {
		return editButton;
	}

	public void setEditButton(WebElement editButton) {
		this.editButton = editButton;
	}

	public WebElement getCancelLink() {
		return cancelLink;
	}

	public void setCancelLink(WebElement cancelLink) {
		this.cancelLink = cancelLink;
	}

	public WebElement getOldPasswordInput() {
		return oldPasswordInput;
	}

	public void setOldPasswordInput(String oldPasswordInput) {
		WebElement e2 = getOldPasswordInput();
		e2.clear();
		e2.sendKeys(oldPasswordInput);
	}

	

	public WebElement getNewPasswordInput() {
		return newPasswordInput;
	}

	public void setNewPasswordInput(String newPasswordInput) {
		WebElement e2 = getNewPasswordInput();
		e2.clear();
		e2.sendKeys(newPasswordInput);
	}

	public WebElement getChangeBtn() {
		return changeBtn;
	}

	public void setChangeBtn(WebElement changeBtn) {
		this.changeBtn = changeBtn;
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

	public WebElement getOldPaswError() {
		return oldPaswError;
	}

	public void setOldPaswError(WebElement oldPaswError) {
		this.oldPaswError = oldPaswError;
	}

	public WebElement getNewPaswError() {
		return newPaswError;
	}

	public void setNewPaswError(WebElement newPaswError) {
		this.newPaswError = newPaswError;
	}

	public WebElement getNewPasErrorChar() {
		return newPasErrorChar;
	}

	public void setNewPasErrorChar(WebElement newPasErrorChar) {
		this.newPasErrorChar = newPasErrorChar;
	}

	public void ensureEditButtonIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(editButton));
	}
	
	public void ensureChangeButtonIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(changeBtn));
	}

}
