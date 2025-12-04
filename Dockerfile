# Étape de build
FROM docker.io/maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /build

# Définir correctement l'encodage UTF-8
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

COPY . .

# Nettoyer le fichier application.properties avant le build si nécessaire
RUN if [ -f src/main/resources/application.properties ]; then \
    iconv -f ISO-8859-1 -t UTF-8 src/main/resources/application.properties -o src/main/resources/application.properties.utf8 && \
    mv src/main/resources/application.properties.utf8 src/main/resources/application.properties || true; \
    fi

# Ajouter l'option d'encodage pour Maven
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# Étape d'exécution
FROM docker.io/eclipse-temurin:17-jre-jammy AS runtime
WORKDIR /app

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Create a non-root user for Openshift
RUN useradd -u 1001 -r -g 0 -d /app -s /sbin/nologin -c "App user" appuser && \
    chmod -R g+rwX /app

USER 1001

# Copier le JAR depuis l'étape de build
COPY --from=builder --chown=1001:0 /build/target/omni365-saas-api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]