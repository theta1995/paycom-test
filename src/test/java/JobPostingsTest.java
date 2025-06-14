import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class JobPostingsTest {

    WebDriver driver;
    WebDriverWait wait;
    String url;

    @Parameters({"URL"})
    @BeforeMethod
    public void setUp(String URL) {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        url = URL;
    }

    @AfterMethod
    public void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyJobNotFound() {
        driver.get(url);
        driver.findElement(By.xpath("//a[text()='careers']")).click();
        String careerLink = driver.findElement(By.xpath("//a[text()='Search for jobs']")).getAttribute("href");

        Assert.assertNotNull(careerLink);

        driver.navigate().to(careerLink);
        wait.until(ExpectedConditions.titleIs("Job Opportunities"));

        driver.findElement(By.id("search-by-keyword")).sendKeys("Nonexisting");
        driver.findElement(By.xpath("//button //span[text()='Search']")).click();

        WebElement resultMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[text()='No results match your search']")));
        Assert.assertEquals(resultMessage.getText(), "No results match your search", "Unexpected result message");
    }
}
