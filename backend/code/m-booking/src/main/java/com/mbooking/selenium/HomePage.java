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
public class HomePage {

    private WebDriver webDriver;

    @FindBy(className = "explore-button")
    private WebElement exploreBtn;

    public HomePage(WebDriver driver) {
        this.webDriver = driver;
    }

    /** Make sure that the page is loaded by referencing an element in it
     * before continuing with the test */
    public void ensureIsDisplayed() {
        (new WebDriverWait(webDriver, 5))
                .until(ExpectedConditions.elementToBeClickable(exploreBtn));
    }


}
