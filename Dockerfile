# Use a base image with JDK and Maven
FROM maven:3.9.6-eclipse-temurin-21 as builder

# Set working directory inside container
WORKDIR /app

# Copy all files to the container
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# -------------------------

# Use a runtime image with JDK
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy only the built WAR from previous stage
COPY --from=builder /app/target/chatapplication-1.0-SNAPSHOT.war /app/app.war

# Install and run Payara Micro (lightweight Java EE server)
RUN curl -O https://repo1.maven.org/maven2/fish/payara/distributions/payara-micro/6.2024.3/payara-micro-6.2024.3.jar

# Expose port (Payara Micro uses 8080 by default)
EXPOSE 8080

# Run the WAR using Payara Micro
CMD ["java", "-jar", "payara-micro-6.2024.3.jar", "--deploy", "app.war"]
