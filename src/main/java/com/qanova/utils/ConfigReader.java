package com.qanova.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
* esta clase es la importante ya que de este inicializamos y cargamos cada propiedad del properties
* especificando la rutal del archivo properties
*/
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/application.properties";
    // Alternativa: "src/main/resources/application.properties" si prefieres mantener un archivo principal

    /*
    * este static tiene el fin de poder realizar toda la carga de las variables del properties
    * si en caso de que no realice la carga completa o exista algun tipo de error dentro de la realización
    * de la carga se lanzara una exception
    */
    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo de configuración: " + CONFIG_FILE_PATH, e);
        }
    }

    /*
    * El fin que tiene este metodo es poder rescatar todas las variables cargadas del properties para empezar
    * ya a realizar automatizaciones
    */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("La propiedad '" + key + "' no está definida en el archivo de configuración");
        }
        return value.trim();
    }

    // Métodos específicos para tipos de datos
    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public static long getLongProperty(String key) {
        return Long.parseLong(getProperty(key));
    }

    // Métodos específicos para configuraciones comunes
    public static String getBrowser() {
        return getProperty("browser");
    }

    public static boolean isHeadless() {
        return getBooleanProperty("headless");
    }

    public static int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }

    public static int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getTestUrl() {
        return getProperty("test.url");
    }
}
