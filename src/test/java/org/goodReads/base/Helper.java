package org.goodReads.base;

import java.util.ArrayList;

import java.util.List;

import io.restassured.response.Response;

public class Helper extends RestAssuredBase {

	/**
	 * This method is used to get the year, month and date from the response and
	 * convert it into the required format and return it
	 * 
	 * @param response Response received
	 * @return list of date in required format(YYYY-MM-DD)
	 */
	public static List<String> bookpublishedDate(Response response) {
		List<String> publishedYear = getListWithKey(response,
				"GoodreadsResponse.search.results.work.original_publication_year");
		List<String> publishedMonth = getListWithKey(response,
				"GoodreadsResponse.search.results.work.original_publication_month");
		List<String> publishedDate = getListWithKey(response,
				"GoodreadsResponse.search.results.work.original_publication_day");

		List<String> ModifiablePublishedMonth = new ArrayList<String>();
		List<String> ModifiablePublishedDate = new ArrayList<String>();

		ModifiablePublishedMonth = createModifiableListWithZeroBeforeSingleCharacters(publishedMonth,
				ModifiablePublishedMonth);
		ModifiablePublishedDate = createModifiableListWithZeroBeforeSingleCharacters(publishedDate,
				ModifiablePublishedDate);
		List<String> date = new ArrayList<String>();
		for (int i = 0; i < publishedYear.size(); i++) {
			date.add(publishedYear.get(i) + "-" + publishedMonth.get(i) + "-" + publishedDate.get(i));
		}
		return date;
	}

//	

}
