package com.library.steps;

import com.library.pages.DashBoardPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.sql.DriverManager;

public class Dashboard_StepDefs {
    LoginPage loginPage = new LoginPage();
    DashBoardPage dashBoardPage = new DashBoardPage();
    String actualBbNumber;

    @Given("the {string} is on the home page")
    public void the_is_on_the_home_page(String usertype) {
        //log in dynamically
        String username = ConfigurationReader.getProperty(usertype + "_username");
        String password = ConfigurationReader.getProperty(usertype + "_password");
        loginPage.emailBox.sendKeys(username);
        loginPage.passwordBox.sendKeys(password);
        loginPage.loginButton.click();

        //confirm user is on the homepage
        BrowserUtil.waitFor(4);
        String actualTitle = Driver.getDriver().getTitle();
        String expectedTitle = "Library";
        Assert.assertEquals(expectedTitle,actualTitle);

    }

    @When("the librarian gets borrowed books number")
    public void the_librarian_gets_borrowed_books_number() {
        actualBbNumber = dashBoardPage.borrowedBooksNumber.getText();
        System.out.println("actualBbNumber = " + actualBbNumber);
        Assert.assertTrue(dashBoardPage.borrowedBooksNumber.isDisplayed());

    }

    @Then("borrowed books number information must match with DB")
    public void borrowed_books_number_information_must_match_with_db() {
    String query = "select count(*) from book_borrow where is_returned = 0";

        DB_Util.runQuery(query);
        String expectedBbNumber = DB_Util.getFirstRowFirstColumn();
        Assert.assertEquals(expectedBbNumber,actualBbNumber);

    }
}
