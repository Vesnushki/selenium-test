import config.InitTestEnv;
import org.apache.log4j.Logger;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by a418 on 3/5/16.
 */



public class VechiclePage extends InitTestEnv{

    private static By       autoBrand       = By.cssSelector("#param_subcat > * > a  > span");
    private static By       autoBrandList   = By.cssSelector("#param_subcat > * > ul > li");
    private static String   autoBrandDefTxt = "Марка";

    private static By       autoModel       = By.cssSelector("#param_model > * > a > span");
    private static String   autoModelDefTxt = "Модель";

    private static String   cssPL           = "#param_price > * > div.filter-item-from > ";
    private static By       priceLow        = By.cssSelector(cssPL+"a > span");
    private static By       priceLowInput   = By.cssSelector(cssPL+"label > input");
    private static By       priceLowList    = By.cssSelector(cssPL+"ul > li");
    private static String   priceLowDefTxt  = "Цена от (грн.)";

    private static String   cssPH           = "#param_price > * > div.filter-item-to > ";
    private static By       priceHigh       = By.cssSelector(cssPH+"a > span");
    private static By       priceHighInput  = By.cssSelector(cssPH+"label > input");
    private static By       priceHighList   = By.cssSelector(cssPH+"ul > li");
    private static String   priceHighDefTxt = "Цена до (грн.)";

    private static String   cssMilLow       = "#param_motor_mileage > * > div.filter-item-from > ";
    private static By       mileageLow      = By.cssSelector(cssMilLow+"a");
    private static By       mileageLowInput = By.cssSelector(cssMilLow+"label > input");
    private static By       mileageLowList  = By.cssSelector(cssMilLow+"ul > li");

    private static By       priceAutoList   = By.cssSelector("td > div > p.price");

    @BeforeClass
    public static void init() throws IOException {
        log = Logger.getLogger(VechiclePage.class);
    }

    @Test
    public void checkDefFilter(){
        initDriver();
        driver.get("http://olx.ua/transport/legkovye-avtomobili/");

        String priceL = driver.findElement(priceLow).getText();
        assertEquals(priceLowDefTxt,priceL);

        String priceH = driver.findElement(priceHigh).getText();
        assertEquals(priceH,priceHighDefTxt);

        String model = driver.findElement(autoModel).getText();
        assertEquals(model, autoModelDefTxt);

        String brand = driver.findElement(autoBrand).getText();
        assertEquals(brand, autoBrandDefTxt);
        driver.close();
    }

    @Test
    public void checkAutoBrand(){
        initDriver();
        driver.get("http://olx.ua/transport/legkovye-avtomobili/");
        Set<String> autoModelCheckList = new HashSet<>(Arrays.asList(prop.getProperty("autoList").split(",")));


        driver.findElement(autoBrand).click();//список подгружается лишь после нажатия на фильтр

        List<WebElement> markList = driver.findElements(autoBrandList);//загрузили список авто
        markList.remove(0);//удаляем запись отсутствует вложенный елемент span возникает ошибка

        Set<String> currAutoModels = new HashSet<>();//текущий список авто сохранили
        for (WebElement m : markList){
            currAutoModels.add(m.getText().replace(m.findElement(By.cssSelector("span")).getText(), "").trim());
        }

        for (String checkedModel: autoModelCheckList) {//сравниваем список авто из property файла с текущим списком авто
            if (!currAutoModels.contains(checkedModel)) {
                String msgError = "Модель " + checkedModel + " отсутствует в списке";
                log.error(msgError);
                Assert.fail("Модель " + checkedModel + " отсутствует в списке");
            }
        }
        driver.close();
    }

    @Test
    public void validPriceField(){
        initDriver();
        driver.get("http://olx.ua/transport/legkovye-avtomobili/");
        Integer numberValue = 123;
        String  stringValue = "t";

        driver.findElement(priceLow).click();//жмем для активации поля input
        WebElement pFrom = driver.findElement(priceLowInput);
        pFrom.clear();
        pFrom.sendKeys(numberValue.toString());
        assertEquals(numberValue,Integer.valueOf(pFrom.getAttribute("value")));//проверка числа

        pFrom.clear();
        pFrom.sendKeys(stringValue);
        assertEquals("",pFrom.getAttribute("value").trim());//проверка на строку

        driver.close();
    }

    @Test
    public void sortMileage(){
        initDriver();
        driver.get("http://olx.ua/transport/legkovye-avtomobili/");
        driver.findElement(mileageLow).click();
        driver.findElements(mileageLowList).get(0).findElement(By.tagName("a")).click();

        driver.findElement(mileageLow).click();
        WebElement mFrom = driver.findElement(mileageLowInput);
        mFrom.clear();
        mFrom.sendKeys("5000");//руками
        driver.close();
    }

    @AfterClass
    public static void destroy(){
        destroyDriver();
    }
}
