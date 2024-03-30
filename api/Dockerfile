FROM openjdk:latest
WORKDIR /app
COPY ./target/parser-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "parser-0.0.1-SNAPSHOT.jar"]