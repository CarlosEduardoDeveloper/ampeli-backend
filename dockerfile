FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/ampeli-backend-0.0.1-SNAPSHOT.jar app.jar

# Aqui você não expõe fixo, deixa dinâmico
EXPOSE ${PORT}

# Passa a PORT como argumento para o Spring Boot
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
