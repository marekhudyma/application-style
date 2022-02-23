FROM openjdk:17-alpine

## Add the wait script to the image https://github.com/ufoscout/docker-compose-wait
# ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.5.0/wait /wait
# RUN chmod +x /wait

COPY service/target/application-style-exec.jar application-style.jar

EXPOSE 8080

ENTRYPOINT java ${ADDITIONAL_JAVA_OPTIONS} -jar application-style.jar