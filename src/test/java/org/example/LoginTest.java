package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String LOGIN_URL = "https://ltu-i0015n-2024-web.azurewebsites.net/login";

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void loginWithValidUser_Stina() {
        driver.get(LOGIN_URL);
        driver.findElement(By.name("username")).sendKeys("stina");
        driver.findElement(By.name("password")).sendKeys("fåGelskådning");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.getPageSource().contains("Search for photos"));

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(text(),'Search for photos')]")
                )
        );
        assertTrue(heading.isDisplayed());
    }

    @Test
    public void loginWithValidUser_Johan() {
        driver.get(LOGIN_URL);
        driver.findElement(By.name("username")).sendKeys("johan");
        driver.findElement(By.name("password")).sendKeys("FotoGrafeRing1!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(text(),'Search for photos')]")
                )
        );
        assertTrue(heading.isDisplayed());
    }

    @Test
    public void loginWithInvalidPassword() {
        driver.get(LOGIN_URL);
        driver.findElement(By.name("username")).sendKeys("stina");
        driver.findElement(By.name("password")).sendKeys("wrongPassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p[contains(text(),'Invalid username or password')]")
                )
        );
        assertTrue(heading.isDisplayed());
    }

    @Test
    public void loginWithUnknownUsername() {
        driver.get(LOGIN_URL);
        driver.findElement(By.name("username")).sendKeys("nonexistentuser");
        driver.findElement(By.name("password")).sendKeys("somePassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p[contains(text(),'Invalid username or password')]")
                )
        );
        assertTrue(heading.isDisplayed());
    }

    @Test
    public void loginWithEmptyFields() {
        driver.get(LOGIN_URL);
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        WebElement usernameInput = driver.findElement(By.name("username"));
        assertTrue(usernameInput.getAttribute("validationMessage") != null);
    }

    @Test
    public void loginWithUsernameOnly() {
        driver.get(LOGIN_URL);
        driver.findElement(By.name("username")).sendKeys("stina");
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        WebElement passwordInput = driver.findElement(By.name("password"));
        assertTrue(passwordInput.getAttribute("validationMessage") != null);
    }

    @Test
    public void loginWithPasswordOnly() {
        driver.get(LOGIN_URL);
        driver.findElement(By.name("password")).sendKeys("fåGelskådning");
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        WebElement usernameInput = driver.findElement(By.name("username"));
        assertTrue(usernameInput.getAttribute("validationMessage") != null);
    }
}
