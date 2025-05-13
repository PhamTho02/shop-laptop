#build
FROM maven:3.8.6-openjdk-18 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


#RUN
FROM openjdk:17-jdk-slim
WORKDIR /run
COPY --from=build /app/target/*.war /run/app.war
EXPOSE 8080
ENTRYPOINT java -jar /run/app.war