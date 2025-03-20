# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml to build the JAR
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Grant execute permission to Maven wrapper
RUN chmod +x ./mvnw

# Build the JAR during the Docker build process
RUN ./mvnw clean package -DskipTests

# Copy the generated JAR to the container
COPY target/SalesSavvy-0.0.1-SNAPSHOT.jar SalesSavvy.jar

# Expose the port your application runs on
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "SalesSavvy.jar"]
