package tacos;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DesignAndOrderTacosBrowserTest {
    private static WebDriver webDriver;

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

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
    void testDesignATacoPage_CorrectOrderInfo() {
        webDriver.get(homePageUrl());
        clickDesignATaco();
        assertDesignPageElements();
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        clickBuildAnotherTaco();
        buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");
        fillInAndSubmitOrderForm();
        assertEquals("Check Home Page URL", homePageUrl(), webDriver.getCurrentUrl());
    }

    @Test
    void testDesignATacoPage_EmptyOrderInfo() {
        webDriver.get(homePageUrl());
        clickDesignATaco();
        assertDesignPageElements();
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        submitEmptyOrderForm();
        fillInAndSubmitOrderForm();
        assertEquals("Check Home Page URL", homePageUrl(), webDriver.getCurrentUrl());
    }

    @Test
    void testDesignATacoPage_InvalidOrderInfo() {
        webDriver.get(homePageUrl());
        clickDesignATaco();
        assertDesignPageElements();
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        submitInvalidOrderForm();
        fillInAndSubmitOrderForm();
        assertEquals("Check Home Page URL", homePageUrl(), webDriver.getCurrentUrl());
    }

    // Browser test action methods
    private void fillInAndSubmitOrderForm() {
        assertTrue(webDriver.getCurrentUrl().startsWith(orderDetailsPageUrl()));
        fillField("input#deliveryName", "Iam Hungry");
        fillField("input#deliveryStreet", "1000 Food Blvd.");
        fillField("input#deliveryCity", "Foodsville");
        fillField("input#deliveryState", "CA");
        fillField("input#deliveryZip", "90011");
        fillField("input#ccNumber", "4111111111111111");
        fillField("input#ccExpiration", "20/12");
        fillField("input#ccCVV", "123");
        webDriver.findElement(By.cssSelector("form")).submit();
    }

    private void submitEmptyOrderForm() {
        assertEquals("Check Current Order Details Page URL",
                currentOrderDetailsPageUrl(), webDriver.getCurrentUrl());
        webDriver.findElement(By.cssSelector("form")).submit();

        assertEquals("Check Order Details Page URL ", orderDetailsPageUrl(), webDriver.getCurrentUrl());

        List<String> validationErrors = getValidationErrorTexts();
        assertEquals("Check Validation Errors Count",9, validationErrors.size());
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
        assertTrue(validationErrors.contains("Delivery name is required"));
        assertTrue(validationErrors.contains("Street is required"));
        assertTrue(validationErrors.contains("City is required"));
        assertTrue(validationErrors.contains("State is required"));
        assertTrue(validationErrors.contains("Zip code is required"));
        assertTrue(validationErrors.contains("Not a valid credit card number"));
        assertTrue(validationErrors.contains("Must be formatted MM/YY"));
        assertTrue(validationErrors.contains("Invalid CVV"));
    }

    private void submitInvalidOrderForm() {
        assertTrue(webDriver.getCurrentUrl().startsWith(orderDetailsPageUrl()));
        fillField("input#deliveryName", "I");
        fillField("input#deliveryStreet", "1");
        fillField("input#deliveryCity", "F");
        fillField("input#deliveryState", "C");
        fillField("input#deliveryZip", "8");
        fillField("input#ccNumber", "1234432112344322");
        fillField("input#ccExpiration", "14/91");
        fillField("input#ccCVV", "1234");
        webDriver.findElement(By.cssSelector("form")).submit();

        assertEquals("Check Order Details Page URL", orderDetailsPageUrl(), webDriver.getCurrentUrl());

        List<String> validationErrors = getValidationErrorTexts();
        assertEquals("Check Validation Errors Count",4, validationErrors.size());
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
        assertTrue(validationErrors.contains("Not a valid credit card number"));
        assertTrue(validationErrors.contains("Must be formatted MM/YY"));
        assertTrue(validationErrors.contains("Invalid CVV"));
    }

    private void fillField(String fieldName, String value) {
        WebElement field = webDriver.findElement(By.cssSelector(fieldName));
        field.clear();
        field.sendKeys(value);
    }

    private List<String> getValidationErrorTexts() {
        List<WebElement> validationErrorElements = webDriver.findElements(By.className("validationError"));

        return validationErrorElements.stream()
                .map(WebElement::getText)
                .toList();
    }

    private void assertDesignPageElements() {
        assertEquals("Check Design Page URL", designPageUrl(), webDriver.getCurrentUrl());
        List<WebElement> ingredientGroups = webDriver.findElements(By.className("ingredient-group"));
        assertEquals("Check Ingredient Group Count",5, ingredientGroups.size());

        WebElement wrapGroup = webDriver.findElement(By.cssSelector("div.ingredient-group#wraps"));
        List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
        assertEquals("Wraps Count",2, wraps.size());
        assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla");
        assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla");

        WebElement proteinGroup = webDriver.findElement(By.cssSelector("div.ingredient-group#proteins"));
        List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
        assertEquals("Proteins Count", 2, proteins.size());
        assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef");
        assertIngredient(proteinGroup, 1, "CARN", "Carnitas");

        WebElement cheeseGroup = webDriver.findElement(By.cssSelector("div.ingredient-group#cheeses"));
        List<WebElement> cheeses = cheeseGroup.findElements(By.tagName("div"));
        assertEquals("Cheeses Count", 2, cheeses.size());
        assertIngredient(cheeseGroup, 0, "CHED", "Cheddar");
        assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack");

        WebElement veggieGroup = webDriver.findElement(By.cssSelector("div.ingredient-group#veggies"));
        List<WebElement> veggies = veggieGroup.findElements(By.tagName("div"));
        assertEquals("Veggies Count", 2, veggies.size());
        assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes");
        assertIngredient(veggieGroup, 1, "LETC", "Lettuce");

        WebElement sauceGroup = webDriver.findElement(By.cssSelector("div.ingredient-group#sauces"));
        List<WebElement> sauces = sauceGroup.findElements(By.tagName("div"));
        assertEquals("Sauces Count", 2, sauces.size());
        assertIngredient(sauceGroup, 0, "SLSA", "Salsa");
        assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream");
    }

    private void assertIngredient(WebElement ingredientGroup, int ingredientIdx, String id, String name) {
        List<WebElement> proteins = ingredientGroup.findElements(By.tagName("div"));
        WebElement ingredient = proteins.get(ingredientIdx);
        assertEquals("Check input Tag Value Attribute", id,
                ingredient.findElement(By.tagName("input")).getAttribute("value"));
        assertEquals("Check span Tag Content", name,
                ingredient.findElement(By.tagName("span")).getText());
    }

    private void buildAndSubmitATaco(String name, String... ingredients) {
        assertDesignPageElements();

        for (String ingredient :
                ingredients) {
            webDriver.findElement(By.cssSelector("input[value='" + ingredient + "']")).click();
        }
        webDriver.findElement(By.cssSelector("input#name")).sendKeys(name);
        webDriver.findElement(By.cssSelector("form")).submit();
    }

    private void clickDesignATaco() {
        assertEquals("Home Page URL", homePageUrl(), webDriver.getCurrentUrl());
        webDriver.findElement(By.cssSelector("a[id='design']")).click();
    }

    private void clickBuildAnotherTaco() {
        assertTrue(webDriver.getCurrentUrl().startsWith(orderDetailsPageUrl()));
        webDriver.findElement(By.cssSelector("a[id='another']")).click();
    }

    // URL helper methods
    private String designPageUrl() {
        return homePageUrl() + "design";
    }

    private String orderDetailsPageUrl() {
        return homePageUrl() + "orders";
    }

    private String currentOrderDetailsPageUrl() {
        return homePageUrl() + "orders/current";
    }

    private String homePageUrl() {
        return "http://localhost:" + port + '/';
    }
}
