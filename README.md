# t1-spring-aop
Задание 1


# Приложение для Логирования с использованием AOP

Этот проект является приложением на Spring Boot, демонстрирующим использование аспектно-ориентированного программирования (AOP) для логирования в слое сервисов. Оно включает в себя функционал для управления пользователями и их заказами с базовыми CRUD-операциями.

## Оглавление
- [Обзор](#обзор)
- [Структура проекта](#структура-проекта)
- [Начало работы](#начало-работы)
  - [Требования](#требования)
  - [Запуск приложения](#запуск-приложения)
  - [Консоль базы данных H2](#консоль-базы-данных-h2)
- [Логирование с использованием AOP](#логирование-с-использованием-aop)
- [REST API Эндпоинты](#rest-api-эндпоинты)
- [Обработка исключений](#обработка-исключений)
- [Тестирование](#тестирование)
- [Конфигурация профилей](#конфигурация-профилей)
- [Вклад в проект](#вклад-в-проект)
- [Лицензия](#лицензия)

## Обзор

Это приложение демонстрирует:
- **Логирование на основе AOP**: Использование Spring AOP для логирования вызовов методов, включая параметры методов, возвращаемые значения и исключения.
- **CRUD-операции**: Базовые операции Create, Read, Update и Delete для сущностей `User` и `Order`.
- **REST API**: Предоставление RESTful эндпоинтов для взаимодействия с сущностями `User` и `Order`.
- **Обработка исключений**: Пользовательские исключения для ошибок, связанных с доменом.
- **Конфигурация профилей**: Поддержка профилей `dev` (с использованием встроенной базы данных H2) и `prod` (с использованием PostgreSQL).

## Структура проекта

Проект следует типичной структуре Spring Boot:

- `holding.t.one.aop`: Основной пакет.
  - `controllers`: REST-контроллеры для обработки HTTP-запросов.
  - `model`: JPA-сущности (`User`, `Order`).
  - `repository`: JPA-репозитории для доступа к данным.
  - `service`: Сервисный слой для бизнес-логики.
  - `aspects`: Аспекты AOP для логирования.
  - `exceptions`: Пользовательские исключения для ошибок, связанных с доменом.
  - `AopApplication`: Основной класс приложения.

## Начало работы

### Требования

- **Java 17** или выше
- **Maven** (для сборки проекта)
- **PostgreSQL** (для профиля `prod`)

### Запуск приложения

1. **Клонирование репозитория**:
   ```bash
   git clone https://github.com/your-repo/aop-logging-application.git
   cd aop-logging-application

2. **Сборка приложения:**
    ```bash
    mvn clean install
3. **Запуск приложения:**
    Для разработки (профиль по умолчанию):
    ```bash
    mvn spring-boot:run

    Для продакшн-версии:
    ```bash
    mvn spring-boot:run -Dspring.profiles.active=prod
  ### Консоль базы данных H2
    Если вы запускаете приложение в профиле dev, вы можете получить доступ к консоли базы данных H2 по адресу:
      URL: http://localhost:8080/h2-console
      JDBC URL: jdbc:h2:mem:testdb
      Имя пользователя: user
      Пароль: pass
## Логирование с использованием AOP
  - Аспект логирования определен в классе LoggingAspect в пакете aspects. Он использует различные аннотации AOP для перехвата вызовов методов в сервисном слое и логирования:
  - Имя метода и аргументы (@Before)
  - Завершение метода (@After)
  - Возвращаемые значения (@AfterReturning)
  - Исключения (@AfterThrowing)

## REST API Эндпоинты
    Эндпоинты для пользователя
    - GET /users/{id}: Получить пользователя по ID.
    - POST /users: Создать нового пользователя.
    - DELETE /users/{id}: Удалить пользователя по ID.
    - PUT /users/{id}: Обновить пользователя по ID.
    - POST /users/{id}/orders: Добавить заказ пользователю.
    - GET /users/all: Получить всех пользователей.
    Эндпоинты для заказов
    - GET /orders/{id}: Получить заказ по ID.
    - POST /orders: Создать новый заказ.
    - DELETE /orders/{id}: Удалить заказ по ID.
    - PUT /orders/{id}: Обновить заказ по ID.
    - GET /orders/all: Получить все заказы.
    
## Обработка исключений
   Пользовательские исключения (UserDomainException, OrderDomainException) используются для обработки ошибок, связанных с доменом, например, когда пользователь или заказ не найден. Эти исключения логируются с помощью совета @AfterThrowing в LoggingAspect.

## Тестирование
    Для запуска юнит тестов (без контекста спринга) используйте следующую команду:
      ```bash
      mvn test
Этот проект включает интеграционные тесты, которые взаимодействуют с реальной базой данных H2 для проверки функциональности сервисов и репозиториев.

## Конфигурация профилей
    Перед использованием приложение необходимо установить Docker. Приложение поддерживает два профиля:
    - dev: Использует встроенную базу данных H2.
    - prod: Настроен для работы с PostgreSQL. Убедитесь, что установлены следующие переменные окружения:
        SPRING_DATASOURCE_URL
        SPRING_DATASOURCE_USERNAME
        SPRING_DATASOURCE_PASSWORD
    Вы можете переключаться между профилями с помощью свойства spring.profiles.active. (конфиг меняется в application.properties)
