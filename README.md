Игра "жизнь"
============

### Алгоритм игры предоставлен в 2-х вариантах

* standalone		- режим игры в автономном режиме
* REST сервис		- серверная часть игры, отдающая координаты обектов для клиентов

### Запуск

#### Запуск игры в автономном режиме
```
java -jar life-standalone-1.0-SNAPSHOT.jar
```
По-умолчанию, игра создаст объекты в случайном порядке. Также, можно запустить отдельные фигуры. Для этого, необходимо в параметрах запуска, передать одно из значений
* GLIDE		- планер
* SHIP		- корабль
* EIGHT		- восьмерка
* APIARY	- пасека
* RANDOM	- случайное распределение
```
java -jar life-standalone-1.0-SNAPSHOT.jar glide
```


#### Запуск серверной части(REST сервис)

Для запуска серверной части(REST сервис), необходим будет сервер osgi(Apache Karaf, Apache Servicemix, Red Hat Fuse).
Необходимо скачать и запустить сервер, следуя инструкциям выбранного сервера

Для установки игры на сервер, можно воспользоваться 2 способами:
* скопировать life-facade-1.0-SNAPSHOT.jar и life-db-1.0-SNAPSHOT.jar в папку deploy
* установить через features(предпочтительнее)

```
feature:repo-add mvn:ru.erdi/life-installer/1.0-SNAPSHOT/xml/features
feature:install life-camel-dependencies
feature:install life-game
```

REST сервис, использует следующие библиотеки:
* camel-cxf
* joda-time
* gson(используется модифицированная версия GSON для работы в среде osgi. Неободимо установить данную библиотеку в локальный репозиторий. находится в проекте /life-installer/src/main/lib/gson-osgi-2.8.0.jar)

Для сохранения состояний объектов, используется БД Sqlite. Предварительной установки и настройки не требуется.

Описание REST сервиса формируется автоматически согласно протоколу swagger и находится в проекте - /life-facade/generated/
* /swagger-ui	- в данном каталоге описание в формате yaml и json
* document.html	- веб версия описания сервиса

#### Примеры запросов
POST http://localhost:8081/life/rest/seventhday

request

```json
"GLIDE"
```
response

```json
{
  "key": "15b17844-a4c6-40a1-804a-e5bb4fb51676",
  "age": 1,
  "entitys": [
    {
      "x": 250,
      "y": 250
    },
    {
      "x": 270,
      "y": 240
    },
    {
      "x": 260,
      "y": 230
    },
    {
      "x": 270,
      "y": 250
    },
    {
      "x": 260,
      "y": 250
    }
  ]
}
```
POST http://localhost:8081/life/rest/nextday


request

```json
{
  "key": "15b17844-a4c6-40a1-804a-e5bb4fb51676",
  "age": 1
}
```
response

```json
{
  "key": "15b17844-a4c6-40a1-804a-e5bb4fb51676",
  "age": 2,
  "entitys": [
    {
      "x": 270,
      "y": 240
    },
    {
      "x": 250,
      "y": 240
    },
    {
      "x": 260,
      "y": 260
    },
    {
      "x": 270,
      "y": 250
    },
    {
      "x": 260,
      "y": 250
    }
  ]
}
```
