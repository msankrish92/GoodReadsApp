package org.goodReads.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class Function2 {
public static void main(String[] args) {
	

	RestAssured.baseURI = "https://www.goodreads.com/"; 
	Response respone = RestAssured.given()
	.log().all()
	.queryParam("q", "Paulo Coelho")
	.queryParam("key", "6lrYcIJMFj37UMKUHz4A")
	.when()
	.get("search/index.xml")
	.then().log().all()
	.extract()
	.response();
	
	XmlPath xmlResponse = respone.xmlPath();
	
	List<String> idList = xmlResponse.getList("GoodreadsResponse.search.results.work.best_book.id");
	Map<String, String> bookNameAndDescription = new HashMap<String,String>(); 
	for(int i = 0; i<idList.size(); i++) {
		Response respone2 = RestAssured.given()
				.log().all()
				.queryParam("key", "6lrYcIJMFj37UMKUHz4A")
				.when()
				.get("book/show/"+idList.get(i)+".xml")
				.then().log().all()
				.extract()
				.response();
		xmlResponse = respone2.xmlPath();
		bookNameAndDescription.put(xmlResponse.getString("GoodreadsResponse.book.url"), xmlResponse.getString("GoodreadsResponse.book.description"));
		
	}
	
	for(Map.Entry<String, String> a:bookNameAndDescription.entrySet()) {
		if(a.getValue().contains("to travel in search of a worldly treasure")) {
			System.out.println("The book that contains this string is " + a.getKey());
		}
	}
	
	
	
	
	
}
}