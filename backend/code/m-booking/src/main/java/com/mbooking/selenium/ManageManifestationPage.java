package com.mbooking.selenium;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
@Setter
public class ManageManifestationPage {

    private WebDriver webDriver;

    @FindBy(id = "mg-manifest-name")
    private WebElement nameInput;

    @FindBy(id = "mg-manifest-description")
    private WebElement descriptionInput;

    @FindBy(id = "manifestation-dates-holder")
    private WebElement manifestDaysHolder;

    @FindBy(id = "mg-manifest-type")
    private WebElement typeSelect;

    @FindBy(id = "mg-manifest-location")
    private WebElement locationSelect;

    @FindBy(id = "allow-reservations")
    private WebElement reservationsAllowedCheckBox;

    @FindBy(id = "mg-manifest-max-reservs")
    private WebElement maxReservationsInput;

    @FindBy(id = "mg-manifest-reservable-until")
    private WebElement reservableUntilInput;

    @FindBy(id = "submit-manifest-btn")
    private WebElement submitButton;

    @FindBy(id = "add-day-btn")
    private WebElement addDayButton;

    @FindBy(css = ".remove-date")
    private WebElement removeDayIcon;

    @FindBy(id = "configure-sections-btn")
    private WebElement configureSectionsBtn;

    @FindBy(css = ".return-btn")
    private WebElement returnFromSectionsBtn;


    public ManageManifestationPage(WebDriver driver) {
        this.webDriver = driver;
    }

    public void ensureIsDisplayed() {
        (new WebDriverWait(webDriver, 5))
                .until(ExpectedConditions.elementToBeClickable(submitButton));

    }

    public void ensureOptionsLoaded() {

        (new WebDriverWait(webDriver, 5))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By.cssSelector("select > option")));
    }



}
