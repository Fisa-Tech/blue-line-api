# Étape 1 : Builder Maven
FROM maven:3.9.9 AS build

WORKDIR /build

# Copier le fichier POM et télécharger les dépendances en mode hors-ligne (cache Maven)
COPY pom.xml /build/pom.xml
RUN mvn dependency:go-offline

# Copier les sources et construire le projet
COPY src /build/src
RUN mvn clean package -DskipTests

# Étape 2 : Image finale avec OpenJDK
FROM openjdk:23 AS runtime

# Définir un répertoire pour l'application
WORKDIR /app

# Copier le fichier JAR depuis l'étape de build
COPY --from=build /build/target/*.jar /app/app.jar

# Créer un utilisateur non-root
RUN useradd -r -m -d /app appuser
USER appuser

# Commande par défaut
ENTRYPOINT ["java", "-jar", "app.jar"]
