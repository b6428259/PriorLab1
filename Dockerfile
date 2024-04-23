# Build stage
FROM maven:3.8.3-openjdk-17 AS build

RUN mkdir /home/app
WORKDIR /home/app

COPY ./pom.xml ./pom.xml
RUN mvn dependency:resolve

COPY ./src ./src
RUN mvn clean package

# Final stage
FROM openjdk:17
RUN mkdir /home/app
WORKDIR /home/app

COPY --from=build /home/app/target/adventureshops-0.0.1-SNAPSHOT.jar /home/app/app.jar

ENTRYPOINT exec java -jar /home/app/app.jar
