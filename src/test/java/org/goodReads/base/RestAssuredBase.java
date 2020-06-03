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

/**
 * The class act as base class where all the core functionalities resides to
 * perform various operations.
 */
public class RestAssuredBase extends PreAndTest {

	/**
	 * This method is used to get the log while sending request.
	 * 
	 * @return RestAssured specification
	 */
	public static RequestSpecification setLogs() {
		return RestAssured.given().log().all();

	}

	/**
	 * This method is used to get the response.
	 * 
	 * @param apiKey   user key
	 * @param value    value of api Key
	 * @param resourse passed along with baseURI
	 * @return response
	 */
	public static Response getWithParam(String apiKey, String value, String resourse) {
		return setLogs().params(apiKey, value).when().get(resourse);
	}

	/**
	 * This method is used to get the response.
	 * 
	 * @param query         query key
	 * @param searchKeyword text to be searched
	 * @param apiKey        user key
	 * @param value         value of api Key
	 * @param resourse      passed along with baseURI
	 * @return response
	 */
	public static Response getWithParam(String query, String searchKeyword, String apiKey, String value,
			String resourse) {
		return setLogs().params(query, searchKeyword).and().params(apiKey, value).when().get(resourse);
	}

	/**
	 * This method is used to verify the Response code and creates an entry in
	 * report.
	 * 
	 * @param response Response received
	 * @param code     expected code
	 */
	public static void verifyResponseCode(Response response, int code) {
		if (response.statusCode() == code) {

			reportRequest("The status code " + code + " matches the expected code", "PASS");
		} else {

			reportRequest("The status code " + code + " does not match the expected code" + response.statusCode(),
					"FAIL");
		}
	}

	/**
	 * This method converts the response into XML and returns list based on the key
	 * passed.
	 * 
	 * @param response Response received
	 * @param key      of value we need to get from response
	 * @return list of values for the key
	 */
	public static List<String> getListWithKey(Response response, String key) {

		XmlPath xmlResponse = response.xmlPath();
		return xmlResponse.getList(key);

	}

	/**
	 * This method takes all the values from the list and check if for the length,
	 * if the length is equal to 1 it add 0 as a prefix.
	 * 
	 * @param inputList            with the value of date or month
	 * @param modifiableoutputList to convert it into modifiable List
	 * @return modifiable List
	 */
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

	/**
	 * This method check whether date as contains any null if not adds book name
	 * along with release date into a map.
	 * 
	 * @param bookName               list of books
	 * @param date                   list of release date
	 * @param bookNameAndReleaseDate
	 * @return book and release date as Maps key value pair
	 */
	public static Map<String, String> createMapWithBookAndReleaseDate(List<String> bookName, List<String> date,
			Map<String, String> bookNameAndReleaseDate) {
		for (int i = 0; i < bookName.size(); i++) {
			if (!date.get(i).contains("null")) {
				bookNameAndReleaseDate.put(bookName.get(i), date.get(i));
			}
		}
		return bookNameAndReleaseDate;
	}

	/**
	 * This method takes only the date from input Map and add it to list and find
	 * the recent date and returns it.
	 * 
	 * @param BookNameReleaseDate Contain bookName along with release date
	 * @param dates               empty add only the dates
	 * @return latest release date
	 * @throws ParseException
	 */
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

	/**
	 * This method will search the map with the search value.
	 * 
	 * @param inputMap    Map to be searched
	 * @param searchValue value to be searched in the map
	 * @param basedOn     on what basis it has to search
	 * @return the matched value
	 */
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

	/**
	 * This method is used to verify the actual and expected result and create a
	 * entry in the report.
	 * 
	 * @param actual   actual value
	 * @param expected expected value
	 */
	public void verifyActualAndExpectedValue(String actual, String expected) {

		if (actual.equals(expected)) {
			reportRequest("The actual \"" + actual + "\" matches with the expected \"" + expected + "\"", "PASS");
		} else {
			reportRequest("The actual \"" + actual + "\" doesn't matches with the expected \"" + expected + "\"",
					"FAIL");
		}
	}

	/**
	 * This method is used to send a request with each book Id in the list and store
	 * the corresponding Url and description of that book into a Map.
	 * 
	 * @param idList    List of id associated to each book
	 * @param outputMap to create a map with book url and description
	 * @return the created Map with book url and description
	 */
	public Map<String, String> getDescriptionAndBookLinkUsingID(List<String> idList, Map<String, String> outputMap) {
		for (int i = 0; i < idList.size(); i++) {
			Response response = getWithParam("key", "6lrYcIJMFj37UMKUHz4A", "book/show/" + idList.get(i) + ".xml");

			XmlPath xmlResponse = response.xmlPath();
			outputMap.put(xmlResponse.getString("GoodreadsResponse.book.url"),
					xmlResponse.getString("GoodreadsResponse.book.description"));

		}
		return outputMap;
	}

	/**
	 * This method is used to send a request with each book Id in the list and get
	 * the shelf in which each book is available and check whether any book is
	 * available in booker-prize if it is available it will return it.
	 * 
	 * @param idList List of id associated to each book
	 * @return book that won Booker Prize
	 */
	public String getBookerPrizerWinnerBook(List<String> idList) {

		String bookerPrizeBook = new String();
		for (int i = 0; i < idList.size(); i++) {

			Response response = getWithParam("key", "6lrYcIJMFj37UMKUHz4A", "book/show/" + idList.get(i) + ".xml");
			XmlPath xmlResponse = response.xmlPath();
			List<String> shelfs = xmlResponse.getList("GoodreadsResponse.book.popular_shelves.shelf.@name");
			String bookName = xmlResponse.get("GoodreadsResponse.book.title").toString();
			for (int j = 0; j < shelfs.size(); j++) {

				if (shelfs.get(j).equals("booker-prize")) {
					bookerPrizeBook = bookName;

				}
			}
		}
		return bookerPrizeBook;
	}

	/**
	 * This method check if the value is null or not and if it is not creates a
	 * entry in report.
	 * 
	 * @param value value to be checked for null
	 */
	public void checkForNullValue(String value) {
		if (value.equals("null")) {
			reportRequest("The value is null", "FAIL");
		}
	}

}
