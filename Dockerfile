FROM openjdk:16
ADD target/CustomerProductAuthenticationAPI-1.0.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
