FROM openjdk:11
VOLUME /tmp
ADD ./target/music_player*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","/app.jar"]
EXPOSE 8080