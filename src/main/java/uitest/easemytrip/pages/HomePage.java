package uitest.easemytrip.pages;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import uitest.easymytrip.util.Utility;

public class HomePage {

	WebDriver driver;
	WebDriverWait wait;
	String month;
	Utility util = new Utility();
	JavascriptExecutor javascriptExecutor;;
	LinkedHashMap<String, Double> daywisePriceForEachMonth = new LinkedHashMap<String, Double>();

	private static final By source = By.xpath("//div[14]//div[2]//div[3]//div[1]//div[1]//input[2]");
	private static final By destination = By.xpath("//div[14]//div[2]//div[3]//div[1]//div[2]//input[2]");
	private static final By sourceAutoSuggestion = By.xpath("/html/body/ul[1]");
	private static final By destinationAutoSuggestion = By.xpath("/html/body/ul[2]");
	private static final By departureDate = By.xpath("//*[@id='ddate']");
	private static final By calendar = By
			.xpath("//div[14]//div[2]//div[3]//div[1]//div[3]//div[2]//div//div[1]//div//div[1]//div[2]");

	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.javascriptExecutor = (JavascriptExecutor) this.driver;
		this.wait = new WebDriverWait(driver, 15);
	}

	public void enterSourceCity() {
		enterCity("Pune", source, sourceAutoSuggestion);
	}

	public void enterDestinationCity() {
		enterCity("New Delhi", destination, destinationAutoSuggestion);
	}

	public void openCalendarForDeparturedate() {

		if (!(driver.findElement(calendar).isDisplayed())) {
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(departureDate)));
			driver.findElement(departureDate).click();
		}
	}

	public void enterCity(String city, By inputField, By autoSuggestionField) {
		driver.findElement(inputField).click();
		driver.findElement(inputField).sendKeys(city);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(autoSuggestionField)));
		driver.findElement(autoSuggestionField).click();
	}

	public void getFareForEachDayOfMonth() {
		String departureDate = "23/08/2020"; // We can give any date here as input in format dd/mm/yyyy

		month = util.getMonthFromdepartureDate(departureDate);
		int maxdays = util.getMaxDaysOfMonth(month);
		String year = util.getYearFromdepartureDate(departureDate);

		// Iterate for all days of month
		for (int d = 1; d <= maxdays; d++) {

			String day = String.format("%02d", d);

			/*
			 * xpath for all prices can be derived as its based on date in format dd/mm/yyyy
			 * hence we iterate though it and gets prices for particular date
			 */
			String dayElement = day + "/" + month + "/" + year;

			// Using javascript executor to retrieve price of each day
			String price = (String) javascriptExecutor
					.executeScript("var fare = document.evaluate(\"//*[@id='" + dayElement
							+ "']/text()\", document, null, XPathResult.STRING_TYPE, null);return fare.stringValue;");

			if (price.isEmpty())
				throw new ArithmeticException("JavaScript Executor failed to perform its operation");

			daywisePriceForEachMonth.put(day, Double.parseDouble(price));
		}
	}

	public void getLowestFaresForMonth() {

		getFareForEachDayOfMonth();

		HashMap<String, Double> sortedData = util.getSortedMapByValues(daywisePriceForEachMonth);

		util.printMapContents(sortedData);

		Double min = Collections.min(sortedData.values());

		System.out.println("Lowest fare available in month " + month + " is " + min + " on below dates :");

		for (Map.Entry<String, Double> record : sortedData.entrySet()) {
			if (record.getValue().equals(min)) {
				System.out.println("Day " + record.getKey() + " : Price " + record.getValue());
			}
		}
	}
}
