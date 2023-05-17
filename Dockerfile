FROM openjdk:8
VOLUME /tmp
WORKDIR app
COPY target/*.jar app.jar
COPY docs/MovieWebsiteJson.json .
ENTRYPOINT ["java", "-jar", "app.jar"]


