package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.BorrowedBooksPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BorrowedBooks_StepDefs {
    BorrowedBooksPage borrowedBooksPage = new BorrowedBooksPage();
    BookPage bookPage = new BookPage();
    String expectedBookTitle;
    @And("the user searches for the book {string}")
    public void theUserSearchesForTheBook(String bookTitle) {
        bookPage.search.sendKeys(bookTitle + Keys.ENTER);
        this.expectedBookTitle = bookTitle;
        BrowserUtil.waitFor(2);
    }
    @When("the user clicks Borrow Book")
    public void the_user_clicks_borrow_book() {
        borrowedBooksPage.borrowBookBtn.click();
        BrowserUtil.waitFor(2);
    }

    @Then("verify that book is shown in {string} page")
    public void verify_that_book_is_shown_in_page(String pageTitle) {
        bookPage.goToDashboardLink(pageTitle);
        BrowserUtil.waitFor(2);

        List<WebElement> webElements = borrowedBooksPage.allBorrowedBooksName;
        List<String> allBorrowedBooks = new ArrayList<>();
        for(WebElement each : webElements){
            allBorrowedBooks.add(each.getText());
        }
        Assert.assertTrue(allBorrowedBooks.contains(expectedBookTitle));
    }

    @Then("verify logged student has same book in database")
    public void verify_logged_student_has_same_book_in_database() {
        String student_username = ConfigurationReader.getProperty("student_username");
    String query = "select b.name from book_borrow bb join books b on bb.book_id=b.id\n" +
            "where is_returned = 0 and user_id = (select id from users where email like '"+student_username+"');";
        DB_Util.runQuery(query);

        List<String> actualBorrowedBooks = DB_Util.getColumnDataAsList(1);

        Assert.assertTrue(actualBorrowedBooks.contains(expectedBookTitle));
    }

    @Then("return borrowed book")
    public void returnBorrowedBook() {
        borrowedBooksPage.returnBook(expectedBookTitle);

    }
}
