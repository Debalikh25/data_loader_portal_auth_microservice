FROM openjdk:17

EXPOSE 7000

ADD target/dlt-auth.jar dlt-auth.jar

ENTRYPOINT ["java" , "-jar" , "dlt-auth.jar"]