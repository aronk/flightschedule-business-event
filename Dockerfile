FROM openjdk:8-jre-alpine

VOLUME /tmp

ARG BUILD_LABEL=dirty-build
ARG JAR_FILE=/target/flightschedule-business-event-*.jar

ADD ${JAR_FILE} app.jar

EXPOSE 8080

CMD ["sh", "-c", "exec java ${APP_JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar ${APP_OPTS}"]

LABEL name="aronk/flightschedule-business-event"
LABEL version="1.0.0"
LABEL release="${BUILD_LABEL}"
