package com.mbooking.selenium;


import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
@Setter
public class Toaster {

    private WebDriver webDriver;

    @FindBy(id = "toaster")
    private WebElement toaster; // <div>

    @FindBy(id = "toaster-header")
    private WebElement toasterHeader; // <div>

    @FindBy(id = "toaster-message")
    private WebElement toasterMessage; // <p>

    public Toaster(WebDriver driver) {
        this.webDriver = driver;
    }




}
