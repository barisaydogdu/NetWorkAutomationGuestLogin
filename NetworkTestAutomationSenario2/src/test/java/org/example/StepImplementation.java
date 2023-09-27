package org.example;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.thoughtworks.gauge.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StepImplementation extends BaseTest {
    private String userEmail; // userEmail tanımlandı
    private String userPassword; // userPassword tanımlandı
    private By emailField = By.id("n-input-email");
    private By passwordField = By.id("n-input-password");
    private By loginButton = By.id("login-button");
    private boolean cookiePopupPresent = false;
    @Step("Sayfanın yüklenmesi beklenir")
    public void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30L)); // Max bekleme süresi 30 saniye
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
    }
    @Step("<locatorType> tipinde <locator> varlığı kontrol edilir <errorMessage>")
    public void verifyElementIsDisplayed(String locatorType,String locator,String errorMessage) {
        WebElement element = findElement(locatorType,locator);
        Assert.assertTrue(element.isDisplayed(), errorMessage);
        Logger.error("Element Yüklenmedi." +element);
        System.out.println(element.isDisplayed()+ "verifyElementIsDisplayed metoduna girildi.");
    }
    @Step("<targetURL> URL'sinin geldiği kontrol edilir")
    public void verifyURL(String targetURL) {
        String currentURL = driver.getCurrentUrl(); // Mevcut sayfanın URL'sini alın
        System.out.println("CurrentURL: " +currentURL);
        // URL'nin beklendiği şekilde olduğunu doğrulayın veya bir şartı kontrol edin
        if (currentURL.equals(targetURL)) {
            Logger.error("URL Doğru." +currentURL);
            System.out.println("URL dogru: " + currentURL);
        } else {
            Logger.error("URL Yanlış." +currentURL);
            System.out.println("URL yanlis: " + currentURL);
        }
    }
    @Step("<locatorValue> yüklenmesi beklenir <locatorType>")
    public void WaitUntilLoad(String LocatorValue,String locatorType) {

        By locator= null;
        switch (locatorType.toLowerCase()) {
            case "css":
                locator = By.cssSelector(LocatorValue);
                break;
            case "xpath":
                locator = By.xpath(LocatorValue);
                break;
            case "id":
                locator = By.id(LocatorValue);
                break;
            case "classname":
                locator = By.className(LocatorValue);
                break;
            default:
                Logger.error("Geçersiz locator türü: " + locatorType);

                System.out.println("Geçersiz locator türü: " + locatorType);
                return;
        }
        try {
            int timeoutSeconds = 30; // Bekleme süresi (örneğin, 30 saniye)
            Duration timeoutDuration = Duration.ofSeconds(timeoutSeconds); // Duration tipine çevirin
            WebDriverWait wait = new WebDriverWait(driver, timeoutDuration);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("Wait until load metodu çalışıyor");
        } catch (Exception e) {
            Logger.error("Element belirtilen süre içinde yüklenmedi");
            System.out.println("Element belirtilen süre içinde yüklenmedi.");
            // İsterseniz burada hata işleme veya raporlama yapabilirsiniz.
        }
    }
    @Step("Sayfada <locatorType> ile <LocatorValue> tıklanır")
    public void clickButton(String locatorType, String LocatorValue) {
        WebElement element=findElement(locatorType,LocatorValue);
        element.click();
        Logger.error( LocatorValue+ " butonuna tiklandi.");
        System.out.println( LocatorValue+ " butonuna tiklandi.");
    }
    @Step("Sayfada <locatorType> ile <LocatorValue> öğesine scroll yapılır")
    public void scrollToElement(String locatorType, String LocatorValue) {
        WebElement element = findElement(locatorType, LocatorValue);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        int yOffset = -300; // Yukarıda ne kadar durmasını ayarladık
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + yOffset + ");");
    }
    @Step("Sayfa kullanıcının CSV dosyasını açar")
    public void openCSVFile() {
        String csvFilePath = "ExcelFile/networkexcel.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Her satırı işleme koyma kodu
                System.out.println(line); // Örnek olarak satırı ekrana yazdırır
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("Sayfa CSV dosyasından kullanıcı bilgilerini okur")
    public void readUserDataFromCSV() {
        String csvFilePath = "ExcelFile/networkexcel.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                userEmail = data[0].trim();
                userPassword = data[1].trim();
              Logger.error("Data okundu");

                // Kullanıcı bilgilerini kullanmak için
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Data okunurken hata oluştu");
        }
    }
    @Step("Kullanıcı bilgileri doldurulur")
    public void performLogin() {

        enterEmail(userEmail);
        enterPassword(userPassword);
    }
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }
    @Step("Sayfada <productList> listelenen indirimli seçilir")
    public void clickFirstProduct(String productList)
    {
        List<WebElement> products = driver.findElements(By.className(productList));
        for (WebElement product : products) {
            // Her ürünün indirim bilgisini kontrol edin (örneğin, indirim yüzdesini içeren bir etiket varsa).
            WebElement discountLabel = product.findElement(By.className("product__discountPercent")); // Örnek bir CSS seçici
            // İndirimli ürünü bulduk.
            if (discountLabel != null) {
              //  scrollDownToElementNew(driver,discountLabel);
                product.click();
                break; // İlk indirimli ürünü bulduktan sonra döngüyü sonlandırın.

            }
        }

    }


    @Step("<cookieDiv> Çerez var mı kontrol edilir")
    public void checkForCookiePopup(String cookieDiv) {
        try {
            WebElement cookiePopup = driver.findElement(By.id(cookieDiv)); // Çerez kutusunun HTML ID'sini belirtin
            if (cookiePopup != null) {
                Logger.error("Cookie Geldi");
                System.out.println("Cookie Geldi");
                cookiePopupPresent = true;
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            if (!cookiePopupPresent) {
                Logger.error("Çerez Yok!");
                System.out.println("Çerez yok");
            }
        }
    }

    @Step("<cookieAcceptButton> butonu ile kabul edilir")
    public void acceptCookie(String cookieAcceptButton) {
        if (cookiePopupPresent) {
            WebElement acceptButton = findElement("id", cookieAcceptButton);
            if (acceptButton != null) {
                acceptButton.click();
            }
        }
    }
    @Step("<element> Elementin üstüne gelinir <locatorType>")
    public void hoverElement(String element,String locatorType)
    {   System.out.println("Hover elementine girildli");
        WebElement elementToHover=findElement(locatorType,element);
        Actions actions = new Actions(driver);
        actions.moveToElement(elementToHover).perform();
    }

    @Step("<key> saniye kadar bekle")
    public void WaitWitSecond(int key) throws InterruptedException {
        Thread.sleep(key * 1000);
    }

    private WebElement findElement(String locatorType, String LocatorValue) {
        WebElement element = null;
        switch (locatorType.toLowerCase()) {
            case "id":
                element = driver.findElement(By.id(LocatorValue));
                break;
            case "css":
                element = driver.findElement(By.cssSelector(LocatorValue));
                break;
            case "classname":
                element = driver.findElement(By.className(LocatorValue));
                break;
            case "xpath":
                element = driver.findElement(By.xpath(LocatorValue));
                break;
            default:
                throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        }
        return element;
    }
}
