# Use OpenJDK base image
FROM openjdk:17-jdk

# Set working directory
WORKDIR /app

# Copy built jar into container
COPY target/vacancy-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
