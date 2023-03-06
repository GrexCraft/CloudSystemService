FROM openjdk:17-alpine

MAINTAINER GrexCraft

COPY src/main/docker/start-server.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/start-server.sh && mkdir -p /data

EXPOSE 8080

#VOLUME ["/data/configuration"]

ENV DEBUG false

COPY target/CloudService.jar /data/Service.jar

WORKDIR /data

CMD ["start-server.sh"]
