DROP TABLE FEED;
CREATE TABLE FEED
(
    sekvens_id     SERIAL       PRIMARY KEY,
    type           VARCHAR      NOT NULL,
    stonad         VARCHAR      NOT NULL,
    fnr            VARCHAR      NOT NULL,
    startdato      DATE,
    sluttdato      DATE,
    opprettet_dato TIMESTAMP(3) NOT NULL
);
