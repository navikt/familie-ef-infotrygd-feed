CREATE TABLE FEED
(
    sekvens_id     SERIAL PRIMARY KEY,
    type           VARCHAR      NOT NULL,
    fnr            VARCHAR      NOT NULL,
    startdato      TIMESTAMP(3) NOT NULL,
    opprettet_dato TIMESTAMP(3) NOT NULL
);
