package org.goodReads.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goodReads.base.RestAssuredBase;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class TC001_GetLatestBookOfAuthor extends RestAssuredBase {

	@BeforeTest
	public void setValues() {
		testCaseName = "Get latest book of the Author";
		testDescription = "Get latest book of the Author and verify it";
		nodes = "Steps latest book of the Author and verify it";
		authors = "Sanjay";
		category = "REST";
		dataFileName = "TC001";
		
	}

	@Test(dataProvider = "fetchData")
	public void getLatestBookOfAuthor(String author, String recentlyReleasedBook) throws ParseException {

		Response response = getWithParam("q", author, "key", "6lrYcIJMFj37UMKUHz4A", "search/index.xml");
		verifyResponseCode(response, 200);
		List<String> publishedYear = getListWithKey(response,
				"GoodreadsResponse.search.results.work.original_publication_year");
		List<String> publishedMonth = getListWithKey(response,
				"GoodreadsResponse.search.results.work.original_publication_month");
		List<String> publishedDate = getListWithKey(response,
				"GoodreadsResponse.search.results.work.original_publication_day");
		List<String> bookName = getListWithKey(response, "GoodreadsResponse.search.results.work.best_book.title");
		List<String> ModifiablePublishedMonth = new ArrayList<String>();
		List<String> ModifiablePublishedDate = new ArrayList<String>();
		Map<String, String> BookNameReleaseDate = new HashMap<String, String>();
		List<Date> dates = new ArrayList<Date>();
		createModifiableListWithZeroBeforeSingleCharacters(publishedMonth, ModifiablePublishedMonth);
		createModifiableListWithZeroBeforeSingleCharacters(publishedDate, ModifiablePublishedDate);
		createMapWithBookAndReleaseDate(bookName, publishedYear, ModifiablePublishedMonth, ModifiablePublishedDate,
				BookNameReleaseDate);
		String recentDate = getRecentDateFromMap(BookNameReleaseDate, dates);
		String basedOn = "date";
		String RecentlyReleasedBookName = searchMap(BookNameReleaseDate, recentDate, basedOn);
		reportRequest("The recently released book name is  " + RecentlyReleasedBookName,
				"PASS");
		verifyActualAndExpectedValue(RecentlyReleasedBookName, recentlyReleasedBook);

	}

}
