# Build stage
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /target/hostel-0.0.1-SNAPSHOT.jar hostel.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","hostel.jar"]
