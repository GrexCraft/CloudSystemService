FROM openjdk:17-alpine

MAINTAINER GrexCraft

COPY start-server.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/start-server.sh
RUN mkdir -p /data

EXPOSE 8080

#VOLUME ["/data/configuration"]

ENV DEBUG false

WORKDIR /data

COPY target/CloudService.jar /data/Service.jar

CMD ["start-server.sh"]
