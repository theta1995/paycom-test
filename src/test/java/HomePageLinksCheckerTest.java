import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.testng.Assert.*;

public class HomePageLinksCheckerTest {

    WebDriver driver;
    String url;
    HttpClient httpClient;

    @Parameters({"URL"})
    @BeforeMethod
    public void setUp(String URL) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        url = URL;
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @AfterMethod
    public void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyFooterNavLinksAreNotBroken() {
        driver.get(url);
        WebElement footerNav = driver.findElement(By.id("footer-nav"));
        List<WebElement> anchorElements = footerNav.findElements(By.tagName("a"));
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

            List<CompletableFuture<Void>> futures = anchorElements.stream()
                    .map(anchor -> anchor.getAttribute("href"))
                    .filter(href -> href != null && href.startsWith("http"))
                    .map(href -> CompletableFuture.runAsync(() -> {
                        try {
                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(href))
                                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                                    .timeout(Duration.ofSeconds(10))
                                    .build();
                            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
                            int statusCode = response.statusCode();
                            assertTrue(statusCode < 400 || statusCode == 403, "Broken link found: " + href + " returned status code " + statusCode);
                        } catch (Exception e) {
                            fail("Error checking link " + href + ": " + e.getMessage());
                        }
                    }, executor))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }
}
