package org.goodReads.test;

import java.util.List;

import org.goodReads.base.RestAssuredBase;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class GetBookNameOfBookerPrizeWinner extends RestAssuredBase {
	
	@BeforeTest
	public void setValues() {
		testCaseName = "Get book name of the Booker prize winner";
		testDescription = "Get book name of the Booker prize winner and verify it";
		nodes = "Steps to Get book name of the Booker prize winner";
		authors = "Sanjay";
		category = "REST";
		dataFileName = "TC003";

	}
	
	@Test(dataProvider = "fetchData")
	public void getNameOfBookePrizeWinner(String searchQuery, String expectedBookName) {
		Response response = getWithParam("q", searchQuery, "key", "6lrYcIJMFj37UMKUHz4A", "search/index.xml");
		verifyResponseCode(response, 200);
		List<String> idList = getListWithKey(response, "GoodreadsResponse.search.results.work.best_book.id");
		
		String bookPrizerWinnerBook = getBookerPrizerWinnerBook(idList);
		reportRequest("The Booker Prize winner book is \"" + bookPrizerWinnerBook +"\"", "PASS");
		verifyActualAndExpectedValue(bookPrizerWinnerBook, expectedBookName);
	}
}
