package org.goodReads.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;

import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class Functions {
	
	
	public static void main(String[] args) throws ParseException {
		
		RestAssured.baseURI = "https://www.goodreads.com/"; 
		Response respone = RestAssured.given()
		.log().all()
		.queryParam("q", "J.k.rowling")
		.queryParam("key", "6lrYcIJMFj37UMKUHz4A")
		.when()
		.get("search/index.xml")
		.then().log().all()
		.extract()
		.response();
		
//		respone.prettyPrint();
		
		XmlPath xmlResponse = respone.xmlPath();
		
		List<String> publishedYear = new ArrayList<String>();
		List<String> publishedMonth = new ArrayList<String>();
		List<String> publishedDay = new ArrayList<String>();
		
		publishedYear = xmlResponse.getList("GoodreadsResponse.search.results.work.original_publication_year");
		publishedMonth = xmlResponse.getList("GoodreadsResponse.search.results.work.original_publication_month");
		publishedDay = xmlResponse.getList("GoodreadsResponse.search.results.work.original_publication_day");
		List<String> bookName = xmlResponse.getList("GoodreadsResponse.search.results.work.best_book.title");
		
		List<String> ModifiablePublishedMonth = new ArrayList<String>();
		List<String> ModifiablePublishedDay = new ArrayList<String>();
		
		for (int i = 0; i < publishedMonth.size(); i++) {
//			System.out.println(publishedMonth.get(i).length()==1);
//			System.out.println(publishedMonth.get(i));
			
			
			if(publishedMonth.get(i).length()==1&&!publishedMonth.get(i).equals("null")) {
				ModifiablePublishedMonth.add("0"+publishedMonth.get(i));
			}else {
				ModifiablePublishedMonth.add(publishedMonth.get(i));
			}
		}
		
		for (int i = 0; i < publishedDay.size(); i++) {
//			System.out.println(publishedDay.get(i).length()==1);
//			System.out.println(publishedDay.get(i));
			
			
			if(publishedDay.get(i).length()==1&&!publishedDay.get(i).equals("null")) {
				ModifiablePublishedDay.add("0"+publishedDay.get(i));
			}else {
				ModifiablePublishedDay.add(publishedDay.get(i));
			}
		}
		System.out.println(ModifiablePublishedDay);
		
		Map<String, String> books = new HashMap<String,String>();
		
		for (int i = 0; i < bookName.size(); i++) {
			System.out.println(publishedYear.get(i));
			System.out.println(ModifiablePublishedMonth.get(i));
			System.out.println(ModifiablePublishedDay.get(i));
//			if(publishedYear.get(i).equals("null")&&publishedMonth.get(i).equals("null")&&publishedDay.get(i).equals("null"))
//			books.put(bookName.get(i), publishedYear.get(i) + "-" + publishedMonth.get(i) + "-" + publishedDay.get(i));
		
			if(!publishedYear.get(i).equals("null"))
			{
				if(!ModifiablePublishedMonth.get(i).equals("null")) {
					if(!ModifiablePublishedDay.get(i).equals("null")) {
						books.put(bookName.get(i), publishedYear.get(i) + "-" + ModifiablePublishedMonth.get(i) + "-" + ModifiablePublishedDay.get(i));
					}
				}
			}
		}
		System.out.println(books);
		List<Date> dates = new ArrayList<Date>();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		for(Map.Entry<String, String> a:books.entrySet()) {
			System.out.println(a.getValue() + "-->" +a.getKey());
			Date date = fmt.parse(a.getValue());
			dates.add(date);
		}
		
		System.out.println(dates);
//		Date date = fmt.parse("2013-07-06");
//		Date dateOne = fmt.parse("2017-05-06");
//		Date dateTwo = fmt.parse("2001-01-06");
//		
//		dates.add(date);
//		dates.add(dateOne);
//		dates.add(dateTwo);
		
		System.out.println(Collections.max(dates));
		
		Date recentReleasedBookDate = Collections.max(dates);
		
		String dateStr = recentReleasedBookDate.toString();
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date1 = (Date)formatter.parse(dateStr);
		System.out.println(date1);   
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +         cal.get(Calendar.DATE);
//		System.out.println("formatedDate : " + formatedDate); 
//		String key = "formatedDate : " + formatedDate;
		for (Map.Entry<String, String> a:books.entrySet()) {
//			System.out.println(a.getValue());
			if(a.getValue().equals(formatedDate)) {
				System.out.println(a.getKey());
				break;
			}
		}
		
		
	}

}
