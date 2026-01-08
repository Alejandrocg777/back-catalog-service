# Etapa 1: Construcción
FROM maven:3.8-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM amazoncorretto:17-al2-jdk

WORKDIR /apl/
COPY --from=build /app/target/*.jar micro.jar

RUN mkdir -p /apl/files/
RUN mkdir -p /apl/tmp/
RUN rm /etc/localtime
RUN ln -s /usr/share/zoneinfo/America/Bogota /etc/localtime
RUN echo 'alias ll="ls -lha"' >> ~/.bashrc

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "micro.jar"]