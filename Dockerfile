FROM navikt/java:11
ENV APP_NAME=familie-ef-infotrygd-feed
COPY ./target/familie-ef-infotrygd-feed.jar "app.jar"
