FROM openjdk:8u121-jre-alpine

WORKDIR /var/dropwizard-rest-stub

ADD target/dropwizard-blog-0.0.1-SNAPSHOT.jar /var/dropwizard-rest-stub/dropwizard-rest-stub.jar
ADD src/main/resources/application.yml /var/dropwizard-rest-stub/config.yml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "dropwizard-rest-stub.jar", "server", "config.yml"]
