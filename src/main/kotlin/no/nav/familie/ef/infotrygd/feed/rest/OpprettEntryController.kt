package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.familie.ef.infotrygd.feed.rest.dto.OpprettEntryDto
import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/entry")
@ProtectedWithClaims(issuer = "azuread")
class OpprettEntryController(private val infotrygdFeedService: InfotrygdFeedService) {

    @PostMapping("/",
                 consumes = [MediaType.APPLICATION_JSON_VALUE],
                 produces = [MediaType.APPLICATION_JSON_VALUE])
    fun lagNyVedtaksMelding(@RequestBody opprettEntryDto: OpprettEntryDto) {
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
    }

}
