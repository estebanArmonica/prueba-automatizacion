# Multi-stage build para GitHub Actions
FROM gradle:8.14-jdk17-alpine AS builder

WORKDIR /app

# Copiar archivos necesarios
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Dar permisos
RUN chmod +x gradlew

# Descargar dependencias primero (cache eficiente)
RUN ./gradlew dependencies --no-daemon

# Construir proyecto
RUN ./gradlew clean build --no-daemon -x test

# Imagen final para tests
FROM openjdk:17-jdk-slim

WORKDIR /app

# Instalar dependencias necesarias para Chrome en Linux
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    gnupg \
    ca-certificates \
    --no-install-recommends && \
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update && \
    apt-get install -y \
    google-chrome-stable \
    fonts-ipafont-gothic \
    fonts-wqy-zenhei \
    fonts-thai-tlwg \
    fonts-kacst \
    fonts-freefont-ttf \
    libxss1 \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Instalar ChromeDriver que coincida con Chrome
RUN CHROME_VERSION=$(google-chrome --version | grep -o '[0-9]*\.[0-9]*\.[0-9]*') && \
    echo "Chrome version: $CHROME_VERSION" && \
    wget -q -O /tmp/chromedriver.zip "https://chromedriver.storage.googleapis.com/$(echo $CHROME_VERSION | cut -d'.' -f1-3)/chromedriver_linux64.zip" && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip && \
    chmod +x /usr/local/bin/chromedriver

# Crear usuario no-root
RUN useradd -m -u 1000 -s /bin/bash qanova
USER qanova

# Copiar artefactos construidos
COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /app/src/test src/test
COPY --from=builder /app/build/classes/java/test build/classes/java/test
COPY --from=builder /app/build/resources/test build/resources/test

# Variables de entorno
ENV BROWSER=chrome
ENV HEADLESS=true
ENV DISPLAY=:99
ENV CHROME_BIN=/usr/bin/google-chrome
ENV CHROMEDRIVER_PATH=/usr/local/bin/chromedriver

# Comando para ejecutar tests
CMD ["java", "-jar", "app.jar", "test"]