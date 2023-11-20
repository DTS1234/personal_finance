FROM arm64v8/openjdk:17-jdk
VOLUME /tmp
COPY target/personal_finance-1.0-SNAPSHOT-spring-boot.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
