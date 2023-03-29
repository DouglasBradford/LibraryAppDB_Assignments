package com.library.steps;

import com.library.utility.DB_Util;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class LibraryInfo_StepDefs {
    String actualMostPopular;

    //US05
    @When("I execute query to find most popular book genre")
    public void i_execute_query_to_find_most_popular_book_genre() {
    String query = "select bc.name,count(*) from book_borrow bb join books b on bb.book_id= b.id\n" +
            "         join book_categories bc on b.book_category_id = bc.id\n" +
            "where is_returned = 0\n" +
            "group by bc.name\n" +
            "order by count(*) desc";
        DB_Util.runQuery(query);
        actualMostPopular = DB_Util.getFirstRowFirstColumn();
        System.out.println("actualMostPopular = " + actualMostPopular);

    }
    @Then("verify {string} is the most popular book genre.")
    public void verify_is_the_most_popular_book_genre(String expected) {
        Assert.assertEquals(expected,actualMostPopular);
    }
}
