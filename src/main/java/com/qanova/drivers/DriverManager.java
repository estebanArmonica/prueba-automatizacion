package com.qanova.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import com.qanova.utils.ConfigReader;
import java.time.Duration;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    // establecemos la variables de WebDriver de Selenium
    private static WebDriver driver;

    private DriverManager() {}

    /*
    * creamos un metodo get el cual realizara casos esto se basa en el caso de que el usuario
    * este utilizando un navegador web diferentes que no sea el Chrome
    * (Opera, Firefox, etc)
    */
    public static WebDriver getDriver() {
        if (driver == null) {

            // en este string llamamos al configReader el cual detectara cual es el browser utilizado
            String browser = ConfigReader.getBrowser();
            boolean headless = ConfigReader.isHeadless();

            // cuando se escoga un browser le especificamos que realice la busqueda pero por nombres en minusculas
            switch (browser.toLowerCase()) {
                case "chrome": // asi como lo tenemos en cada uno de case
                    driver = createChromeDriver(headless);
                    break;
                case "firefox":
                    driver = createFirefoxDriver(headless);
                    break;
                default:
                    /*
                    * En caso de error de cualquier tipo, se realizara una exception
                    * en este caso de que el navegador no es soportado para la prueba
                    */
                    throw new IllegalArgumentException("Navegador no soportado: " + browser);
            }

            // Configurar timeouts desde properties
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));

            // Maximizar ventana si no está en headless
            if (!headless) {
                driver.manage().window().maximize();
            }
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Configuraciones para evitar detección como bot
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Argumentos comunes
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        // Remover notificaciones de automation
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 2); // Bloquear notificaciones
        options.setExperimentalOption("prefs", prefs);

        // User-Agent personalizado
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
        options.addArguments("--user-agent=" + userAgent);

        // Headless mode
        if (headless) {
            options.addArguments("--headless=new"); // Nueva sintaxis para headless
        } else {
            options.addArguments("--start-maximized");
        }

        // Deshabilitar extensiones
        options.addArguments("--disable-extensions");

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        // Configurar para evitar detección
        options.addPreference("dom.webdriver.enabled", false);
        options.addPreference("useAutomationExtension", false);

        return new FirefoxDriver(options);
    }

    // Metodo para obtener el driver sin crear uno nuevo (para validación)
    public static WebDriver getExistingDriver() {
        return driver;
    }
}
