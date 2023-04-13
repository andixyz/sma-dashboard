FROM openjdk:17.0.2-slim-bullseye

# Set the timezone.
RUN echo "Europe/Berlin" > /etc/timezone

COPY ./adapter/sma-webui-adapter/target/sma-webui-adapter.jar /

# run filebeat and application
CMD java -Djava.security.egd=file:/dev/./urandom \
         -Xmx256m \
         -jar sma-webui-adapter.jar