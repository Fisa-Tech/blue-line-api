# Étape 1 : Construction avec Maven et Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /build

# Copier le fichier POM et télécharger les dépendances
COPY pom.xml /build/pom.xml
RUN mvn dependency:go-offline

# Copier le code source et construire le projet
COPY src /build/src
RUN mvn clean package -DskipTests

# Étape 2 : Image finale avec OpenJDK 21
FROM eclipse-temurin:21-jdk AS runtime

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR depuis l'étape de construction
COPY --from=build /build/target/*.jar /app/app.jar

# Créer un utilisateur non-root pour des raisons de sécurité
RUN useradd -r -m -d /app appuser
USER appuser

# Définir la commande par défaut
ENTRYPOINT ["java", "-jar", "app.jar"]
