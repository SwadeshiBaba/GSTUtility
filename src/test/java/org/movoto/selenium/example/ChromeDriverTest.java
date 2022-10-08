package org.movoto.selenium.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.movoto.selenium.example.bulkUploadSupplyOutward.XlsToSupplyOutwardConverter;
import org.movoto.selenium.example.enums.GSTEnum;
import org.movoto.selenium.example.pojo.GstCalculationDTO;
import org.movoto.selenium.example.pojo.SupplyOutwardDTO;
import org.movoto.selenium.example.utils.AESUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by umbhut on 29/02/22.
 */
public class ChromeDriverTest {

    private String testUrl;
    private WebDriver driver;

    private WebDriverWait w;

    private ChromeOptions chromeOptions;

    private Capabilities cap;

    private List<SupplyOutwardDTO> supplyOutwardDTOList;

    public static final Properties properties = new Properties();

    public ChromeDriverTest() throws IOException {
        this.properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
    }

   @Before
    public void prepare() {
        //setup chromedriver
        /*System.setProperty(
                "webdriver.chrome.driver",
                "C:\\Users\\ubhutada\\IdeaProjects\\selenium-example\\webdriver\\chromedriver.exe");*/
        WebDriverManager.chromedriver().setup();
        testUrl = properties.getProperty("testUrl");

        // Create a new instance of the Chrome driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.

           chromeOptions = new ChromeOptions();
           chromeOptions.setExperimentalOption("debuggerAddress",properties.getProperty("debuggerAddress"));
           driver = new ChromeDriver(chromeOptions);
           /*driver.getWindowHandles()
                   driver.switchTo().window()*/
           // getCapabilities will return all browser capabilities
           cap=((HasCapabilities)driver).getCapabilities();

            // asMap method will return all capability in MAP
           Map<String, Object> myCap=cap.asMap();

            // print the map data-
           System.out.println(myCap);

           List<Object> chromeOptions =  myCap.entrySet().stream().filter(entry -> "goog:chromeOptions".equals(entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
           System.out.println(chromeOptions);

           //maximize window
           //driver.manage().window().maximize();

           // And now use this to visit myBlog
           // Alternatively the same thing can be done like this
           // driver.navigate().to(testUrl);
          //driver.get(testUrl);

       /*}else {
           ChromeOptions co = new ChromeOptions();
           co.merge(cap);
           driver = new ChromeDriver(co);
       }*/

    }

    @Test
    public void testLogin() throws IOException {

        // Find elements by attribute lang="READ_MORE_BTN"
        waitUnmask(driver);
        driver.findElements(By.cssSelector("a[href*=\'login\']")).get(0).click();
        waitUnmask(driver);

        driver.findElements(By.id("username")).get(0).sendKeys(decrypt(properties.getProperty("userName")));
        driver.findElements(By.id("user_pass")).get(0).sendKeys(decrypt(properties.getProperty("userPass")));
        w = new WebDriverWait(driver, Duration.ofSeconds(10));
        w.until(new ExpectedCondition<Boolean>() { public Boolean apply(WebDriver d) {
            return 6 == driver.findElements(By.id("captcha")).get(0).getAttribute("value").length();
        } });
        driver.findElements(By.xpath("//button[text()='Login']")).get(0).click();

    }

    @Test
    public void testReturnToDashBoard() throws IOException{

        waitUnmask(driver);
        driver.findElements(By.xpath("//button[span='Return Dashboard']")).get(0).click();
    }

    @Test

    public void testReturnSearch() throws IOException{

        waitUnmask(driver);

        Select financialYear = new Select(driver.findElements(By.name("fin")).get(0));
        financialYear.selectByVisibleText(properties.getProperty("financialYear"));

        waitUnmask(driver);

        Select quarter = new Select(driver.findElements(By.name("quarter")).get(0));
        quarter.selectByVisibleText(properties.getProperty("quarter"));

        waitUnmask(driver);

        Select month = new Select(driver.findElements(By.name("mon")).get(0));
        month.selectByVisibleText(properties.getProperty("month"));

        waitUnmask(driver);
        driver.findElements(By.xpath("//button[text() ='Search']")).get(0).click();
        //assertTrue("Catch cde length should be 6",6 == catchaCode.length());
        /*assertTrue("The Captcha should be entered manually in 10 sec",
                (new WebDriverWait(driver, Duration.ofSeconds(10)))
                        .until(new ExpectedCondition<Boolean>() {
                            public Boolean apply(WebDriver d) {
                                return 6 == d.findElements(By.id("captcha")).get(0).getText().length();
                            }
                        })
        );
        */
    }

   @Test

    public void testGSR1IFF() throws IOException {
        w = new WebDriverWait(driver, Duration.ofSeconds(10));
        w.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='GSTR1']"))).click();
    }

    @Test

    public void testGSTR1B2BSEZDEInoices() throws IOException{

        waitUnmask(driver);
        driver.findElements(By.xpath("//div[text()='4A, 4B, 6B, 6C - B2B, SEZ, DE Invoices']")).get(0).click();

    }

   @Test

    public void testAddInvoices() throws IOException{

        waitUnmask(driver);
       List<WebElement> eleList = driver.findElements(By.xpath("//button[text() ='Add Record']"));
        if(!eleList.isEmpty()){
            eleList.get(eleList.size()-1).click();
        }

    }

    @Test
    public void testImportExcel() throws IOException {
        XlsToSupplyOutwardConverter supplyOutwardConverter = new XlsToSupplyOutwardConverter();
        ReadExcelFile ref = new ReadExcelFile(supplyOutwardConverter);
        supplyOutwardDTOList = ref.getInputFile();

        System.out.println("Supply Outward list size : "+supplyOutwardDTOList.size());
        for(SupplyOutwardDTO dto : supplyOutwardDTOList){
            testAddRecordB2B(dto);
        }
    }

    public void testAddRecordB2B(SupplyOutwardDTO dto) throws IOException {
        System.out.println("Supply Outward DTO : "+dto);
        waitUnmask(driver);
        driver.findElements(By.id("ruid_value")).get(0).sendKeys(dto.getGstn());
        waitUnmask(driver);
        try {
            String gstnxpath = "//div[text()=\'" + dto.getGstn().toUpperCase() + "-\']/..";
            w = new WebDriverWait(driver, Duration.ofSeconds(4));
            w.until(ExpectedConditions.elementToBeClickable(By.xpath(gstnxpath))).click();
        }catch(TimeoutException t){
            //do nothing
        }

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        return "" != d.findElements(By.id("u_name")).get(0).getAttribute("value");
                    }
                });

        String party = driver.findElements(By.id("u_name")).get(0).getAttribute("value");
        System.out.println("Party : "+party);
        if(null != party && party.length() > 0){
            waitUnmask(driver);
            driver.findElements(By.id("inv_no")).get(0).clear();
            driver.findElements(By.id("inv_no")).get(0).sendKeys(dto.getDocNo());
            waitUnmask(driver);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String format = formatter.format(dto.getDocDate());
            driver.findElements(By.id("invdate")).get(0).sendKeys(format);
            waitUnmask(driver);
            driver.findElements(By.id("invval")).get(0).clear();
            driver.findElements(By.id("invval")).get(0).sendKeys(String.valueOf(dto.getInvoiceValue()));
            waitUnmask(driver);
            //by default : place of supply is getting selected on basis of GST number
            /*Select pos = new Select(driver.findElements(By.id("pos")).get(0));
            pos.selectByVisibleText(dto.getPlaceOfSupply());
            waitUnmask(driver);*/

            List<GstCalculationDTO> gstDto = dto.getGstCalculationList();
            for(GstCalculationDTO gstRow: gstDto){
                testGSTCalculationFiling(gstRow);
            }

            waitUnmask(driver);
            driver.findElements(By.xpath("//button[text()='Save']")).get(0).click();
            waitUnmask(driver);


            try {
                String ruleIdXpath = "//input[@id='ruid_value']/../span";
                w = new WebDriverWait(driver, Duration.ofSeconds(3));
                w.until(ExpectedConditions.elementToBeClickable(By.xpath(ruleIdXpath)));
                driver.findElements(By.xpath("//input[@id='ruid_value']/../span")).get(0).click();
            }catch(TimeoutException e){
                driver.findElements(By.id("ruid_value")).get(0).clear();
            }
            waitUnmask(driver);
        }
    }

    public void testGSTCalculationFiling(GstCalculationDTO row){
       waitUnmask(driver);
       GSTEnum percentage= GSTEnum.findByPercentage(row.getTaxRate());
        driver.findElements(By.xpath("//div[@class='table-responsive']/table/tbody/tr[td//text()='"+percentage.id()+"']/td[2]/input")).get(0).sendKeys(row.getTaxableValue().toString());
    }

    @Test
    public void testOne () throws IOException {
       testLogin();
       testReturnToDashBoard();
       testReturnSearch();
       testGSR1IFF();
       testGSTR1B2BSEZDEInoices();
       testAddInvoices();
       testImportExcel();
    }

    @After
    public void teardown() throws IOException {
        //driver.quit();
    }

    public void waitUnmask(WebDriver driver){
        w = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofSeconds(1));
        w.until(ExpectedConditions.invisibilityOfElementLocated (By.xpath("//div[@class ='dimmer-holder']")));
    }

    public String decrypt(String encryptedKey){
        String key = encryptedKey;
        if(null != encryptedKey){
            if(encryptedKey.startsWith("{AES}")){
                encryptedKey = encryptedKey.replaceAll("\\{AES}","");
                key = AESUtil.decrypt(encryptedKey);
            }
        }
        return key;
    }
}
