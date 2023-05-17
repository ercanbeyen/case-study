FROM openjdk:8
VOLUME /tmp
WORKDIR app
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


