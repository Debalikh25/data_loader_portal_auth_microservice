FROM openjdk:8

EXPOSE 7001

COPY target/dlt-auth.jar dlt-auth.jar


ENTRYPOINT ["java" , "-jar" , "dlt-auth.jar"]