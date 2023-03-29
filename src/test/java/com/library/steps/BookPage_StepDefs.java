package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class BookPage_StepDefs {
    LoginPage loginPage = new LoginPage();
    BookPage bookPage = new BookPage();
    String bookTitle;

    List<String> actualBookCategoryOptions;

    @Given("the {string} on the home page")
    public void the_on_the_home_page(String usertype) {
        String username = ConfigurationReader.getProperty(usertype + "_username");
        String password = ConfigurationReader.getProperty(usertype + "_password");
        loginPage.emailBox.sendKeys(username);
        loginPage.passwordBox.sendKeys(password);
        loginPage.loginButton.click();

        BrowserUtil.waitFor(4);
        String expectedTitle = "dashboard";
        String actualTitle = Driver.getDriver().getCurrentUrl();
        Assert.assertTrue(actualTitle.contains(expectedTitle));
    }

    @When("the user navigates to {string} page")
    public void the_user_navigates_to_page(String PageTitle) {
        //Driver.getDriver().findElement(By.xpath("//span[@class='title'][.='"+PageTitle+"']")).click();

        //method version of above command
        bookPage.goToDashboardLink(PageTitle);
        BrowserUtil.waitFor(4);
        String actualTitle = Driver.getDriver().getCurrentUrl();
        Assert.assertTrue(actualTitle.contains(PageTitle.toLowerCase()));

    }

    @When("the user clicks book categories")
    public void the_user_clicks_book_categories() {
        bookPage.mainCategoryElement.click();
        //get dropdown options as list of String
        Select select = new Select(bookPage.mainCategoryElement);
        actualBookCategoryOptions = new ArrayList<>();
        for (WebElement eachOption : select.getOptions()) {
            actualBookCategoryOptions.add(eachOption.getText());
        }
        actualBookCategoryOptions.remove(0);
        System.out.println("actualBookCategoryOptions = " + actualBookCategoryOptions);
    }

    @Then("verify book categories must match book_categories table from db")
    public void verify_book_categories_must_match_book_categories_table_from_db() {
        String query = "select name from book_categories";
        DB_Util.runQuery(query);
        List<String> expectedBookCategoryOptions = DB_Util.getColumnDataAsList(1);
        Assert.assertEquals(expectedBookCategoryOptions, actualBookCategoryOptions);
    }

    //US04 StepDefs
    @When("the user searches for {string} book")
    public void the_user_searches_for_book(String bookTitle) {
        this.bookTitle = bookTitle;
        //Send data in search field
        bookPage.search.sendKeys(bookTitle + Keys.ENTER);

    }

    @When("the user clicks edit book button")
    public void the_user_clicks_edit_book_button() {
        bookPage.editBook(bookTitle).click();
        BrowserUtil.waitFor(2);
        Assert.assertTrue(bookPage.saveChanges.isDisplayed());
    }

    @Then("book information must match the Database")
    public void book_information_must_match_the_database() {
        //Get data from UI
        List<String> actualBookInfo = new ArrayList<>();
        actualBookInfo.add(bookPage.bookName.getAttribute("value"));
        actualBookInfo.add(bookPage.author.getAttribute("value"));
        actualBookInfo.add(bookPage.isbn.getAttribute("value"));
        actualBookInfo.add(bookPage.year.getAttribute("value"));
        Select select = new Select(bookPage.categoryDropdown);
        actualBookInfo.add(select.getFirstSelectedOption().getText());
        actualBookInfo.add(bookPage.description.getText());
        System.out.println("actualBookInfo = " + actualBookInfo);
        //Get data from DB
        String query = "select b.name,author,isbn, year,bc.name,b.description\n" +
                "from books b join book_categories bc on b.book_category_id = bc.id\n" +
                "where b.name = '"+bookTitle+"'";
        DB_Util.runQuery(query);
        List<String> expectedBookInfo = DB_Util.getRowDataAsList(1);
        System.out.println("expectedBookInfo = " + expectedBookInfo);
        //Compare
        Assert.assertEquals(expectedBookInfo, actualBookInfo);
    }
}
