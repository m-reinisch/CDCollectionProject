FROM eclipse-temurin:25
LABEL authors="m-reinisch"

EXPOSE 8080

COPY backend/target/cdcollapp.jar cdcollapp.jar

ENTRYPOINT ["java", "-jar", "cdcollapp.jar"]
