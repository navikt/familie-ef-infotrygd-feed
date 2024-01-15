FROM navikt/java:21
ENV APP_NAME=familie-ef-infotrygd-feed
COPY ./target/familie-ef-infotrygd-feed.jar "app.jar"
