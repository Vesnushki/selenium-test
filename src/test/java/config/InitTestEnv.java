package config;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by a418 on 3/5/16.
 */
public class InitTestEnv {

    protected static Properties prop;
    protected static WebDriver  driver;
    protected static Logger log;

    private final static String PROP_FILE = "config.properties";


    static {
        prop = System.getProperties();
        try (InputStream in = InitTestEnv.class.getClassLoader().getResourceAsStream(PROP_FILE)) {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected  static void initDriver(){
        String browser = prop.getProperty("browser");
        switch (browser){
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "chrome":
                System.out.println(prop.getProperty("chrome.val"));
                System.setProperty(prop.getProperty("chrome.key"), prop.getProperty("chrome.val"));
                driver = new ChromeDriver();
                break;
        }
        WebDriver.Window window = driver.manage().window();
        window.setPosition(new Point(0,0));
        window.setSize(new Dimension(1200,window.getSize().getHeight()));
    }

    protected static void destroyDriver() {}

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            super.succeeded(description);
            log.info(description);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            super.failed(e, description);
            log.error(description,e);
        }
    };
}
