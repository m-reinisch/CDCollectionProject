FROM eclipse-temurin:25
LABEL authors="m-reinisch"
EXPOSE 8080
COPY backend/target/cdcollapp.jar cdcollapp.jar
#ARG USER_ID
#ENV MY_USER_ID=$USER_ID
ENTRYPOINT ["java", "-jar", "cdcollapp.jar"]
