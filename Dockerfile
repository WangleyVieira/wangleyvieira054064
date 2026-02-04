# Estágio 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Estágio 2: Runtime (imagem final)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia apenas o JAR gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
