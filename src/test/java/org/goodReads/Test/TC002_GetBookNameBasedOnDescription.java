package org.goodReads.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goodReads.base.RestAssuredBase;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class TC002_GetBookNameBasedOnDescription extends RestAssuredBase {
	
	@BeforeTest
	public void setValues() {
		testCaseName = "Get Book name based on description";
		testDescription = "Get Book name based on description and verify it";
		nodes = "Steps to Get Book name based on description and verify it";
		authors = "Sanjay";
		category = "REST";
		dataFileName = "TC002";
		
	}
	
	@Test(dataProvider = "fetchData")
	public void getBookNameBasedOnDescription(String author, String Description) {
		Response response = getWithParam("q", author, "key", "6lrYcIJMFj37UMKUHz4A", "search/index.xml");
		verifyResponseCode(response, 200);
		Map<String, String> bookNameAndDescription = new HashMap<String,String>(); 
		List<String> idList = getListWithKey(response,
				"GoodreadsResponse.search.results.work.best_book.id");
		bookNameAndDescription = getDescriptionAndBookLinkUsingID(idList,bookNameAndDescription);
		System.out.println(bookNameAndDescription);
		String basedOn = "desc";
		String BookLink = searchMap(bookNameAndDescription, Description, basedOn);
		checkForNullValue(BookLink);
		
		verifyActualAndExpectedValue(BookLink,"https://www.goodreads.com/book/show/865.The_Alchemist");
		
	}
}