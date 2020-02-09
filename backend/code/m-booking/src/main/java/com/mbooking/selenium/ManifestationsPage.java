package com.mbooking.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManifestationsPage {
	private WebDriver webDriver;
	
	@FindBy(id = "search-name")
	private WebElement searchName;
	
	@FindBy(css = ".button")
	private WebElement visitButton;
	
	public ManifestationsPage(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	public void ensureSearchNameIsDisplayed() {
		(new WebDriverWait(this.webDriver, 5))
			.until(ExpectedConditions.elementToBeClickable(searchName));
	}
	
	public void ensureVisitIsDisplayed() {
		(new WebDriverWait(this.webDriver, 5))
			.until(ExpectedConditions.elementToBeClickable(visitButton));
	}
	
}
