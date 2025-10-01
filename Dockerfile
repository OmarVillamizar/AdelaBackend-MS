### STAGE 1:BUILD ###
FROM maven:3-amazoncorretto-17-alpine AS build

# Create app directory
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application (Maven descargará las dependencias automáticamente)
RUN mvn clean package -DskipTests


### STAGE 2:DEPLOY ###
FROM amazoncorretto:17-alpine AS deploy

# Create app directory
WORKDIR /app

# Copy the built jar file
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8081

# Create a volume for the uploads
VOLUME /app/uploads

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]