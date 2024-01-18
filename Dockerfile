FROM openjdk:17-jdk

COPY build/libs/*.jar weneed.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dspring.profiles.active=dev -jar /weneed.jar"]