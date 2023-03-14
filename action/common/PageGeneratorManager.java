package common;

import org.openqa.selenium.WebDriver;

import uk.CCG007PageObject;
import uk.KMK007PageObject;

public class PageGeneratorManager {
	public static CCG007PageObject getCCG007Page(WebDriver driver) {
		return new CCG007PageObject(driver);
	}
	public static KMK007PageObject getKMK007Page(WebDriver driver) {
		return new KMK007PageObject(driver);
	}
	
}
