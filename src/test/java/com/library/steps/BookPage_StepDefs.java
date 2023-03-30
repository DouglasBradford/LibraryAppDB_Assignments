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
        String expectedTitle = "";
        if(usertype.equals("librarian")){
        expectedTitle = "dashboard";
        }else if(usertype.equals("student")){
            expectedTitle = "books";
        }
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
                "where b.name = '" + bookTitle + "'";
        DB_Util.runQuery(query);
        List<String> expectedBookInfo = DB_Util.getRowDataAsList(1);
        System.out.println("expectedBookInfo = " + expectedBookInfo);
        //Compare
        Assert.assertEquals(expectedBookInfo, actualBookInfo);
    }

    // US06
    @When("the librarian click to add book")
    public void the_librarian_click_to_add_book() {
        Assert.assertTrue(bookPage.addBook.isDisplayed());
        bookPage.addBook.click();
        BrowserUtil.waitFor(2);
    }

    String bookName;

    @When("the librarian enter book name {string}")
    public void the_librarian_enter_book_name(String bookName) {
        bookPage.bookName.sendKeys(bookName);
        this.bookName = bookName;
        BrowserUtil.waitFor(2);
    }

    String ISBN;

    @When("the librarian enter ISBN {string}")
    public void the_librarian_enter_isbn(String ISBN) {
        bookPage.isbn.sendKeys(ISBN);
        this.ISBN = ISBN;
        BrowserUtil.waitFor(2);
    }

    String year;

    @When("the librarian enter year {string}")
    public void the_librarian_enter_year(String year) {
        bookPage.year.sendKeys(year);
        this.year = year;
        BrowserUtil.waitFor(2);
    }

    String author;

    @When("the librarian enter author {string}")
    public void the_librarian_enter_author(String author) {
        bookPage.author.sendKeys(author);
        this.author = author;
        BrowserUtil.waitFor(2);
    }

    String bookCategory;

    @When("the librarian choose the book category {string}")
    public void the_librarian_choose_the_book_category(String bookCategory) {
        Select select = new Select(bookPage.categoryDropdown);
        select.selectByVisibleText(bookCategory);
        this.bookCategory = bookCategory;
        BrowserUtil.waitFor(2);
    }

    @When("the librarian click to save changes")
    public void the_librarian_click_to_save_changes() {
        Assert.assertTrue(bookPage.saveChanges.isDisplayed());
        bookPage.saveChanges.click();
        BrowserUtil.waitFor(3);
    }

    @Then("verify {string} message is displayed")
    public void verify_message_is_displayed(String expectedMessage) {
        String actualMessage = bookPage.toastMessage.getText();
        System.out.println("actualMessage = " + actualMessage);
        Assert.assertEquals(expectedMessage, actualMessage);
    }



    @Then("verify {string} information must match with DB")
    public void verify_information_must_match_with_db(String bookName2) {
        List<String> actualBookInfo = new ArrayList<>();
        actualBookInfo.add(bookName);
        actualBookInfo.add(ISBN);
        actualBookInfo.add(year);
        actualBookInfo.add(author);
        actualBookInfo.add(bookCategory);
        System.out.println("actualBookInfo = " + actualBookInfo);
        String query = "select b.name,isbn,year,author,bc.name from books b join book_categories bc on b.book_category_id = bc.id\n" +
                "where b.name ='" + bookName2 + "'";
        DB_Util.runQuery(query);
        List<String> expectedBookInfo = DB_Util.getRowDataAsList(1);
        System.out.println("expectedBookInfo = " + expectedBookInfo);
        Assert.assertEquals(expectedBookInfo, actualBookInfo);

    }
}
