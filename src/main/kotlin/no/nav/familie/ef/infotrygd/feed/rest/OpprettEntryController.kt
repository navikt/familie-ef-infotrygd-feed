package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.familie.kontrakter.ef.felles.StønadType
import no.nav.familie.kontrakter.ef.infotrygd.OpprettPeriodeHendelseDto
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    "/api/entry",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.TEXT_PLAIN_VALUE],
)
@ProtectedWithClaims(issuer = "azuread", claimMap = ["roles=access_as_application"])
class OpprettEntryController(
    private val infotrygdFeedService: InfotrygdFeedService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @PostMapping("/periode")
    fun lagNyPeriodeMelding(
        @RequestBody opprettEntryDto: OpprettPeriodeHendelseDto,
    ): ResponseEntity<String> {
        logger.info("Mottatt request for å opprette ny periode melding i feed for stønadstype ${opprettEntryDto.type}, med ${opprettEntryDto.perioder.size} perioder.")
        if (opprettEntryDto.type != StønadType.OVERGANGSSTØNAD) {
            return ResponseEntity
                .badRequest()
                .body("Har ikke satt opp mappinger for andre typer enn for overgangsstønad")
        }
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        return ResponseEntity.ok("OK")
    }
}
