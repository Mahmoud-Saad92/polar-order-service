#stage: build
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /code/build

# Copy pom.xml and download dependencies first (caching benefits)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn dependency:go-offline -B

# Copy source code and build
COPY src/ ./src/
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn clean package -DskipITs=true

#stage: extract
FROM eclipse-temurin:21.0.5_11-jre AS extract
WORKDIR /usr/bin
COPY --from=builder /code/build/target/order-service-0.0.1.jar ./app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher

#stage: run
FROM gcr.io/distroless/java21-debian12:nonroot

# Set working directory
WORKDIR /app

# Create non-root user is already handled by distroless:nonroot
# Copy application layers in order of change frequency (least to most frequently changed)
COPY --from=extract --chown=nonroot:nonroot /usr/bin/app/dependencies/ ./
COPY --from=extract --chown=nonroot:nonroot /usr/bin/app/spring-boot-loader/ ./
COPY --from=extract --chown=nonroot:nonroot /usr/bin/app/snapshot-dependencies/ ./
COPY --from=extract --chown=nonroot:nonroot /usr/bin/app/application/ ./

# Set JVM options for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom"

# Use exec form for proper signal handling
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.launch.JarLauncher"]