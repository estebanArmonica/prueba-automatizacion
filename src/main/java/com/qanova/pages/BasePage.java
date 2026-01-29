package com.qanova.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import com.qanova.drivers.DriverManager;
import com.qanova.utils.ConfigReader;

public abstract class BasePage {
    protected WebDriver driver;

    // en este class basePage utilizamos el patron de PageFactory para especificar el inicio de los elementos
    public BasePage() {
        this.driver = DriverManager.getDriver();
        // Inicializa PageFactory con timeout de 10 segundos para encontrar elementos
        PageFactory.initElements(
                new AjaxElementLocatorFactory(driver, ConfigReader.getIntProperty("explicit.wait")),
                this
        );
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void goBack() {
        driver.navigate().back();
    }

    public void goForward() {
        driver.navigate().forward();
    }

    /*
    * junto a esto tambien rescatamos algunos datos como la url del navegador, el page title (titulo de la pagina)
    * datos que estan en el archivo properties (base.url)
    */
}
