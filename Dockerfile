FROM openjdk:11-jre-slim

ENV JAVA_OPTS=""

RUN addgroup --system --gid 1000 berightthere && \
    adduser --system --uid 1000 --gid 1000 berightthere
USER berightthere

VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]