FROM  maven:3.8.5-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven configuration file
COPY pom.xml /app/pom.xml

COPY sonar-project.properties /app/sonar-project.properties

# Copy the source code
COPY src /app/src

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/RealEstateRental-0.0.1.jar /app/app.jar


# Expose the port the app will run on
EXPOSE 8080

# Run the JAR application
CMD ["java", "-jar", "app.jar"]