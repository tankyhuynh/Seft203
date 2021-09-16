# SEFT-203

## Prerequisite

* Docker (https://docs.docker.com/get-docker)
* docker-compose (https://docs.docker.com/compose/install)

## How to run

> docker-compose up --build

Then, go to documentation url

> http://localhost:8080/swagger-ui.html

## Login API

User/pass: admin/Admin@123

## Notes

jar file comes from `jar` folder, if you need to run a newer version, copy new jarfile to that location.

## Build (for Java only)

Change "11.0.10.j9-adpt" to your java version if you use sdk

> sdk use java 11.0.10.j9-adpt

> ./gradlew build

For skipping tests (one test intentionally to fail)

> ./gradlew build -x test

## Running tests

> ./gradlew test
