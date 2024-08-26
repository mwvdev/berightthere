FROM eclipse-temurin:21-jre

ENV JAVA_OPTS=""

RUN groupadd --system --gid 1001 berightthere
RUN useradd --system --uid 1001 --gid 1001 berightthere
USER berightthere

VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]