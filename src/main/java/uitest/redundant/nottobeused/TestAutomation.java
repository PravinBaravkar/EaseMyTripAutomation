package uitest.redundant.nottobeused;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestAutomation {

	enum Months {
		January, February, March, April, May, June, July, August, September, October, November, December
	}

	public WebDriver driver;
	String source, destination, travelDate;
	WebDriverWait wait;
	LinkedHashMap<String, Double> dayPrice = new LinkedHashMap<String, Double>();

	public static HashMap<String, Double> getSortedMapByValues(HashMap<String, Double> hashMap) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hashMap.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> map1, Map.Entry<String, Double> map2) {
				return (map1.getValue()).compareTo(map2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> sortedEntry : list) {
			sortedMap.put(sortedEntry.getKey(), sortedEntry.getValue());
		}
		return sortedMap;
	}

	public static void printMapContents(HashMap<String, Double> hashMap) {

		for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
			System.out.println("Day " + entry.getKey() + " : Price " + entry.getValue());
		}
	}

	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver",
				".\\src\\main\\java\\external\\chromedriver.exe");

		System.setProperty("webdriver.gecko.driver", ".\\src\\main\\java\\uitest\\sample\\automation\\geckodriver.exe");
		driver = new ChromeDriver();

		source = "Pune";
		destination = "Bangalore";
		travelDate = "23/08/2020";

		wait = new WebDriverWait(driver, 15);

	}

	@Test
	public void executeTest() throws InterruptedException {

		try {
			driver.get("https://www.easemytrip.com/");
			driver.manage().window().maximize();

			WebElement from = driver.findElement(By.xpath("//div[14]//div[2]//div[3]//div[1]//div[1]//input[2]"));
			WebElement to = driver.findElement(By.xpath("//div[14]//div[2]//div[3]//div[1]//div[2]//input[2]"));
			WebElement fromAutoSuggestion = driver.findElement(By.xpath("/html/body/ul[1]"));
			WebElement toAutoSuggestion = driver.findElement(By.xpath("/html/body/ul[2]"));
			WebElement departureDate = driver.findElement(By.xpath("//*[@id='ddate']"));

			from.click();
			from.sendKeys(source);

			wait.until(ExpectedConditions.visibilityOf(fromAutoSuggestion));
			fromAutoSuggestion.click();
			// from.sendKeys(Keys.ENTER);

			System.out.println("Source city entered is : " + source);

			to.click();
			to.sendKeys(destination);

			wait.until(ExpectedConditions.visibilityOf(toAutoSuggestion));
			toAutoSuggestion.click();
			// to.sendKeys(Keys.ENTER);

			System.out.println("Destination city entered is : " + destination);

			WebElement cal = driver.findElement(
					By.xpath("//div[14]//div[2]//div[3]//div[1]//div[3]//div[2]//div//div[1]//div//div[1]//div[2]"));
			if (!cal.isDisplayed()) {
				wait.until(ExpectedConditions.elementToBeClickable(departureDate));
				departureDate.click();
			}

			Thread.sleep(3000);

			String[] dateArray = travelDate.split("/");

			String month = dateArray[1];
			String year = dateArray[2];

			JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
			for (int d = 1; d <= 31; d++) {

				String day = String.format("%02d", d);

				String dayElement = day + "/" + month + "/" + year;

				String price = (String) javascriptExecutor.executeScript("var fare = document.evaluate(\"//*[@id='"
						+ dayElement
						+ "']/text()\", document, null, XPathResult.STRING_TYPE, null);return fare.stringValue;");

				dayPrice.put(day, Double.parseDouble(price));
			}

			HashMap<String, Double> sortedData = getSortedMapByValues(dayPrice);
			printMapContents(sortedData);

			Double min = Collections.min(sortedData.values());

			System.out.println("Lowest fare available in month " + month + " is " + min + " on below dates :");

			for (Map.Entry<String, Double> record : sortedData.entrySet()) {
				if (record.getValue().equals(min)) {
					System.out.println("Day " + record.getKey() + " : Price " + record.getValue());
				}
			}

		} catch (NoSuchElementException elementException) {
			System.out.println("Element not found" + elementException.getMessage());
		} catch (TimeoutException timeoutException) {
			System.out.println("Timed out.." + timeoutException.getMessage());
		}
	}

	@AfterTest
	public void tearDown() {
		driver.close();
	}
}
