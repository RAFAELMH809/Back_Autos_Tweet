FROM openjdk:17-jdk-alpine

LABEL maintainer="zs23004693@estudiantes.uv.mx"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/SpringBootSecurityPostgresqlApplication-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} antlr-api.jar

#RUN
ENTRYPOINT  ["java",  "-Djava.security.egd=file:/dev/./urandom", "-jar", "/antlr-api.jar"]