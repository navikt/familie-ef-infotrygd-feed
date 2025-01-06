FROM ghcr.io/navikt/baseimages/temurin:21
COPY ./target/familie-ef-infotrygd-feed.jar "app.jar"
