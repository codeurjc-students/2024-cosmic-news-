# BUILD STAGE

# FRONTEND 

FROM  node:lts-alpine3.19 as buildFront
WORKDIR /frontend
COPY frontend/cosmic-news/package*.json /frontend/cosmic-news/angular.json /frontend/cosmic-news/tsconfig*.json  /frontend/
RUN npm install --force
COPY /frontend/cosmic-news/src /frontend/src
RUN npm run build -- --configuration production --base-href="/new/"

# BACKEND

FROM maven:eclipse-temurin AS build 
WORKDIR /project
COPY cosmic-news/pom.xml /project/
COPY cosmic-news/src/ /project/src
COPY --from=buildFront /frontend/dist/cosmic-news/browser/ ./src/main/resources/static/new
RUN mvn -f /project/pom.xml clean package -DskipTests

# PACKAGE STAGE
FROM openjdk:23-slim
WORKDIR /project
COPY --from=build /project/target/*.jar /project/Application.jar
EXPOSE 8443
CMD ["java", "-jar", "/project/Application.jar"]