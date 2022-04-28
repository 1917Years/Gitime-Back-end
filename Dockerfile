FROM openjdk:11-jdk

RUN ./gradlew build

WORKDIR /server

COPY ./build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
