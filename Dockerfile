FROM openjdk:11 as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:11
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
RUN mkdir ssl
COPY *.jks  /ssl
# COPY *.key  /ssl
# stls
ENV BOOTSTRAP_SERVERS="my-cluster"
# mtls
ENV MENS_A_ENVIAR=2
ENV TOPICO="my-topic"
ENV DELAY_MS=10000
# local
ENV AMBIENTE="docker"
ENV SSLTRUST="/ssl/truststore.jks"
ENV PASSWORD="red"
ENV PROTOCOL="ssl"
#ENV SSLKEY="/ssl/user.jks"
# openshift
#ENV CA_CRT="/ssl/truststore.jks"
#ENV USER_CRT="/ssl/user.jks"
#ENV USER_KEY="/ssl/user-client.key"

ENTRYPOINT ["java","-cp","app:app/lib/*","com/devs4j/devs4jtransactions/ProdDevs4jTransactionsApplication"]