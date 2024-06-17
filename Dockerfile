FROM openjdk:17-alpine
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY target/*.jar .
ENTRYPOINT ["java","-jar","weddingplatform-0.0.1-SNAPSHOT.jar"]