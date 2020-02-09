package com.mbooking.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationPage {
	private WebDriver webDriver;
	
	@FindBy(id = "date-selection")
	private WebElement dateSelection;
	
	@FindBy(css  = ".manifestation-layout")
	private WebElement manifestationLayout;
	
	@FindBy(id  = "reservation-button")
	private WebElement reservationButton;
	
	public ReservationPage(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	public void ensureDateSelectionIsDisplayed() {
		(new WebDriverWait(this.webDriver, 5))
			.until(ExpectedConditions.elementToBeClickable(dateSelection));
	}
	
	public void ensureManifestationLayoutIsDisplayed() {
		(new WebDriverWait(this.webDriver, 7))
		.until(ExpectedConditions.elementToBeClickable(manifestationLayout));
	}
	
	public void ensureReservationButtonIsDisplayed() {
		(new WebDriverWait(this.webDriver, 5))
			.until(ExpectedConditions.elementToBeClickable(reservationButton));
	}
	
	public List<WebElement> getSeatsElements() {
		
		List<WebElement> elements = webDriver.findElements(By.cssSelector(".seat"));
		return elements;
	}
	
}
