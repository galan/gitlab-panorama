FROM maven:3.8.5-eclipse-temurin-17-alpine AS build

COPY . /sources
WORKDIR /sources
RUN mvn -B clean verify


FROM eclipse-temurin:17-jre-alpine

COPY --from=build /sources/target/application.jar /project/application.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/project/application.jar"]
