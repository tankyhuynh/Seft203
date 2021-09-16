FROM openjdk:11.0.11-9-jre-slim
ARG JAR_FILE=jar/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]