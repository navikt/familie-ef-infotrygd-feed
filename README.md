# familie-ef-infotrygd-feed
Feed-kommunikasjon mellom Enslig forsørger og Infotrygd

# Kjøring lokalt
`DevLauncher` kjører appen lokalt med Spring-profilen `dev` satt. Appen tilgjengeliggjøres da på `localhost:8092`.  

## Swagger
http://localhost:8092/swagger-ui/index.html

## Database

Dersom man vil kjøre med postgres, kan man bytte til Spring-profilen `postgres`. Dette kan feks gjøres ved å sette
 `-Dspring.profiles.active=postgres` under Edit Configurations -> VM Options.
Da må man sette opp postgres-databasen, dette gjøres slik:
```
docker run --name familie-ef-infotrygd-feed -e POSTGRES_PASSWORD=test -d -p 5432:5432 postgres
docker ps (finn container id)
docker exec -it <container_id> bash
psql -U postgres
CREATE DATABASE "familie-ef-infotrygd-feed";
```

### Databasenavn og versjon on-prem
Navnet på database i preprod er `familie-ef-infotrygd-feed` og er på versjon `16.4.0`. 
I prod er navnet på databasen `familie-ef-infotrygd-feed-15` og er på versjon `15.7.0`.

Databasestatistikk for on-prem databaser finnes her: https://grafana.nav.cloud.nais.io/goto/apT2w8VHg?orgId=1

## Kontaktinformasjon
For NAV-interne kan henvendelser om applikasjonen rettes til #team-familie på slack. Ellers kan man opprette et issue her på github.
