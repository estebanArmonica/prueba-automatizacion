package com.qanova.pages;

import com.qanova.utils.HumanBehavior;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.qanova.drivers.DriverManager;
import java.time.Duration;
import java.util.List;

public class HomePage extends BasePage{
    // Localizadores usando PageFactory

    @FindBy(how = How.NAME, using = "q")
    private WebElement searchInput;

    @FindBy(how = How.CSS, using = "textarea[name='q'], input[name='q']")
    private WebElement searchInputAlt;

    // Botón de búsqueda
    @FindBy(how = How.NAME, using = "btnK")
    private WebElement searchButton;

    @FindBy(how = How.CSS, using = "input[value='Buscar con Google'], input[type='submit']")
    private List<WebElement> searchButtons;

    // Logo - múltiples selectores
    @FindBy(how = How.CSS, using = "img[alt='Google'], div#logo img, svg[aria-label='Google']")
    private List<WebElement> logos;

    @FindBy(how = How.XPATH, using = "//*[@alt='Google' or contains(@aria-label, 'Google')]")
    private List<WebElement> googleLogos;

    // Constructor
    public HomePage() {
        super(); // Llama al constructor de BasePage que inicializa PageFactory
    }

    // aqui empiezan los metodos de acción - Interfaz publica de la pagina

    /**
     * Escribe texto en el campo de búsqueda
     */
    public void enterSearchText(String text) {
        WebElement input = getSearchInput();
        waitForElementToBeVisible(input);
        input.clear();
        input.sendKeys(text);
    }

    /**
     * Hace clic en el botón de búsqueda
     */
    public void clickSearchButton() {
        WebElement button = getSearchButton();
        waitForElementToBeClickable(button);
        button.click();
    }

    /**
     * Realiza una búsqueda completa
     */
    public void searchFor(String text) {
        enterSearchText(text);
        clickSearchButton();

        // Esperar a que cambie la URL
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(),
                Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("search"));
    }

    /**
     * Verifica si el logo es visible
     */
    public boolean isLogoVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(),
                    Duration.ofSeconds(10));

            // Intentar con diferentes selectores
            for (WebElement logo : logos) {
                try {
                    wait.until(ExpectedConditions.visibilityOf(logo));
                    if (logo.isDisplayed()) {
                        return true;
                    }
                } catch (Exception e) {
                    // Continuar con el siguiente
                }
            }

            // Intentar con XPath
            for (WebElement logo : googleLogos) {
                try {
                    wait.until(ExpectedConditions.visibilityOf(logo));
                    if (logo.isDisplayed()) {
                        return true;
                    }
                } catch (Exception e) {
                    // Continuar
                }
            }

            System.out.println("No se encontró ningún logo visible");
            return false;
        } catch (Exception e) {
            System.out.println("Logo no encontrado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el texto del placeholder del campo de búsqueda
     */
    public String getSearchPlaceholder() {
        WebElement input = getSearchInput();
        waitForElementToBeVisible(input);
        return input.getAttribute("placeholder");
    }

    /**
     * Verifica si la página está cargada
     */
    public boolean isPageLoaded() {
        try {
            WebElement input = getSearchInput();
            return input.isDisplayed() && input.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el título actual de la página
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /*
    * Obtiene las cookies y las accepta
    */
    public void acceptCookiesIfPresent() {
        try {
            // Intentar diferentes selectores de botón de cookies
            String[] cookieSelectors = {
                    "//button[contains(., 'Aceptar todo')]",
                    "//button[div[text()='Aceptar todo']]",
                    "//button[@id='L2AGLb']",
                    "//button[contains(@aria-label, 'Aceptar')]",
                    "//button[contains(text(), 'Acepto')]",
                    "//button[contains(text(), 'Aceptar')]",
                    "button#W0wltc", // Selector específico de Google
                    "div[role='dialog'] button:last-child"
            };

            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(),
                    Duration.ofSeconds(5));

            for (String selector : cookieSelectors) {
                try {
                    List<WebElement> cookieButtons = DriverManager.getDriver()
                            .findElements(By.xpath(selector));

                    for (WebElement cookieButton : cookieButtons) {
                        if (cookieButton.isDisplayed() && cookieButton.isEnabled()) {
                            cookieButton.click();
                            System.out.println("Cookies aceptadas usando selector: " + selector);
                            HumanBehavior.shortDelay();
                            return;
                        }
                    }
                } catch (Exception e) {
                    // Continuar
                }

                System.out.println("No se encontró banner de cookies o ya está aceptado");
            }
        } catch (Exception e) {
            System.out.println("No se encontró banner de cookies o ya está aceptado");
            System.out.println("error: " + e.getMessage());
        }
    }

    // Métodos helper privados
    private void waitForElementToBeVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(),
                Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void waitForElementToBeClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(),
                Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Obtiene el campo de búsqueda (con fallback)
     */
    private WebElement getSearchInput() {
        try {
            if (searchInput.isDisplayed()) {
                return searchInput;
            }
        } catch (Exception e) {
            // Intentar con alternativo
        }
        return searchInputAlt;
    }

    /**
     * Obtiene el botón de búsqueda (con fallback)
     */
    private WebElement getSearchButton() {
        try {
            if (searchButton.isDisplayed()) {
                return searchButton;
            }
        } catch (Exception e) {

        }

        for (WebElement button : searchButtons) {
            try {
                if (button.isDisplayed() && button.isEnabled()) {
                    return button;
                }
            } catch (Exception e) {
                // Continuar
            }
        }

        // Si no encuentra, devolver el primero
        return searchButton;
    }
}
