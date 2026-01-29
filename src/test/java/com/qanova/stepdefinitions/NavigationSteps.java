package com.qanova.stepdefinitions;

import com.qanova.drivers.DriverManager;
import com.qanova.pages.HomePage;
import com.qanova.utils.HumanBehavior;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

public class NavigationSteps {
    private HomePage homePage;
    private WebDriverWait wait;

    public NavigationSteps() {
        this.homePage = new HomePage();
        this.wait = new WebDriverWait(DriverManager.getDriver(),
                Duration.ofSeconds(15));
    }

    /*
    * aplicamos en esta clase el escenario de Give-When-Then usando el lenguaje de Gherkin
    * en esta clase describimos el comportamiento de la pespectiva del Usuario final
    */

    @Given("que estoy en la página principal de Google")
    public void que_estoy_en_la_pagina_principal_de_google() {
        // se dirige a google con retraso humano
        HumanBehavior.randomDelay();

        // Ir a Google
        DriverManager.getDriver().get("https://www.google.com");
        HumanBehavior.randomDelay();

        // Aceptar cookies
        homePage.acceptCookiesIfPresent();

        // Esperar a que cargue
        wait.until(ExpectedConditions.titleContains("Google"));

        // Verificar que estamos en Google
        String title = DriverManager.getDriver().getTitle();
        System.out.println("Título de la página: " + title);
        assertThat(title).contains("Google");
    }

    @Then("el logo debe ser visible")
    public void el_logo_debe_ser_visible() {
        HumanBehavior.shortDelay();
        boolean isLogoVisible = homePage.isLogoVisible();
        System.out.println("Logo visible: " + isLogoVisible);
        assertThat(isLogoVisible).isTrue();
    }

    @Then("el campo de búsqueda debe estar presente")
    public void el_campo_de_busqueda_debe_estar_presente() {
        HumanBehavior.shortDelay();
        boolean isPageLoaded = homePage.isPageLoaded();
        System.out.println("Página cargada: " + isPageLoaded);
        assertThat(isPageLoaded).isTrue();
    }

    @Then("el placeholder debe ser {string}")
    public void el_placeholder_debe_ser(String placeholderEsperado) {
        HumanBehavior.shortDelay();
        String placeholderActual = homePage.getSearchPlaceholder();
        System.out.println("Placeholder actual: " + placeholderActual);
        System.out.println("Placeholder esperado: " + placeholderEsperado);
        assertThat(placeholderActual).isEqualTo(placeholderEsperado);
    }

    @When("ingreso {string} en el campo de búsqueda")
    public void ingreso_en_el_campo_de_busqueda(String texto) {
        HumanBehavior.randomDelay();

        // Usar el método de la página
        homePage.enterSearchText(texto);

        // Pequeña pausa antes de buscar
        HumanBehavior.shortDelay();
    }

    @When("hago clic en el botón de buscar")
    public void hago_clic_en_el_boton_de_buscar() {
        HumanBehavior.randomDelay();

        // Usar el método de la página que espera resultados
        homePage.clickSearchButton();

        // Esperar explícitamente a que cambie la URL
        wait.until(ExpectedConditions.urlContains("search"));

        System.out.println("URL después de buscar: " + DriverManager.getDriver().getCurrentUrl());
    }

    @Then("la URL debe contener {string}")
    public void la_url_debe_contener(String textoUrl) {
        HumanBehavior.shortDelay();
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        System.out.println("URL actual: " + currentUrl);
        System.out.println("URL debe contener: " + textoUrl);
        assertThat(currentUrl).contains(textoUrl);
    }

    @Then("el título debe contener {string}")
    public void el_titulo_debe_contener(String textoEsperado) {
        HumanBehavior.shortDelay();
        String title = DriverManager.getDriver().getTitle();
        System.out.println("Título actual: " + title);
        System.out.println("Título debe contener: " + textoEsperado);
        assertThat(title).contains(textoEsperado);
    }
}
