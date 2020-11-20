package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.familie.ef.infotrygd.feed.rest.dto.erVedtak
import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
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
@RequestMapping("/api/preprod")
@ProtectedWithClaims(issuer = "sts")
class PreprodOpprettEntryController(private val infotrygdFeedService: InfotrygdFeedService) {

    @PostMapping("/vedtak",
                 consumes = [MediaType.APPLICATION_JSON_VALUE],
                 produces = [MediaType.APPLICATION_JSON_VALUE])
    fun lagNyVedtaksMelding(@RequestBody opprettEntryDto: OpprettVedtakHendelseDto): ResponseEntity<Any> {
        if (!opprettEntryDto.type.erVedtak()) {
            return ResponseEntity.badRequest().build()
        }
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/start-behandling",
                 consumes = [MediaType.APPLICATION_JSON_VALUE],
                 produces = [MediaType.APPLICATION_JSON_VALUE])
    fun lagNyStartBehandlingMelding(@RequestBody opprettEntryDto: OpprettStartBehandlingHendelseDto): ResponseEntity<Any> {
        if (opprettEntryDto.type.erVedtak()) {
            return ResponseEntity.badRequest().build()
        }
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        return ResponseEntity.ok().build()
    }

}
