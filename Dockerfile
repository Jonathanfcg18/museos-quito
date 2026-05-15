# Etapa 1: compilar el proyecto
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: desplegar en Tomcat
FROM tomcat:11.0-jdk21
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/museosquito.war /usr/local/tomcat/webapps/ROOT.war

# Puerto que Railway espera
EXPOSE 8080
CMD ["catalina.sh", "run"]