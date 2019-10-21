FROM openjdk:11-jre-slim

ENV JAVA_OPTS=""

RUN addgroup -g 1000 -S berightthere && \
    adduser -u 1000 -S berightthere -G berightthere
USER berightthere

VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]