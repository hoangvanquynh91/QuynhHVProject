package uk;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.BaseTest;

public class kmk007 extends BaseTest {
	WebDriver driver;
	
		  
	  @Parameters({"browser",""})
	  @BeforeClass
	  public void beforeClass(String browserName, String url) {
		  driver = getBrowserDriverID(browserName, url);

	  }
	  @Test
	  public void User_01_Register (){

		  log.info("Register - step 01 - Navigate to register page");
		
	  }
	  @Test
	  public void User_02_Login () {
		
		  
	  }
	  
	  @AfterClass
	  public void afterClass() {
		  driver.quit();
	  }
	  


}
