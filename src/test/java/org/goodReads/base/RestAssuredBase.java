package org.goodReads.base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredBase extends PreAndTest {

	public static RequestSpecification setLogs() {
		return RestAssured.given().log().all();

	}

	public static Response getWithParam(String apiKey, String value, String resourse) {
		return setLogs().params(apiKey, value).when().get(resourse);
	}

	public static Response getWithParam(String query, String searchKeyword, String apiKey, String value,
			String resourse) {
		return setLogs().params(query, searchKeyword).and().params(apiKey, value).when().get(resourse);
	}

	public static void verifyResponseCode(Response response, int code) {
		if (response.statusCode() == code) {

			reportRequest("The status code " + code + " matches the expected code", "PASS");
		} else {

			reportRequest("The status code " + code + " does not match the expected code" + response.statusCode(),
					"FAIL");
		}
	}

	public static List<String> getListWithKey(Response response, String key) {

		XmlPath xmlResponse = response.xmlPath();
		return xmlResponse.getList(key);

	}

	public static List<String> createModifiableListWithZeroBeforeSingleCharacters(List<String> inputList,
			List<String> modifiableoutputList) {
		for (int i = 0; i < inputList.size(); i++) {

			if (inputList.get(i).length() == 1 && !inputList.get(i).equals("null")) {
				modifiableoutputList.add("0" + inputList.get(i));
			} else {
				modifiableoutputList.add(inputList.get(i));
			}
		}
		return modifiableoutputList;
	}

	public static Map<String, String> createMapWithBookAndReleaseDate(List<String> bookName, List<String> date,
			Map<String, String> bookNameAndReleaseDate) {
		for (int i = 0; i < bookName.size(); i++) {
			if (!date.get(i).contains("null")) {
				bookNameAndReleaseDate.put(bookName.get(i), date.get(i));
			}
		}
		return bookNameAndReleaseDate;
	}

	public static String getRecentDateFromMap(Map<String, String> BookNameReleaseDate, List<Date> dates)
			throws ParseException {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		for (Map.Entry<String, String> a : BookNameReleaseDate.entrySet()) {

			Date date = fmt.parse(a.getValue());
			dates.add(date);
		}

		Date recentReleasedBookDate = Collections.max(dates);
		String dateStr = recentReleasedBookDate.toString();
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date1 = (Date) formatter.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
				+ cal.get(Calendar.DATE);
		return formatedDate;

	}

	public String searchMap(Map<String, String> inputMap, String searchValue, String basedOn) {
		String matchedKey = new String();
		for (Map.Entry<String, String> a : inputMap.entrySet()) {
			if (basedOn.equals("date")) {
				if (a.getValue().equals(searchValue)) {
					matchedKey = a.getKey();
					break;
				}
			} else if (basedOn.equals("desc")) {
				if (a.getValue().contains("to travel in search of a worldly treasure")) {
					matchedKey = a.getKey();
				}
			}
		}
		return matchedKey;
	}

	public void verifyActualAndExpectedValue(String actual, String expected) {

		if (actual.equals(expected)) {
			reportRequest("The actual \"" + actual + "\" matches with the expected \"" + expected + "\"", "PASS");
		} else {
			reportRequest("The actual \"" + actual + "\" doesn't matches with the expected \"" + expected + "\"",
					"FAIL");
		}
	}

	public Map<String, String> getDescriptionAndBookLinkUsingID(List<String> idList, Map<String, String> outputMap) {
		for (int i = 0; i < idList.size(); i++) {
			Response response = getWithParam("key", "6lrYcIJMFj37UMKUHz4A", "book/show/" + idList.get(i) + ".xml");

			XmlPath xmlResponse = response.xmlPath();
			outputMap.put(xmlResponse.getString("GoodreadsResponse.book.url"),
					xmlResponse.getString("GoodreadsResponse.book.description"));

		}
		return outputMap;
	}

	public String getBookerPrizerWinnerBook(List<String> idList) {

		String bookerPrizeBooks = new String();
		for (int i = 0; i < idList.size(); i++) {

			Response response = getWithParam("key", "6lrYcIJMFj37UMKUHz4A", "book/show/" + idList.get(i) + ".xml");
			XmlPath xmlResponse = response.xmlPath();
			List<String> shelfs = xmlResponse.getList("GoodreadsResponse.book.popular_shelves.shelf.@name");
			String bookName = xmlResponse.get("GoodreadsResponse.book.title").toString();
			for (int j = 0; j < shelfs.size(); j++) {

				if (shelfs.get(j).equals("booker-prize")) {
					bookerPrizeBooks = bookName;

				}
			}
		}
		return bookerPrizeBooks;
	}

	public void checkForNullValue(String value) {
		if (value.equals("null")) {
			reportRequest("The value is null", "FAIL");
		}
	}

}
