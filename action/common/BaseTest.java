package common;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	private WebDriver driver;
	protected final Log log;
	
	@BeforeSuite
	public void initBeforeSuite() {
		deleteAllureReport();
	}

	protected BaseTest() {
		this.log = LogFactory.getLog(getClass());
	}
	
	protected WebDriver getBrowserDriverID(String browserName, String appURL) {
		return getBrowserDriver(browserName,appURL);
	}
	
	private WebDriver getBrowserDriver(String browserName, String appURL) {
		  if(browserName.equals("edge")) {
			  WebDriverManager.edgedriver().setup();
			  driver = new EdgeDriver();
		  } else if(browserName.equals("firefox")){
			  WebDriverManager.firefoxdriver().setup();
			  driver = new FirefoxDriver();
		  } else if(browserName.equals("h_firefox")) {
			  FirefoxOptions options = new FirefoxOptions();
			  options.addArguments("--headless");
			  options.addArguments("window-size=1920x1080");
			  WebDriverManager.firefoxdriver().setup();
			  driver = new FirefoxDriver(options);
		  }
		  else if(browserName.equals("chrome")){
			  WebDriverManager.chromedriver().setup();
			  driver = new ChromeDriver();
		  }else if(browserName.equals("h_chrome")) {
			  ChromeOptions options = new ChromeOptions();
			  options.addArguments("--headless");
			  options.addArguments("window-size=1920x1080");
			  WebDriverManager.chromedriver().setup();
			  driver = new ChromeDriver(options);
		  }else if(browserName.equals("ie")) {
			  WebDriverManager.iedriver().arch32().setup();
			  driver = new InternetExplorerDriver();
		  }
		  else {
			  throw new RuntimeException("Browser Name invalid");
		  }
		  driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		  driver.manage().window().maximize();
		  driver.get(appURL);
		  return driver;
	}
	protected int generaterFakeNumber() {
		  Random ran = new Random();
		  return ran.nextInt(9999);
	  }
	public WebDriver getDriverInstance () {
		return this.driver;
	}
	
	public void deleteAllureReport() {
		try {
			//String workingDir = System.getProperty("user.dir");
			String pathFolderDownload = GlobalConstants.PROJECT_PATH + "/allure-json";
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			for(int i = 0; i<listOfFiles.length; i++) {
				if(listOfFiles[i].isFile()) {
					new File(listOfFiles[i].toString()).delete();
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	protected void closeBrowserDriver() {
		String cmd = null;
		try {
			String osName = GlobalConstants.OS_NAME;
			log.info("OS name = " + osName);

			String driverInstanceName = driver.toString().toLowerCase();
			log.info("Driver instance name = " + driverInstanceName);

			String browserDriverName = null;

			if (driverInstanceName.contains("chrome")) {
				browserDriverName = "chromedriver";
			} else if (driverInstanceName.contains("internetexplorer")) {
				browserDriverName = "IEDriverServer";
			} else if (driverInstanceName.contains("firefox")) {
				browserDriverName = "geckodriver";
			} else if (driverInstanceName.contains("edge")) {
				browserDriverName = "msedgedriver";
			} else if (driverInstanceName.contains("opera")) {
				browserDriverName = "operadriver";
			} else {
				browserDriverName = "safaridriver";
			}

			if (osName.contains("window")) {
				cmd = "taskkill /F /FI \"IMAGENAME eq " + browserDriverName + "*\"";
			} else {
				cmd = "pkill " + browserDriverName;
			}

			if (driver != null) {
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			try {
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected String getCurrentDay() {
		DateTime nowUTC = new DateTime();
		int day = nowUTC.getDayOfMonth();
		if (day < 10) {
			String dayValue = "0" + day;
			return dayValue;
		}
		return String.valueOf(day);
	}

	protected String getCurrentMonth() {
		DateTime now = new DateTime();
		int month = now.getMonthOfYear();
		if (month < 10) {
			String monthValue = "0" + month;
			return monthValue;
		}
		return String.valueOf(month);
	}

	protected String getCurrentYear() {
		DateTime now = new DateTime();
		return String.valueOf(now.getYear());
	}

	protected String getToday() {
		return getCurrentDay() + "/" + getCurrentMonth() + "/" + getCurrentYear();
	}

}
