# Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --create-home --shell /bin/bash sinapta
COPY --from=build /app/target/ecossistema.jar app.jar
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh && \
    mkdir -p /data/storage && \
    chown -R sinapta:sinapta /app /data

USER sinapta
EXPOSE 8080
ENTRYPOINT ["/app/docker-entrypoint.sh"]
