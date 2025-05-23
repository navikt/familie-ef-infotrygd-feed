package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.familie.kontrakter.ef.felles.StønadType
import no.nav.familie.kontrakter.ef.infotrygd.OpprettPeriodeHendelseDto
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("preprod")
@RequestMapping(
    "/api/preprod",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@ProtectedWithClaims(issuer = "sts")
class PreprodOpprettEntryController(
    private val infotrygdFeedService: InfotrygdFeedService,
) {
    @PostMapping("/periode")
    fun lagNyPeriodeMelding(
        @RequestBody opprettEntryDto: OpprettPeriodeHendelseDto,
    ): ResponseEntity<Any> {
        if (opprettEntryDto.type != StønadType.OVERGANGSSTØNAD) {
            return ResponseEntity.badRequest().body("Har ikke satt opp mappinger for andre typer enn for overgangsstønad")
        }
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        return ResponseEntity.ok().build()
    }
}
