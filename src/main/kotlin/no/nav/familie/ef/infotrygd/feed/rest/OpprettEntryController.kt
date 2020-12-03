package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/entry",
                consumes = [MediaType.APPLICATION_JSON_VALUE],
                produces = [MediaType.APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = "azuread")
class OpprettEntryController(private val infotrygdFeedService: InfotrygdFeedService) {

    @PostMapping("/vedtak")
    fun lagNyVedtaksMelding(@RequestBody opprettEntryDto: OpprettVedtakHendelseDto): ResponseEntity<Any> {
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/start-behandling")
    fun lagNyStartBehandlingMelding(@RequestBody opprettEntryDto: OpprettStartBehandlingHendelseDto): ResponseEntity<Any> {
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        return ResponseEntity.ok().build()
    }

}
