DROP TABLE FEED;
CREATE TABLE FEED
(
    sekvens_id     SERIAL       PRIMARY KEY,
    saksnummer     INTEGER      NOT NULL,
    type           VARCHAR      NOT NULL,
    stonad         VARCHAR      NOT NULL,
    fnr            VARCHAR      NOT NULL,
    startdato      DATE,
    opprettet_dato TIMESTAMP(3) NOT NULL
);
