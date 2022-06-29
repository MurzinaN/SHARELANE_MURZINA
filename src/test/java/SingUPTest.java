import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SingUPTest {
    WebDriver driver;
    @BeforeClass
    public void start(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }
   @AfterClass
    public void finish(){
        driver.close();
    }
    @Test
    public void zipCodeTestPositive() {
        driver.get("https://www.sharelane.com/cgi-bin/register.py");
        WebElement zipCodeInput = driver.findElement(By.name("zip_code"));
        zipCodeInput.sendKeys("12345");
        WebElement continueButton = driver.findElement(By.cssSelector("[value='Continue']"));
        continueButton.click();
        Assert.assertFalse(driver.findElement(By.name("zip_code")).isDisplayed(),"1.1 zip_code shouldn't be on the display");
        Assert.assertTrue(driver.findElement(By.name("first_name")).isDisplayed(),"1.2 first_name should appear on the display");
    }


    @Test
    public void zipCodeTestNegativeEmptyLines(){
        driver.get("https://www.sharelane.com/cgi-bin/register.py");
        WebElement continueButton = driver.findElement(By.cssSelector("[value='Continue']"));
        continueButton.click();
        Assert.assertTrue(driver.findElement(By.name("zip_code")).isDisplayed(),"2.1 zip_code should remain on the display");
        Assert.assertTrue(driver.findElement(By.cssSelector("[class=error_message]")).isDisplayed(),"2.2 error message should be displayed");
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Oops, error on page. ZIP code should have 5 digits']")).isDisplayed(),"2.3 Oops, error on page. should be displayed");
        Assert.assertTrue(driver.findElements(By.name("first_name")).isEmpty(),"2.4 first_name shouldn't be on the display");
    }

    @Test
    public void zipCodeTestNegativeMoreDigits(){
        driver.get("https://www.sharelane.com/cgi-bin/register.py");
        WebElement zipCodeInput = driver.findElement(By.name("zip_code"));
        zipCodeInput.sendKeys("12345678");
        WebElement continueButton = driver.findElement(By.cssSelector("[value='Continue']"));
        continueButton.click();
        Assert.assertTrue(driver.findElement(By.name("zip_code")).isDisplayed(),"3.1 zip_code should remain on the display");
        Assert.assertTrue(driver.findElements(By.name("first_name")).isEmpty(),"3.2 first_name shouldn't appear on the display");
    }

    @Test
    public void signUpTestPositive(){
        driver.get("https://www.sharelane.com/cgi-bin/register.py?page=1&zip_code=12345");
        WebElement firstNameInput = driver.findElement(By.name("first_name"));
        firstNameInput.sendKeys("Ivan");
        WebElement lastNameInput = driver.findElement(By.name("last_name"));
        lastNameInput.sendKeys("Ivanov");
        WebElement emailInput = driver.findElement(By.name("email"));
        emailInput.sendKeys("ivanov@123.sharelane.com");
        WebElement passwordInput = driver.findElement(By.name("password1"));
        passwordInput.sendKeys("1111");
        WebElement confirmPasswordInput = driver.findElement(By.name("password2"));
        confirmPasswordInput.sendKeys("1111");
        WebElement registerButton = driver.findElement(By.cssSelector("[value='Register']"));
        registerButton.click();
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Account is created!']")).isDisplayed(),"4.1 Account is created! should appear on the display");
        Assert.assertTrue(driver.findElements(By.name("first_name")).isEmpty(),"4.2 first_name shouldn't be on the display");
    }


    @Test
    public void signUpTestNegativeDifferentPassword(){
        driver.get("https://www.sharelane.com/cgi-bin/register.py?page=1&zip_code=12345");
        WebElement firstNameInput = driver.findElement(By.name("first_name"));
        firstNameInput.sendKeys("Ivan");
        WebElement lastNameInput = driver.findElement(By.name("last_name"));
        lastNameInput.sendKeys("Ivanov");
        WebElement emailInput = driver.findElement(By.name("email"));
        emailInput.sendKeys("ivanov@123.sharelane.com");
        WebElement passwordInput = driver.findElement(By.name("password1"));
        passwordInput.sendKeys("1111");
        WebElement confirmPasswordInput = driver.findElement(By.name("password2"));
        confirmPasswordInput.sendKeys("2222");
        WebElement registerButton = driver.findElement(By.cssSelector("[value='Register']"));
        registerButton.click();
        Assert.assertFalse(driver.findElement(By.xpath("//*[text()='Account is created!']")).isDisplayed(),"5.1 account mustn't be created");
        Assert.assertFalse(driver.findElements(By.name("first_name")).isEmpty(),"5.2 first_name should be on the display");
    }


    @Test
    public void signUpTestNegativeOnlyFirstName(){
        driver.get("https://www.sharelane.com/cgi-bin/register.py?page=1&zip_code=12345");
        WebElement firstNameInput = driver.findElement(By.name("first_name"));
        firstNameInput.sendKeys("Ivan");
        WebElement registerButton = driver.findElement(By.cssSelector("[value='Register']"));
        registerButton.click();
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Oops, error on page. Some of your fields have invalid data or email was previously used']")).isDisplayed(),"6.1 Oops, error on page. should appear on the display");
        Assert.assertTrue(driver.findElement(By.name("first_name")).isDisplayed(),"6.2 first_name should be on the display");
        Assert.assertTrue(driver.findElements(By.xpath("//*[text()='Account is created!']")).isEmpty(),"6.3 account mustn't be created");
    }

    @Test
    public void addToCartTestPositive() throws InterruptedException {
        driver.get("https://www.sharelane.com/cgi-bin/register.py");
        WebElement testPortal = driver.findElement(By.cssSelector("[href='../test_portal.html']"));
        testPortal.click();
        WebElement accountCreator = driver.findElement(By.cssSelector("[href='../cgi-bin/create_account.py']"));
        accountCreator.click();
        WebElement createButton = driver.findElement(By.cssSelector("[value='Create new user account']"));
        createButton.click();
        WebElement autoLoginButton = driver.findElement(By.cssSelector("[value='Auto Login']"));
        autoLoginButton.click();
        Thread.sleep(3000);
        WebElement bookName = driver.findElement(By.cssSelector("[href^='./show_book.py?book_id=']"));
        bookName.click();
        WebElement addToCartButton = driver.findElement(By.cssSelector("[src='../images/add_to_cart.gif']"));
        addToCartButton.click();
        String actualMessage = driver.findElement(By.className("confirmation_message")).getText();
        String expectedMessage = "Book was added to the Shopping Cart";
        Assert.assertEquals(actualMessage, expectedMessage,"Book was added to the Shopping Cart should be on the display");

    }

    @Test
    public void changeQuantityTestPositive() throws InterruptedException {
        driver.get("https://www.sharelane.com/cgi-bin/register.py");
        WebElement testPortal = driver.findElement(By.cssSelector("[href='../test_portal.html']"));
        testPortal.click();
        WebElement accountCreator = driver.findElement(By.cssSelector("[href='../cgi-bin/create_account.py']"));
        accountCreator.click();
        WebElement createButton = driver.findElement(By.cssSelector("[value='Create new user account']"));
        createButton.click();
        WebElement autoLoginButton = driver.findElement(By.cssSelector("[value='Auto Login']"));
        autoLoginButton.click();
        Thread.sleep(3000);
        WebElement bookName = driver.findElement(By.cssSelector("[href^='./show_book.py?book_id=']"));
        bookName.click();
        WebElement addToCartButton = driver.findElement(By.cssSelector("[src='../images/add_to_cart.gif']"));
        addToCartButton.click();
        WebElement shoppingCart = driver.findElement(By.cssSelector("[href='./shopping_cart.py']"));
        shoppingCart.click();
        WebElement quantityInput = driver.findElement(By.name("q"));
        quantityInput.clear();
        quantityInput.sendKeys("5");
        WebElement updateButton = driver.findElement(By.cssSelector("[value='Update']"));
        updateButton.click();
        String actualQuantity = driver.findElement(By.name("q")).getAttribute("value");
        String expectedQuantity = "5";
        Assert.assertEquals(actualQuantity, expectedQuantity,"Quantity should be 5");

    }


}