# GoodReadsApp
Using this app can search the name of the book based on various inputs.

## Tech/framework used
##### Build with
  * Maven
  * TestNG
  * Extent Reports
  
## API Reference
https://www.goodreads.com/api
  
## code Example
```java
Response response = getWithParam("q", searchQuery, "key", "6lrYcIJMFj37UMKUHz4A", "search/index.xml");
		verifyResponseCode(response, 200);
		List<String> idList = getListWithKey(response, "GoodreadsResponse.search.results.work.best_book.id");
		
		String bookPrizerWinnerBook = getBookerPrizerWinnerBook(idList);
		reportRequest("The Booker Prize winner book is \"" + bookPrizerWinnerBook +"\"", "PASS");
		verifyActualAndExpectedValue(bookPrizerWinnerBook, expectedBookName);
```

## Tests
For testing we need to load the test data into the 
