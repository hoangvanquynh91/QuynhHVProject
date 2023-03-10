package common;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {
	public static BasePage getBasePageObject() {
		return new BasePage();
	}
	//Mở 1 URL
	//Common fuction
	public void openPageUrl(WebDriver driver, String pageUrl) {
		driver.get(pageUrl);
	}
	
	public String getPageTitle(WebDriver driver) {
		return driver.getTitle();
	}
	public String getPageUrl(WebDriver driver) {
		return driver.getCurrentUrl();
	}
	public String getPageSourceCode(WebDriver driver) {
		return driver.getPageSource();
	}
	
	public void backToPage(WebDriver driver) {
		driver.navigate().back();
	}
	public void forwardToPage(WebDriver driver) {
		driver.navigate().forward();
	}
	public void refreshCurrentPage(WebDriver driver) {
		driver.navigate().refresh();
	}
	public Set<Cookie> getAllCookie(WebDriver driver) {
		return driver.manage().getCookies();
	}
	public void setCookie(WebDriver driver, Set<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			  driver.manage().addCookie(cookie);
		  }
		sleepInSecond(3);
	}
	public Alert waitForAlertPresence(WebDriver driver) {
		WebDriverWait explicitWait = new WebDriverWait(driver,30);
		return explicitWait.until(ExpectedConditions.alertIsPresent());
	}
	public void acceptAlert(WebDriver driver) {
		Alert alert =  waitForAlertPresence(driver);
		alert.accept();
	}
	public void cancelAlert(WebDriver driver) {
		waitForAlertPresence(driver).dismiss();
	}
	
	public String getAlertText(WebDriver driver) {
		return waitForAlertPresence(driver).getText();
	}
	public void sendKeyToAlert(WebDriver driver, String textValue) {
		waitForAlertPresence(driver).sendKeys(textValue);
	}
	public void switchToWindowByID(WebDriver driver,String otherID) {
		Set<String> allWindownIDs = driver.getWindowHandles();
		
		for (String id : allWindownIDs) {
			if(!id.equals(otherID)) {
				driver.switchTo().window(id);
			}
		}
	
	}
    public void switchToWindownByPageTitle(WebDriver driver,String expectedPageTitle) {
    	Set<String> allWindownTitle = driver.getWindowHandles();
    	for (String id : allWindownTitle) {
			driver.switchTo().window(id);
			String actualTitle = driver.getTitle();
			if(actualTitle.equals(expectedPageTitle)) {
				break;
			}
			
		}
    }
    public void closeWindownWithoutParent(WebDriver driver,String parentID) {
    	Set<String> allWindownID = driver.getWindowHandles();
    	
    	for (String id : allWindownID) {
    		if(!id.equals(parentID)) {
    			driver.switchTo().window(id);
        		driver.close();
    		}
		}
    	driver.switchTo().window(parentID);
    }
    private By getByLocator(String locatorType) {
    	By by = null;
    	if(locatorType.startsWith("id=") || locatorType.startsWith("ID=") || locatorType.startsWith("Id=")) {
    		by = By.id(locatorType.substring(3));
    	} else if(locatorType.startsWith("css=") || locatorType.startsWith("CSS=") || locatorType.startsWith("Css=")) {
    		by = By.cssSelector(locatorType.substring(4));
    	} else if(locatorType.startsWith("xpath=") || locatorType.startsWith("XPATH=") || locatorType.startsWith("Xpath=")) {
    		by = By.xpath(locatorType.substring(6));
    	} else if(locatorType.startsWith("class=") || locatorType.startsWith("CLASS=") || locatorType.startsWith("Class=")) {
    		by = By.className(locatorType.substring(6));
    	} else if(locatorType.startsWith("name=") || locatorType.startsWith("NAME=") || locatorType.startsWith("Name=")) {
    		by = By.name(locatorType.substring(5));
    	} else {
    		throw new RuntimeException("Locator type is not support");
    	}
    	return by;
    }
    
    private String getDynamicXpath(String locator, String... dynamicValues) {
    	if(locator.startsWith("xpath=")||locator.startsWith("Xpath=")||locator.startsWith("XPATH=")) {
    		locator = String.format(locator,(Object[]) dynamicValues);
    		//System.out.println(locator);
    	}
    	return locator;
    }
    
    private WebElement getWebElement(WebDriver driver,String locator) {
    	return driver.findElement(getByLocator(locator));
    }
    public List<WebElement> getListWebElement(WebDriver driver,String locator) {
    	return driver.findElements(getByLocator(locator));
    	
    }
    public void clickToElement(WebDriver driver,String locator) {
    	getWebElement(driver,locator).click();
    	
    }
    public void clickToElement(WebDriver driver,String locator, String... dynamicValues) {
    	getWebElement(driver,getDynamicXpath(locator,dynamicValues)).click();
    	
    }
    public void sendkeyToElement(WebDriver driver,String locator, String textValue) {
    	WebElement element = getWebElement(driver,locator);
    	element.clear();
    	element.sendKeys(textValue);
    }
    public void sendkeyToElement(WebDriver driver,String locator, String textValue, String... dynamicValues) {
    	WebElement element = getWebElement(driver,getDynamicXpath(locator, dynamicValues));
    	element.clear();
    	element.sendKeys(textValue);
    }
    public void clearValueInElementByDeleteKey(WebDriver driver,String locator) {
    	WebElement element = getWebElement(driver,locator);
    	element.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
    }
    public void selectItemInDefaultDropdown(WebDriver driver,String locator, String textItem) {
    	Select select = new Select(getWebElement(driver,locator));
    	select.selectByVisibleText(textItem);
    }

    public void selectItemInDefaultDropdown(WebDriver driver,String locator, String textItem, String... dynamicValues) {
    	Select select = new Select(getWebElement(driver,getDynamicXpath(locator, dynamicValues)));
    	select.selectByVisibleText(textItem);
    }
    
    public String getSelectedItemDefaultDropDown(WebDriver driver,String locator) {
    	Select select = new Select(getWebElement(driver,locator));
    	return select.getFirstSelectedOption().getText();
    }
    
    public boolean isDropdownMultiple(WebDriver driver,String locator) {
    	Select select = new Select(getWebElement(driver,locator));
    	return select.isMultiple();
    }
    public void sleepInSecond (long timeSecond) {
		try {
			Thread.sleep(timeSecond * 1000);
		}catch (InterruptedException e ) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
    public void selectItemInDropdown(WebDriver driver,String parentLocator, String childLocator, String expectedItem) {
    	getWebElement(driver,parentLocator).click();
        sleepInSecond(2);
		WebDriverWait explicitWait = new WebDriverWait(driver,30);
		
		List<WebElement> allItem = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(childLocator)));
		for (WebElement item : allItem) {
			if (item.getText().trim().equals(expectedItem)) {
				((JavascriptExecutor) driver).executeScript("argument[0].scrollIntoView(true);", item);
				sleepInSecond(2);
				item.click();
				break;
			}
		}
		
	}
    public String getElementAttribute(WebDriver driver,String locator, String attributeName) {
    	return getWebElement(driver,locator).getAttribute(attributeName);
    }
    
    public String getElementAttribute(WebDriver driver,String locator, String attributeName, String ...dynamicLocator) {
    	return getWebElement(driver,getDynamicXpath(locator, dynamicLocator)).getAttribute(attributeName);
    }
    
    public String getElementText(WebDriver driver,String locator) {
    	return getWebElement(driver,locator).getText();
    }
    
    public String getElementText(WebDriver driver,String locator, String... dynamicValues) {
    	return getWebElement(driver,getDynamicXpath(locator, dynamicValues)).getText();
    }
	
    public String getElementCssValue(WebDriver driver,String locator,String propetyName) {
    	return getWebElement(driver,locator).getCssValue(propetyName);
    }
	public String getHexaColorFromRGBD(String rgbaValue) {
		return Color.fromString(rgbaValue).asHex();
	}
	
	public int getElementSize(WebDriver driver,String locator) {
		return getListWebElement(driver,locator).size();
	}
	public int getElementSize(WebDriver driver,String locator, String... dynamicValues) {
		return getListWebElement(driver,getDynamicXpath(locator, dynamicValues)).size();
	}
	public void checkToDefaultCheckBoxRadioButton(WebDriver driver,String locator) {
		WebElement element = getWebElement(driver, locator);
		if(!element.isSelected()) {
			element.click();
		}
	}
	public void uncheckToDefaultCheckBox(WebDriver driver,String locator) {
		WebElement element = getWebElement(driver, locator);
		if(element.isSelected()) {
			element.click();
		}
	}
	public void checkToDefaultCheckBoxRadioButton(WebDriver driver,String locator, String... dynamicValues) {
		WebElement element = getWebElement(driver, getDynamicXpath(locator, dynamicValues));
		if(!element.isSelected()) {
			element.click();
		}
	}
	public void uncheckToDefaultCheckBox(WebDriver driver,String locator, String... dynamicValues) {
		WebElement element = getWebElement(driver, getDynamicXpath(locator, dynamicValues));
		if(element.isSelected()) {
			element.click();
		}
	}
	public boolean isElementDisplay(WebDriver driver,String dynamicLocator, String... value) {
		return getWebElement(driver, getDynamicXpath(dynamicLocator, value)).isDisplayed();
	}
	public boolean isElementDisplay(WebDriver driver,String locator) {
		try {
			// Tìm thấy element
			// Case 1: Element có hiển thị -> Trả về true
			// Case 2: Element không hiển thị nhưng có trong DOM -> Trả về false
			return getWebElement(driver, locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
		
	}
	public void overrideGlobalTimeout(WebDriver driver, long timeOut) {
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	}
	
	public boolean isElementUnDisplayed(WebDriver driver, String locator) {
		List<WebElement> elements = getListWebElement(driver, locator);
		overrideGlobalTimeout(driver, 30);
		if(elements.size() == 0) {
			return true;
		} else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isElementUnDisplayed(WebDriver driver, String locator,  String... DynamicValue) {
		List<WebElement> elements = getListWebElement(driver, locator);
		overrideGlobalTimeout(driver, 30);
		if(elements.size() == 0) {
			return true;
		} else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isElementEnable(WebDriver driver,String locator) {
		return getWebElement(driver, locator).isEnabled();
	}
	public boolean isElementSelected(WebDriver driver,String locator) {
		return getWebElement(driver, locator).isSelected();
	}
	
	public void switchToFrame(WebDriver driver,String locator) {
		driver.switchTo().frame(getWebElement(driver, locator));
	}
    public void switchToDefaultContent(WebDriver driver) {
    	driver.switchTo().defaultContent();
    }
    public void hoverMouseToElement(WebDriver driver,String locator) {
    	Actions action = new Actions(driver);
    	action.moveToElement(getWebElement(driver, locator)).perform();
    }
    public void pressKeyToElement(WebDriver driver,String locator, Keys key) {
    	Actions action = new Actions(driver);
    	action.sendKeys(getWebElement(driver, locator),key).perform();
    }
    public void pressKeyToElement(WebDriver driver,String locator, Keys key, String...dynamicValue ) {
    	Actions action = new Actions(driver);
    	action.sendKeys(getWebElement(driver, getDynamicXpath(locator, dynamicValue)),key).perform();
    }
    
	public void scrollToBottomPage(WebDriver driver) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}
	public void highlightElement(WebDriver driver, String locator) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		WebElement element = getWebElement(driver, locator);
		String originalStyle = element.getAttribute("style");
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
		sleepInSecond(1);
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
	}

	public void clickToElementByJS(WebDriver driver, String locator) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].click();", getWebElement(driver, locator));
	}

	public void scrollToElement(WebDriver driver, String locator) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locator));
	}
	public void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(driver, locator));
	}
	public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
		WebDriverWait explicitWait = new WebDriverWait(driver, 20);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
			}
		};

		return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
	}
	public String getElementValidationMessage(WebDriver driver, String locator) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getWebElement(driver, locator));
	}

	public boolean isImageLoaded(WebDriver driver, String locator) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getWebElement(driver, locator));
		if (status) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isImageLoaded(WebDriver driver, String locator, String...dynamicLocator) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getWebElement(driver, getDynamicXpath(locator, dynamicLocator)));
		if (status) {
			return true;
		} else {
			return false;
		}
	}

	public void waitForElementVisble(WebDriver driver,String locator) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
	}
	public void waitForElementVisble(WebDriver driver,String locator, String... dynamicValues) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		//System.out.println("Locator wait"+locator);
		By by = getByLocator(getDynamicXpath(locator, dynamicValues));
		System.out.println("Locator wait"+by);
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(getDynamicXpath(locator, dynamicValues))));
		
	}
	public void waitForAllElementVisible(WebDriver driver,String locator) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(locator)));
	}
	public void waitForAllElementVisible(WebDriver driver,String locator, String... dynamicValues) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(getDynamicXpath(locator, dynamicValues))));
	}
	public void waitForElementInVisble(WebDriver driver,String locator) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
	}
	public void waitForElementInVisble(WebDriver driver,String locator, String... dynamicValues) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
	}
	/*
	 * Wait for Element undisplay or not in DOM and overide implicit time out
	 */
	public void waitForElementUndisplayed(WebDriver driver,String locator) {
		WebDriverWait explicitWait = new WebDriverWait(driver,5);
		overrideGlobalTimeout(driver, 5);
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
		overrideGlobalTimeout(driver, 30);
	}
	
	public void waitForAllElementInVisible(WebDriver driver,String locator) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, locator)));
	}
	
	public void waitForAllElementInVisible(WebDriver driver,String locator, String... dynamicLocator) {
		WebDriverWait explicitWait = new WebDriverWait(driver,20);
		explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, getDynamicXpath(locator, dynamicLocator))));
	}
	
	public void waitToElementClickable(WebDriver driver,String locator) {
		WebDriverWait explicitWait = new WebDriverWait(driver, 30);
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
	}
	public void waitToElementClickable(WebDriver driver,String locator, String... dynamicLocator) {
		WebDriverWait explicitWait = new WebDriverWait(driver, 30);
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(getDynamicXpath(locator, dynamicLocator))));
	}
	

}
