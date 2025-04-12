# Сборка приложения
FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app

COPY pom.xml .
COPY application/application/pom.xml ./application/application/pom.xml
COPY track/track/pom.xml ./track/track/pom.xml

# 2. Скачиваем зависимости для всего проекта (без сборки)
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline

# 3. Копируем исходные коды
COPY application/application/src ./application/application/src
COPY track/track/src ./track/track/src

# 4. Собираем весь проект
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Финальный образ
FROM openjdk:21-jdk-slim
WORKDIR /app

# Копируем собранный JAR из модуля application
COPY --from=build /app/application/application/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
