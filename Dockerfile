FROM adoptopenjdk/openjdk11:alpine

RUN mkdir /opt/app

COPY build/libs/vertx-config-122-1.0.0-SNAPSHOT-fat.jar /opt/app/app.jar

CMD ["java", "-jar", "/opt/app/app.jar"]
