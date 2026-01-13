FROM eclipse-temurin:21-jdk

USER root

EXPOSE 8081

# Sposta il jar in macchina
COPY /target/magazzino-app.jar /app.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5900", "-jar", "app.jar"]
