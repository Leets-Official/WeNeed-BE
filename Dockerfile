FROM openjdk:17-jdk

COPY build/libs/*.jar weneed.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/weneed.jar"]