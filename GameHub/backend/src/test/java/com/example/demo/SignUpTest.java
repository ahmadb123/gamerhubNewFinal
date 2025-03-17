package com.example.demo;


import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

// If using WebDriverManager
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignUpTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        // If using WebDriverManager (highly recommended):
        WebDriverManager.chromedriver().setup();
        
        // Otherwise, ensure chromedriver is on your PATH or specify the path here:
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Optionally set a timeout
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Selenium Test 1:
     * - Attempt to sign up with an existing username
     * - Expect an error message
     * - Change username to a new one, expect success
     */
    @Test
    @Order(1)
    public void testSignUpWithExistingUsername() {
        // 1. Navigate to home page (or sign-up page)
        driver.get("http://localhost:8080/"); 
        // Adjust the URL/port as needed for your local environment

        // 2. Click "Sign in" link or "Sign up" link (depending on your UI labels)
        //    This step depends on how your nav bar or sign-up button is labeled:
        WebElement signUpLink = driver.findElement(By.id("signUpLink")); 
        // ^ Example: <a id="signUpLink" href="/signup">Sign Up</a>
        signUpLink.click();

        // 3. Enter an EXISTING username (e.g. "jsmith") and same password in both fields
        WebElement usernameField = driver.findElement(By.id("username")); 
        WebElement passwordField1 = driver.findElement(By.id("password"));
        WebElement passwordField2 = driver.findElement(By.id("confirmPassword"));

        usernameField.clear();
        usernameField.sendKeys("admin");  // existing user
        passwordField1.clear();
        passwordField1.sendKeys("admin");
        passwordField2.clear();
        passwordField2.sendKeys("admin");

        // 4. Click "Create account"
        WebElement createAccountBtn = driver.findElement(By.id("createAccountBtn"));
        createAccountBtn.click();

        // 5. Verify that the page did not navigate away and an error toast or message shows up
        //    This check can be done either by verifying you’re still on the same URL, or
        //    by checking for an error message element:
        WebElement errorToast = driver.findElement(By.id("toastError")); 
        // Example <div id="toastError">Username is already taken</div>
        assertTrue(errorToast.isDisplayed(), "Expected error toast to be displayed");
        assertTrue(errorToast.getText().contains("already taken"), 
                   "Expected error message to mention 'already taken'");

        // 6. Change the username to something new ("jsmith2025")
        usernameField.clear();
        usernameField.sendKeys("jsmith2025");

        // 7. Click "Create account" again
        createAccountBtn.click();

        // 8. Verify success: e.g., the URL changes or the homepage shows "jsmith2025" in top menu
        //    If you show a success toast, you can also locate it:
        WebElement successToast = driver.findElement(By.id("toastSuccess")); 
        // e.g. <div id="toastSuccess">Account successfully created!</div>
        assertTrue(successToast.isDisplayed(), "Expected success toast to be displayed");
        assertTrue(successToast.getText().contains("Account successfully created!"),
                   "Expected success message to mention 'Account successfully created!'");

        // Alternatively, check if the top menu displays the new username:
        WebElement topMenuUsername = driver.findElement(By.id("currentUsername"));
        assertEquals("jsmith2025", topMenuUsername.getText(), 
                     "Expected the top menu to show the new username");
    }

    /**
     * Selenium Test 2:
     * - Attempt sign-up with mismatched passwords
     * - Expect an error
     * - Correct the password and confirm success
     */
    @Test
    @Order(2)
    public void testSignUpMismatchedPasswords() {
        driver.get("http://localhost:8080/signup"); 
        // Directly to sign-up page, or navigate similarly to Test #1

        // Enter a new username
        WebElement usernameField = driver.findElement(By.id("username")); 
        usernameField.clear();
        usernameField.sendKeys("jsmith2026");

        // Enter two different passwords
        WebElement passwordField1 = driver.findElement(By.id("password"));
        WebElement passwordField2 = driver.findElement(By.id("confirmPassword"));

        passwordField1.clear();
        passwordField1.sendKeys("pa$$Word");
        passwordField2.clear();
        passwordField2.sendKeys("password"); // intentionally different

        // Submit
        WebElement createAccountBtn = driver.findElement(By.id("createAccountBtn"));
        createAccountBtn.click();

        // Check for error about mismatched passwords
        WebElement errorToast = driver.findElement(By.id("toastError"));
        assertTrue(errorToast.isDisplayed(), "Expected error toast to be displayed");
        assertTrue(errorToast.getText().contains("do not match"), 
                   "Expected error message to mention that passwords do not match");

        // Correct the second password
        passwordField2.clear();
        passwordField2.sendKeys("pa$$Word");
        createAccountBtn.click();

        // Confirm success
        WebElement successToast = driver.findElement(By.id("toastSuccess"));
        assertTrue(successToast.isDisplayed(), "Expected success toast to be displayed");
        assertTrue(successToast.getText().contains("Account successfully created!"), 
                   "Expected success message after correcting password mismatch");
    }

    /**
     * Selenium Test 3:
     * - Enter a brand‐new username
     * - Provide valid matching passwords
     * - Expect to be redirected to the homepage with success
     */
    @Test
    @Order(3)
    public void testSignUpSuccessful() {
        driver.get("http://localhost:8080/signup"); 

        // Fill out a brand-new username & matching passwords
        WebElement usernameField = driver.findElement(By.id("username")); 
        WebElement passwordField1 = driver.findElement(By.id("password"));
        WebElement passwordField2 = driver.findElement(By.id("confirmPassword"));

        usernameField.clear();
        usernameField.sendKeys("jsmith2027");
        passwordField1.clear();
        passwordField1.sendKeys("pa$$Word");
        passwordField2.clear();
        passwordField2.sendKeys("pa$$Word");

        // Click "Create account"
        WebElement createAccountBtn = driver.findElement(By.id("createAccountBtn"));
        createAccountBtn.click();

        // Check for success
        WebElement successToast = driver.findElement(By.id("toastSuccess"));
        assertTrue(successToast.isDisplayed(), "Expected success toast to be displayed");
        assertTrue(successToast.getText().contains("Account successfully created!"),
                   "Expected success message: Account successfully created!");

        // Optionally verify redirect to homepage
        assertTrue(driver.getCurrentUrl().contains("/home"),
                   "Expected to be redirected to /home after a successful signup");

        // Possibly check that the top menu or some user indicator is correct
        WebElement topMenuUsername = driver.findElement(By.id("currentUsername"));
        assertEquals("jsmith2027", topMenuUsername.getText(), 
                     "Top menu should display newly created username");
    }
}
