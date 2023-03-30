package com.library.pages;

import com.library.utility.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BorrowedBooksPage extends BasePage{

    @FindBy(xpath = "//a[@class='btn btn-primary btn-sm  ']")
    public WebElement borrowBookBtn;

    //a[@class='btn btn-primary btn-sm ']//td[.='Do Androids Dream of Electric Sheep?']
    @FindBy(xpath = "//a[@class='btn btn-primary btn-sm ']")
    public WebElement returnBookBtn;

    @FindBy(xpath = "//tbody//td[2]")
    public List<WebElement> allBorrowedBooksName;


    public void returnBook(String bookTitle){
        Driver.getDriver().findElement(By.xpath("(//td[.='"+bookTitle+"'])[last()]//preceding-sibling::td[1]/a")).click();
    }

    //last row                                  (//tbody//td[2])[last()]
    //return button for last appearing of book  (//td[.='The Balkans'])[last()]//preceding-sibling::td[1]/a
    //return button found by bookTitle          td[.='The Balkans']//preceding-sibling::td[1]/a

}
