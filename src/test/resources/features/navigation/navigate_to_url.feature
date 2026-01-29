Feature: Pruebas de HomePage usando Page Object Model
Como usuario de Google
Quiero interactuar con la página principal
Para validar que el Page Object Model funciona correctamente

  @google
  Scenario: Verificar elementos de la página principal
    Given que estoy en la página principal de Google
    Then el logo debe ser visible
    And el campo de búsqueda debe estar presente
    And el placeholder debe ser "Buscar con Google"

  @google @busqueda
  Scenario: Realizar una búsqueda en Google
    Given que estoy en la página principal de Google
    When ingreso "Selenium WebDriver" en el campo de búsqueda
    And hago clic en el botón de buscar
    Then la URL debe contener "/search"
    And el título debe contener "Selenium WebDriver"
