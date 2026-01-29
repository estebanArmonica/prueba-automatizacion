# Etapa de construcción
FROM gradle:8.14-jdk17 AS builder
WORKDIR /app

# Copiar archivos de Gradle primero (cache optimizado)
COPY gradle gradle
COPY gradlew .
COPY gradle.properties .
COPY build.gradle .
COPY settings.gradle .

# Dar permisos al gradlew
RUN chmod +x gradlew

# Descargar dependencias
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente
COPY src src

# Construir la aplicación
RUN ./gradlew clean build --no-daemon

# Etapa de ejecución
FROM openjdk:17-slim
WORKDIR /app

# Instalar dependencias necesarias para Chrome
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    unzip \
    xvfb \
    libxi6 \
    libgconf-2-4 \
    libxss1 \
    libappindicator1 \
    libindicator7 \
    fonts-liberation \
    libasound2 \
    libnspr4 \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libxrender1 \
    libxtst6 \
    lsb-release \
    xdg-utils \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Instalar Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

# Crear usuario no-root para seguridad
RUN groupadd -r qanova && useradd -r -g qanova -G audio,video qanova \
    && mkdir -p /home/qanova && chown -R qanova:qanova /home/qanova

# Copiar artefactos de construcción
COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /app/src/test src/test
COPY --from=builder /app/build/classes/java/test build/classes/java/test
COPY --from=builder /app/build/resources/test build/resources/test

# Cambiar a usuario no-root
USER qanova

# Variables de entorno
ENV BROWSER=chrome
ENV HEADLESS=true
ENV IMPLICIT_WAIT=10
ENV PAGE_LOAD_TIMEOUT=30

# Puerto para debugging (opcional)
EXPOSE 5005

# Comando para ejecutar tests
ENTRYPOINT ["java", "-jar", "app.jar", "test"]