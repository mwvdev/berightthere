FROM openjdk:8-jre-alpine

RUN groupadd -r berightthere --gid=999 && useradd -r -g postgres --uid=999 berightthere
USER berightthere

VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]