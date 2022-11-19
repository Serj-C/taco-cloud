package tacos;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HomePageBrowserTest {
    @LocalServerPort
    private int port;
    private static WebDriver webDriver;

    @BeforeAll
    static void beforeAll() {
        webDriver = new HtmlUnitDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterAll
    static void afterAll() {
        webDriver.quit();
    }

    @Test
    void testHomePage() {
        String homePage = "http://localhost:" + port;
        webDriver.get(homePage);

        String titleText = webDriver.getTitle();
        assertThat(titleText).isEqualTo("Taco Cloud");

        String h1Text = webDriver.findElement(By.tagName("h1")).getText();
        assertThat(h1Text).isEqualTo("Welcome to...");

        String imgSrc = webDriver.findElement(By.tagName("img")).getAttribute("src");
        assertThat(imgSrc).isEqualTo(homePage + "/images/TacoCloud.png");
    }
}
