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
1. For testing we need to load the test data into the below excel file based on the requirement.<br>
![alt text](https://github.com/msankrish92/GoodReadsApp/blob/master/readmeImages/Capture.PNG?raw=true) <br>
2. Then we need to open the testng.xml and run the test as TestNg suite there<br>
![alt text](https://github.com/msankrish92/GoodReadsApp/blob/master/readmeImages/Capture2.PNG?raw=true) <br>
3. After the job completes you can see report in console like <br>
![alt text](https://github.com/msankrish92/GoodReadsApp/blob/master/readmeImages/Capture3.PNG?raw=true)
4. Html report is also generated in under reports folder. <Br>
![alt text](https://github.com/msankrish92/GoodReadsApp/blob/master/readmeImages/Capture4.PNG?raw=true)

### Sample Report
![alt text](https://github.com/msankrish92/GoodReadsApp/blob/master/readmeImages/Capture5.PNG?raw=true)