FROM openjdk:11-jdk
#// FROM amazoncorretto:11 ==> amazon corretto 11 사용할 경우
WORKDIR /server
COPY ./build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
