FROM maven:3.6.1-jdk-8-alpine AS MAVEN_TOOL_CHAIN

# Add Maintainer Info
LABEL maintainer="c.nadjim@gmail.com"

WORKDIR /usr/src/app

COPY ./pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean install

FROM java:openjdk-8-jdk-alpine

COPY --from=MAVEN_TOOL_CHAIN /usr/src/app/target/*.jar /app.jar

RUN sh -c 'touch /app.jar'

VOLUME /tmp

CMD ["java", "-jar", "/app.jar", "--spring.profiles.active=${profiles}"]

EXPOSE 8080
