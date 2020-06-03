package org.goodReads.api;

import java.util.ArrayList;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class Function3 {

	public static void main(String[] args) {

		RestAssured.baseURI = "https://www.goodreads.com/";
		Response respone = RestAssured.given().log().all().queryParam("q", "The Test")
				.queryParam("key", "6lrYcIJMFj37UMKUHz4A").when().get("search/index.xml").then().log().all().extract()
				.response();

		XmlPath xmlResponse = respone.xmlPath();

		List<String> idList = xmlResponse.getList("GoodreadsResponse.search.results.work.best_book.id");
//		Map<String, String> bookNameAndDescription = new HashMap<String,String>(); 
		List<String> bookerPrizeBooks = new ArrayList<String>();
		for (int i = 0; i < idList.size(); i++) {
			Response respone2 = RestAssured.given().log().all().queryParam("key", "6lrYcIJMFj37UMKUHz4A").when()
					.get("book/show/" + idList.get(i) + ".xml")
//					.get("book/show/41081373.xml")
					.then().log().all().extract().response();
			xmlResponse = respone2.xmlPath();
			List<String> shelfs = xmlResponse.getList("GoodreadsResponse.book.popular_shelves.shelf.@name");
			System.out.println(shelfs);
			String bookName = xmlResponse.get("GoodreadsResponse.book.title").toString();

			for (int j = 0; j < shelfs.size(); j++) {
				System.out.println(shelfs.get(j));
				if (shelfs.get(j).equals("booker-prize")) {
					bookerPrizeBooks.add(bookName);
//					System.out.println("prize winner books are: " + bookName);
				}
			}

		}
		System.out.println(bookerPrizeBooks);
//		RestAssured.baseURI = "https://www.goodreads.com/"; 
//		Response respone = RestAssured.given()
//		.log().all()
//		.queryParam("key", "6lrYcIJMFj37UMKUHz4A")
//		.when()
//		.get("book/show/42975172.xml");
//		
//		respone.prettyPrint();
//		
//		XmlPath xmlResponse = respone.xmlPath();
//		
//		List<String> shelfs = xmlResponse.getList("GoodreadsResponse.book.popular_shelves.shelf.@name");
//		System.out.println(shelfs);
	}
}