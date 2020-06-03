package org.goodReads.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.goodReads.dataProvider.DataInputProvider;
import org.goodReads.reporter.HTMLReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import io.restassured.RestAssured;


public class PreAndTest extends HTMLReporter {
	
	public String dataFileName;
	
	@BeforeSuite
	public void beforeSuite() {
		startReport();
	}
	
	@BeforeClass
	public void beforeClass() {
		startTestCase(testCaseName, testDescription);		
	}
	
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException, IOException {
		
		svcTest = startTestModule(nodes);
		svcTest.assignAuthor(authors);
		svcTest.assignCategory(category);
		
		Properties prop = new Properties();
		prop.load(new FileInputStream(new File("./src/test/resources/config.properties")));
		
		RestAssured.baseURI = "https://"+prop.getProperty("server")+"/";
		

	}
	
	@DataProvider(name="fetchData")
	public  Object[][] getData(){
		
			return DataInputProvider.getSheet(dataFileName);	
	}
	
	@AfterSuite
	public void afterSuite() {
		endResult();
	}
}
