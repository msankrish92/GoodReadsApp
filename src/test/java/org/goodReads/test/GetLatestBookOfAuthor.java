package org.goodReads.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goodReads.base.Helper;
import org.goodReads.base.RestAssuredBase;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class GetLatestBookOfAuthor extends RestAssuredBase {

	@BeforeTest
	public void setValues() {
		testCaseName = "Get latest book of the Author";
		testDescription = "Get latest book of the Author and verify it";
		nodes = "Steps to latest book of the Author and verify it";
		authors = "Sanjay";
		category = "REST";
		dataFileName = "TC001";

	}

	@Test(dataProvider = "fetchData")
	public void getLatestBookOfAuthor(String author, String expectedRecentlyReleasedBook) throws ParseException {

		Response response = getWithParam("q", author, "key", "6lrYcIJMFj37UMKUHz4A", "search/index.xml");
		verifyResponseCode(response, 200);
		List<String> bookName = getListWithKey(response, "GoodreadsResponse.search.results.work.best_book.title");
		List<String> publishedDateOfBooks = Helper.bookpublishedDate(response);

		Map<String, String> bookNameAndReleaseDate = new HashMap<String, String>();
		List<Date> dates = new ArrayList<Date>();

		bookNameAndReleaseDate = createMapWithBookAndReleaseDate(bookName, publishedDateOfBooks,
				bookNameAndReleaseDate);

		String recentDate = getRecentDateFromMap(bookNameAndReleaseDate, dates);
		String basedOn = "date";
		String recentlyReleasedBookName = searchMap(bookNameAndReleaseDate, recentDate, basedOn);
		reportRequest("The recently released book name is  " + recentlyReleasedBookName, "PASS");
		verifyActualAndExpectedValue(recentlyReleasedBookName, expectedRecentlyReleasedBook);

	}

}
