package com.qanova.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.qanova.drivers.DriverManager;
import com.qanova.utils.ConfigReader;

public class Hooks {
    @Before
    public void setUp() {
        System.out.println("");
        // Inicializar el driver antes de cada escenario
        DriverManager.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        // Tomar screenshot si el escenario falla
        if (scenario.isFailed() && ConfigReader.getBooleanProperty("take.screenshot.on.failure")) {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        // Cerrar el driver después de cada escenario
        DriverManager.quitDriver();
    }

    @Before("@navegacion")
    public void beforeNavegacion() {
        // Hook especifico para tag @navegacion
        System.out.println("Ejecutando escenario de navegación...");
    }
}
