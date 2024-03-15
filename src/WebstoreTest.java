import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebstoreTest {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// Set up WebDriver
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Navigate to the website
		driver.get("https://www.webstaurantstore.com/");

		// Search for 'stainless work table'
		searchForProduct(driver, "stainless work table");

		// Check the search result ensuring every product has the word 'Table' in its title
		checkProductTitlesInEachPage(driver);
//		if (checkProductTitlesInEachPage(driver)) {
//			System.out.println("Not all product title contain the word table in it. Exiting the test!!!");
//			driver.quit();
//			return;
//		}

		// Add the last item to the cart+
		addToCart(driver);

		// Click on the cart
		clickOnCart(driver);

		// Click on Empty the cart
		clickOnEmptyCart(driver);

		// Close the browser
		driver.quit();
	}

	// Method to search for a product
	private static void searchForProduct(WebDriver driver, String productName) {
		WebElement searchBox = driver.findElement(By.id("searchval"));
		searchBox.sendKeys(productName);
		searchBox.submit();
	}

	// Method to iterate all the pages
	private static boolean checkProductTitlesInEachPage(WebDriver driver) {
		WebElement pagingDivElement = driver.findElement(By.xpath("//div[@id='paging']"));
		List<WebElement> liItems = pagingDivElement.findElements(By.tagName("li"));
		WebElement lastPageLiItem = liItems.get(liItems.size() - 2);
		WebElement lastAnchorTag = lastPageLiItem.findElement(By.tagName("a"));
		System.out.println("No of pages : " + lastAnchorTag.getText());
		int numberOfPages = Integer.valueOf(lastAnchorTag.getText());
		System.out.println("currently in page 1");
		checkProductTitles(driver);
//		if (checkProductTitles(driver))
//			return true;

		for (int i = 1; i < numberOfPages; i++) {
			System.out.println("going to page " + (i + 1));
			goToNextPage(driver);
			checkProductTitles(driver);
//			if (checkProductTitles(driver))
//				return true;
		}

		return false;
	}

	// Method to go to the next page of pagination
	private static void goToNextPage(WebDriver driver) {
		WebElement nextPageLiItem = driver.findElement(By.xpath("//div[@id='paging']//li[last()]"));
		nextPageLiItem.click();
	}

	// Method to check product titles
	private static boolean checkProductTitles(WebDriver driver) {
		List<WebElement> productsOnLastPage = driver.findElements(By.cssSelector("#product_listing"));
		WebElement productList = productsOnLastPage.get(0);

		List<WebElement> productDivElements = productList.findElements(By.xpath("./div"));
		for (WebElement productDiv : productDivElements) {
			List<WebElement> spanElements = productDiv.findElements(By.xpath(".//span"));
		
			WebElement spanElement = spanElements.get(0);
			if (!spanElement.getText().toLowerCase().contains("table")) {
				System.out.println("Product title doesn't contain 'Table': " + spanElement.getText());
				return true;
			}
		}

		return false;
	}

	// Method to add the last item to the cart
	private static void addToCart(WebDriver driver) {
		List<WebElement> productsOnLastPage = driver.findElements(By.cssSelector("#product_listing"));
		WebElement productList = productsOnLastPage.get(0);

		List<WebElement> childDivElements = productList.findElements(By.xpath("./div"));
		WebElement lastElement = childDivElements.get(childDivElements.size() - 1);
		System.out.println(" Last item is : " + lastElement.getText());

		WebElement addToCartButton = lastElement
				.findElement(By.cssSelector("input[type='submit'][name='addToCartButton']"));
		addToCartButton.click();
	}

	// Method to click on the cart
	private static void clickOnCart(WebDriver driver) {
		WebElement cart = driver.findElement(By.xpath("//a[@href='/cart/']"));
		cart.click();
	}

	// Method to click on Empty the cart
	private static void clickOnEmptyCart(WebDriver driver) {
		// click on Empty the cart
		WebElement EmptyCart = driver
				.findElement(By.cssSelector("button.emptyCartButton.btn.btn-mini.btn-ui.pull-right[type='button']"));
		EmptyCart.click();

		// clicking on Empty cart pop up
		WebElement EmptyCartPopup = driver.findElement(By.cssSelector(
				"button.border-solid.border.box-border.cursor-pointer.inline-block.text-center.no-underline.antialiased.focus-visible\\:outline.focus-visible\\:outline-offset-2.focus-visible\\:outline-2.mr-2.rounded-normal.text-base.leading-normal.px-7.py-2-1\\/2.hover\\:bg-green-800.text-white.text-shadow-black-60.bg-green-500.border-black-10.focus-visible\\:outline-green-800.btn.align-middle.font-semibold[type='button']"));
		EmptyCartPopup.click();

	}
}
