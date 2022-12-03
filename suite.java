import com.google.common.annotations.VisibleForTesting;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class suite {

    @Test
    public void test(){

        WebElement cheapest = null;
        WebElement expensivest = null;

        System.setProperty("webdriver.chrome.driver", Paths.get("Tools").toAbsolutePath().toString() + File.separator + "chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("-incognito");

        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.ebay.com/");

        driver.findElement(By.id("gh-ac")).sendKeys("watch");

        driver.findElement(By.id("gh-btn")).click();

        List<WebElement> elements = driver.findElements(By.xpath("//ul[@class='srp-results srp-list clearfix']/li[contains(@class,'s-item s-item__pl-on-bottom')]"));

        for(int i = 0; i < elements.size(); i++){//for(WebElement element : elements){

            String currentItemPriceText = driver.findElement(By.xpath("//ul[@class='srp-results srp-list clearfix']/li[contains(@class,'s-item s-item__pl-on-bottom')]["+ (i + 1) +"]//div[contains(@class,'s-item__detail')]/span[@class='s-item__price']")).getText();

            if(currentItemPriceText == "" || currentItemPriceText.contains("to")){
                continue;
            }

            String Formatted = "";
            Pattern p = Pattern.compile("[0-9]+.[0-9]+.[0-9]*");
            Matcher m = p.matcher(currentItemPriceText);
            while (m.find()) {
                Formatted =  Formatted + m.group();
            }

            currentItemPriceText = Formatted;

            if(currentItemPriceText.contains(",")){
                currentItemPriceText = currentItemPriceText.replace(",", "");
            }

            double currentItemPriceNumber = Double.valueOf(currentItemPriceText.trim());

            if(cheapest == null && expensivest == null){
                expensivest = cheapest = elements.get(i);
            }

            String cheapestPriceText = cheapest.findElement(By.className("s-item__price")).getText();

            String expensivestPriceText = expensivest.findElement(By.className("s-item__price")).getText();

            Formatted = "";

            m = p.matcher(cheapestPriceText);
            while (m.find()) {
                Formatted =  Formatted + m.group();
            }

            cheapestPriceText = Formatted;

            if(cheapestPriceText.contains(",")){
                cheapestPriceText = cheapestPriceText.replace(",", "");
            }

            double cheapestPriceNumber = Double.valueOf(cheapestPriceText.trim());

            Formatted = "";

            m = p.matcher(expensivestPriceText);
            while (m.find()) {
                Formatted =  Formatted + m.group();
            }

            expensivestPriceText = Formatted;

            if(expensivestPriceText.contains(",")){
                expensivestPriceText = expensivestPriceText.replace(",", "");
            }

            double expensivestPriceNumber = Double.valueOf(expensivestPriceText.trim());

            if(currentItemPriceNumber < cheapestPriceNumber){
                cheapest = elements.get(i);
            }
            if(currentItemPriceNumber > expensivestPriceNumber){
                expensivest = elements.get(i);
            }
        }

        WebElement tmp = cheapest.findElement(By.className("s-item__title"));
        String cheapestName = tmp.findElement(By.tagName("span")).getText();

        if(cheapestName.toLowerCase().contains("new listing")){
            cheapestName = cheapestName.toLowerCase().replace("new listing", "").trim();
        }

        tmp = expensivest.findElement(By.className("s-item__title"));
        String expensivestName = tmp.findElement(By.tagName("span")).getText();

        if(expensivestName.toLowerCase().contains("new listing")){
            expensivestName = expensivestName.toLowerCase().replace("new listing", "").trim();
        }

        System.out.println("Cheapest item is " + cheapest.findElement(By.className("s-item__price")).getText() + " Item name : " + cheapestName);

        System.out.println("Cheapest item is " + expensivest.findElement(By.className("s-item__price")).getText() + " Item name : " + expensivestName);

        driver.quit();
    }

}
