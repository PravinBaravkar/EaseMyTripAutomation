package uitest.easemytrip.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import uitest.easemytrip.pages.HomePage;


public class TestEaseMyTrip {

	WebDriver driver;

	@BeforeTest
	public void setup() {
		System.setProperty("webdriver.chrome.driver", ".\\src\\main\\java\\external\\chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		driver.get("https://www.easemytrip.com/");
		driver.manage().window().maximize();
	}

	@Test
	public void findLowestPriceFromMonth() throws InterruptedException {

		try {

			HomePage homePage = new HomePage(driver);

			homePage.enterSourceCity();

			homePage.enterDestinationCity();

			homePage.openCalendarForDeparturedate();

			homePage.getLowestFaresForMonth();

		} catch (NoSuchElementException elementException) {
			System.out.println("Element not found" + elementException.getMessage());
		} catch (TimeoutException timeoutException) {
			System.out.println("Timed out.." + timeoutException.getMessage());
		} catch (WebDriverException webdriverException) {
			webdriverException.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() throws InterruptedException {
		Thread.sleep(10000);
		driver.close();
	}
}