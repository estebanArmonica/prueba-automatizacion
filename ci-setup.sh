#!/bin/bash

# Script de configuración para CI/CD

echo "🔧 Setting up CI/CD environment..."

# Instalar dependencias del sistema
sudo apt-get update
sudo apt-get install -y \
    xvfb \
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
    libxi6 \
    libxrandr2 \
    libxrender1 \
    libxtst6 \
    lsb-release \
    xdg-utils

# Configurar Xvfb
export DISPLAY=:99
Xvfb :99 -screen 0 1920x1080x24 > /dev/null 2>&1 &
sleep 3

echo "✅ CI/CD environment setup complete"