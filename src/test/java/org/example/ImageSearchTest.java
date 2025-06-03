package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageSearchTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://ltu-i0015n-2024-web.azurewebsites.net";

    private void login() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.name("username")).sendKeys("stina");
        driver.findElement(By.name("password")).sendKeys("fåGelskådning");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Search for photos')]")));
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        login();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void searchForValidKeyword() {
        WebElement input = driver.findElement(By.name("search_terms"));
        input.sendKeys("cat");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement resultsContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("searchResultsContainer")));
        assertTrue(resultsContainer.isDisplayed());
        assertTrue(driver.findElements(By.cssSelector("#searchResults img")).size() > 0);
    }

    @Test
    public void searchIsCaseInsensitive() {
        WebElement input = driver.findElement(By.name("search_terms"));
        input.sendKeys("EMPIRE");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement resultsContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("searchResultsContainer")));
        assertTrue(resultsContainer.isDisplayed());
        assertTrue(driver.findElements(By.cssSelector("#searchResults img")).size() > 0);
    }

    @Test
    public void searchWithInvalidKeyword() {
        WebElement input = driver.findElement(By.name("search_terms"));
        input.sendKeys("invalidsearch123456");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement noResultsMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("message-no-search-results")));
        assertTrue(noResultsMsg.isDisplayed());
    }

    @Test
    public void searchWithEmptyInput() {
        WebElement input = driver.findElement(By.name("search_terms"));
        input.clear();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Assumes input still exists and nothing crashes
        assertTrue(driver.findElement(By.name("search_terms")).isDisplayed());
    }

    @Test
    public void searchWithOnlyWhitespace() {
        WebElement input = driver.findElement(By.name("search_terms"));
        input.sendKeys("   "); // three spaces
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("searchErrorMessage")));
        assertTrue(errorMessage.getText().contains("Search query is required."));
    }
}
