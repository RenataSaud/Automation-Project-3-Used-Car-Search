import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsedCarSearch {
    @Test
    public void carGurus() throws InterruptedException, IOException {

        // 1. Launch Chrome browser and navigate to cargurus.com
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://www.cargurus.com/");
        String actualTitle = driver.getTitle(); // returns the Title of the page
        String expectedTitle = "Buy & Sell Cars: Reviews, Prices, and Financing - CarGurus";
        Assert.assertEquals(actualTitle, expectedTitle, "Titles are not matching, this may be not the desired page.");

        // 2.Click on Buy Used
        driver.findElement(By.xpath("//label[contains(. ,'Buy Used')]")).click();
        Assert.assertTrue(driver.getPageSource().contains("Used Cars"));

        // 3.Verify that the default selected option in Makes dropdown is All Makes
        WebElement makesElement = new Select(driver.findElement(By.id("carPickerUsed_makerSelect"))).getFirstSelectedOption();
        Assert.assertEquals(makesElement.getText(), "All Makes", "Default selected option");

        // 4. In Makes dropdown, choose Lamborghini
        new Select(driver.findElement(By.id("carPickerUsed_makerSelect"))).selectByVisibleText("Lamborghini");

        // 5. Verify that the default selected option in Models dropdown is All Models
        WebElement modelsElement = new Select(driver.findElement(By.id("carPickerUsed_modelSelect"))).getFirstSelectedOption();
        Assert.assertEquals(modelsElement.getText(), "All Models", "Default selected option");

        // 6. Verify that Models dropdown options are [All Models, Aventador, Huracan, Urus,
        //400GT, Centenario, Countach, Diablo, Espada, Gallardo, Murcielago]
        List<String> expexctedModelList = List.of("All Models", "Aventador","Gallardo", "Huracan", "Urus", "400GT",
                "Centenario", "Countach", "Diablo", "Espada", "Murcielago");
        List<String> actualModelList = new ArrayList<>();
        List<WebElement> modelOptions = new Select(driver.findElement(By.id("carPickerUsed_modelSelect"))).getOptions();

        for (WebElement each : modelOptions) {
            actualModelList.add(each.getText());
        }
        Assert.assertEquals(actualModelList, expexctedModelList, "The lists are not equal.");

        // 7. In Models dropdown, choose Gallardo
        new Select(driver.findElement(By.id("carPickerUsed_modelSelect"))).selectByVisibleText("Gallardo");

        // 8. Enter 22031 for zip and hit search
        driver.findElement(By.id("dealFinderZipUsedId_dealFinderForm")).sendKeys("22031", Keys.ENTER);

        // 9. In the results page, verify that there are 15 search results, excluding the first sponsored result
        List<WebElement> searchResults = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]"));
        if (searchResults.size() == 0) {
            throw new RuntimeException("List is empty");
        } else {
            System.out.println("There are " + searchResults.size() + " search results in this page.");
        }
        Assert.assertEquals(searchResults.size(), 15);

        // 10. Verify that all 15 result's title text contains "Lamborghini Gallardo"
        for (WebElement result : searchResults) {
            String title = result.getText();
            Assert.assertTrue(title.contains("Lamborghini Gallardo"));
        }

        // 11. From the dropdown on the left corner choose “Lowest price first” option and verify that
        //all 15 results are sorted from lowest to highest. You should exclude the first result since
        //it will not be a part of sorting logic.
        //To verify correct sorting, collect all 15 prices into a list, create a copy of it and sort in
        //ascending order and check the equality of the sorted copy with the original.
        Select sortDropdown = new Select(driver.findElement(By.xpath("//select[@id='sort-listing']")));
        sortDropdown.selectByVisibleText("Lowest price first");
        List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='cg-dealFinder-result-stats']/div[2]/div"));
        List<Integer> prices = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            prices.add(Integer.parseInt(priceElement.getText().replaceAll("[$,]", "")));
        }
        List<Integer> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        Assert.assertEquals(prices, sortedPrices, "Results are not sorted by lowest price first.");

        // 12. From the dropdown menu, choose “Highest mileage first” option and verify that all 15 results are sorted from highest to lowest.
        // You should exclude the first result since it will not be a part of sorting logic
        sortDropdown.selectByVisibleText("Highest mileage first");
        List<WebElement> mileageElements = driver.findElements(By.xpath("//span[@class='cg-dealFinder-result-stats']/div[1]/div"));
        List<Integer> mileages = new ArrayList<>();
        for (WebElement mileageElement : mileageElements) {
            mileages.add(Integer.parseInt(mileageElement.getText().replaceAll("[,]", "")));
        }
        List<Integer> sortedMileages = new ArrayList<>(mileages);
        Collections.sort(sortedMileages, Collections.reverseOrder());
        Assert.assertEquals(mileages, sortedMileages, "Results are not sorted by highest mileage");

        // 13. On the left menu, click on Coupe AWD checkbox and verify that all results on the page contains “Coupe AWD"
        driver.findElement(By.xpath("//fieldset[5]//ul[1]//li[1]//label[1]//p[1]")).click();
        List<WebElement> resultTitleElements = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]//h4[contains(@class, 'cg-listingDetail-model')]"));
        for (WebElement resultTitleElement : resultTitleElements) {
            String resultTitle = resultTitleElement.getText().toLowerCase();
            Assert.assertTrue(resultTitle.contains("lamborghini gallardo") && resultTitle.contains("coupe awd"));
        }
        // 14. Click on the last result (get the last result dynamically, i.e., your code should click on the last result regardless of how many results are there
        List<WebElement> lastResult = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]"));
        WebElement lastCheck =  lastResult.get(lastResult.size()-1);
        lastCheck.click();

        // 15. Once you are in the result details page go back to the results page and verify that the clicked result has “Viewed” text on it
        driver.navigate().back();
        List<WebElement> viewed = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]//span[@class='cg-dealFinder-result-viewed']"));
        for (WebElement view : viewed) {
            String viewText = view.getText();
            Assert.assertTrue(viewText.contains("Viewed"));
        }

        driver.close();
    }
}