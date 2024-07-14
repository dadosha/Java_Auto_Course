Java Auto Course [![Java CI with Gradle](https://github.com/dadosha/Java_Auto_Course/actions/workflows/gradle.yml/badge.svg)](https://github.com/dadosha/Java_Auto_Course/actions/workflows/gradle.yml)

# Как запустить проект:
## Перед запуском проекта и автотестов, убедитесь, что установлены:

- [Docker](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md)
- [Docker Compose](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md)
- [Java Development Kit (JDK) 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (рекомендуется JDK 11)
- Плагин Lombok 


## Установка и настройка
### 1. Клонирование репозитория
Склонируйте репозиторий на локальную машину:
- git clone https://github.com/dadosha/Java_Auto_Course.git 
### 2. Запуск MySQL с помощью Docker
- docker-compose up
### 3. Запуск приложения
- java -jar .\artifacts\aqa-shop.jar
### 4. Запуск тестов
- ./gradlew clean test -D selenide.headless=true
### 5. Просмотреть отчет
- ./gradlew allureServe

После прохождения всех тестов можно остановить приложение и контейнеры docker


- [Plan.md](Plan.md)