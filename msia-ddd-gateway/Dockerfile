FROM openjdk:17-alpine

MAINTAINER reionchan

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=9000 \
    PROFILES=dev \
    NACOS_ADDR=127.0.0.1:8848

ADD ./target/*.jar /gateway.jar

HEALTHCHECK --interval=5s --timeout=30s CMD wget --no-verbose --tries=1 --spider http://localhost:$PORT/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", \
            "java $JAVA_OPTS -jar /gateway.jar --server.port=$PORT --spring.profiles.active=$PROFILES --spring.cloud.nacos.server-addr=$NACOS_ADDR --spring.cloud.nacos.discovery.server-addr=$NACOS_ADDR --spring.output.ansi.enabled=$SPRING_OUTPUT_ANSI_ENABLED"]

EXPOSE $PORT