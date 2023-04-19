FROM openjdk:8-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/market.jar /app/
WORKDIR /app/
ENTRYPOINT ["java", "-jar", "market.jar"]