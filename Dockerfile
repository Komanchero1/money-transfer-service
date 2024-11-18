# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл jar в контейнер
COPY target/money-transfer-service-0.0.1-SNAPSHOT.jar app.jar


# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]