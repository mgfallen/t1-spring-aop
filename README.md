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
  - [Запуск тестирования](#запуск-тестирования)
  - [Логирование методов сервисов](#логирование-методов-сервисов)
- [Конфигурация профилей](#конфигурация-профилей)

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

- **Java 22** (openjdk)
- **Maven** (для сборки проекта)
- **Docker** (для интеграционных тестов с TestContainer)
- **PostgreSQL** (для профиля `prod`)

### Запуск приложения

1. **Клонирование репозитория**:
   ```bash
   git clone https://github.com/your-repo/aop-logging-application.git
   cd aop-logging-application
   ```

2. **Сборка приложения:**
    ```bash
    mvn clean install
    ```
    or
    ```bash
    ./mvwn clean install
    ```
4. **Запуск приложения:**
    Для разработки (профиль по умолчанию):
    ```bash
    mvn spring-boot:run
    ```
      or
    ```bash
    ./mvwn spring-boot:run
    ```
    
  Для продакшн-версии:
    ```bash
    mvn spring-boot:run -Dspring.profiles.active=prod
    ```
    or
    ```bash
    ./mvwn spring-boot:run -Dspring.profiles.active=prod
    ```
    
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
### Запуск тестирования ###
Тесты запускаются следующими командами:
  ```bash
  mvn test
  ```
  or
  ```bash
  ./mvnw test
  ```
В проекте реализованы интеграционные и юнит тесты. Юнит тесты работают без контекста спринга, логика сервисов мокируется. Интеграционные тесты полноценно поднимают контекст спринга, поднимают локальную базу данных (H2 для dev, PostgreSQL для prod; дефолтно интеграционные тесты поднимают prod профиль) и полноценно испольняют пользовательский сценарий использования логики сервиса.

### Логирование методов сервисов ### 
После выполнения команды **mvn test** по пути *target/surefire-reports* будут лежать логи отработки тестов. 
Класс `LoggingAspect` содержит несколько аспектов, которые перехватывают вызовы методов в сервисном слое и логируют различные события:
- **Логирование перед выполнением метода**: Логируется начало выполнения метода и его аргументы.
- **Логирование после выполнения метода**: Логируется завершение выполнения метода.
- **Логирование возвращаемого значения метода**: Логируется возвращаемое значение метода.
- **Логирование исключений**: Логируется информация об исключении, если оно было выброшено в методе.

**Пример логирования с помощью LoggingAspect** (по пути *target/surefire-reports/TEST-holding.t.one.aop.integrational.UserServiceIntegrationTests.xml*):
```
<testcase name="testUpdateUserById" classname="holding.t.one.aop.integrational.UserServiceIntegrationTests" time="0.033">
    <system-out><![CDATA[2024-08-13T23:49:04.460+03:00  INFO 4800 --- [           main] h.t.one.aop.aspects.LoggingAspect        : Executing method: User holding.t.one.aop.service.UserService.createUser(User) with arguments: [User(userId=null, name=Peter Parker, email=peter.parker@example.com, orders=null)]
2024-08-13T23:49:04.463+03:00  INFO 4800 --- [           main] h.t.one.aop.aspects.LoggingAspect        : Method User holding.t.one.aop.service.UserService.createUser(User) returned with value: User(userId=c539bf65-0f41-4d7c-b201-f49f7d246b24, name=Peter Parker, email=peter.parker@example.com, orders=null)
2024-08-13T23:49:04.464+03:00  INFO 4800 --- [           main] h.t.one.aop.aspects.LoggingAspect        : Completed method: User holding.t.one.aop.service.UserService.createUser(User)
2024-08-13T23:49:04.464+03:00  INFO 4800 --- [           main] h.t.one.aop.aspects.LoggingAspect        : Executing method: User holding.t.one.aop.service.UserService.updateUserById(UUID,User) with arguments: [c539bf65-0f41-4d7c-b201-f49f7d246b24, User(userId=c539bf65-0f41-4d7c-b201-f49f7d246b24, name=Updated Name, email=updated.email@example.com, orders=null)]
2024-08-13T23:49:04.471+03:00  INFO 4800 --- [           main] h.t.one.aop.aspects.LoggingAspect        : Method User holding.t.one.aop.service.UserService.updateUserById(UUID,User) returned with value: User(userId=c539bf65-0f41-4d7c-b201-f49f7d246b24, name=Updated Name, email=updated.email@example.com, orders=null)
2024-08-13T23:49:04.472+03:00  INFO 4800 --- [           main] h.t.one.aop.aspects.LoggingAspect        : Completed method: User holding.t.one.aop.service.UserService.updateUserById(UUID,User)
]]></system-out>
```

## Конфигурация профилей
  Перед использованием приложение необходимо установить Docker. Приложение поддерживает два профиля:
  - dev: Использует встроенную базу данных H2.
  - prod: Настроен для работы с PostgreSQL. Убедитесь, что установлены следующие переменные окружения:
      - SPRING_DATASOURCE_URL
      - SPRING_DATASOURCE_USERNAME
      - SPRING_DATASOURCE_PASSWORD
   
Вы можете переключаться между профилями с помощью свойства spring.profiles.active. (конфиг меняется в application.properties)
