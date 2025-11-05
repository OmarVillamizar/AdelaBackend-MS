### STAGE 1: BUILD ###
FROM maven:3-amazoncorretto-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

### STAGE 2: DEPLOY ###
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
VOLUME /app/uploads
ENTRYPOINT ["java","-jar","/app/app.jar"]
