package no.nav.familie.ef.infotrygd.feed.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import no.nav.familie.ef.infotrygd.feed.rest.dto.FeedMeldingDto
import no.nav.familie.ef.infotrygd.feed.rest.dto.konverterTilFeedMeldingDto
import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/feed")
@ProtectedWithClaims(issuer = "sts")
class InfotrygdFeedController(private val infotrygdFeedService: InfotrygdFeedService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Operation(
            summary = "Hent liste med hendelser.",
            description = "Henter hendelser med sekvensId større enn sistLesteSekvensId."
    )
    @GetMapping("/v1/feed", produces = ["application/json; charset=us-ascii"])
    fun feed(
            @Parameter(description = "Sist leste sekvensnummer.", required = true, example = "0")
            @RequestParam("sistLesteSekvensId") sekvensnummer: Long
    ): FeedMeldingDto {
        val konverterTilFeedMeldingDto =
                konverterTilFeedMeldingDto(infotrygdFeedService.hentMeldingerFraFeed(sistLestSekvensId = sekvensnummer))
        logger.info("Hentet ${konverterTilFeedMeldingDto.elementer.size} feeds fra sekvensnummer $sekvensnummer")
        return konverterTilFeedMeldingDto
    }

}