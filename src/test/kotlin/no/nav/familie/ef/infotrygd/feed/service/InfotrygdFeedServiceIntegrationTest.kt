package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.DbContainerInitializer
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.database.HendelseType
import no.nav.familie.ef.infotrygd.feed.rest.dto.InfotrygdHendelseType
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.StønadType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [DbContainerInitializer::class])
@ActiveProfiles("postgres")
@Tag("integration")
class InfotrygdFeedServiceIntegrationTest {

    @Autowired lateinit var infotrygdFeedService: InfotrygdFeedService

    @Autowired lateinit var feedRepository: FeedRepository

    @BeforeEach
    internal fun setUp() {
        feedRepository.deleteAll()
    }

    @Test
    fun `Vedtak - Hent feeds fra database`() {
        val fnr = "12345678911"
        infotrygdFeedService.opprettNyFeed(OpprettVedtakHendelseDto(type = StønadType.OVERGANGSSTØNAD,
                                                                    saksnummer = 1,
                                                                    fnr = fnr,
                                                                    startdato = LocalDate.now()))
        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0)

        assertNotNull(feeds.find {
            it.type == HendelseType.VEDTAK
            && it.stønad == StønadType.OVERGANGSSTØNAD
            && it.fnr == fnr
        })
    }

    @Test
    fun `StartBehandling - Hent feeds fra database`() {
        val fnr = "12345678911"
        infotrygdFeedService.opprettNyFeed(
                OpprettStartBehandlingHendelseDto(type = StønadType.BARNETILSYN,
                                                  saksnummer = 1,
                                                  fnr = fnr))
        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0)

        assertNotNull(feeds.find {
            it.type == HendelseType.START_BEHANDLING
            && it.stønad == StønadType.BARNETILSYN
            && it.fnr == fnr
        })
    }

    @Test
    fun `Verifiser at maks definert antall feeds blir returnert`() {
        val fnrStonadsmottaker = "10000000000"
        for (i in 1..3) infotrygdFeedService.opprettNyFeed(
                OpprettVedtakHendelseDto(type = StønadType.OVERGANGSSTØNAD,
                                         saksnummer = i,
                                         fnr = fnrStonadsmottaker + i,
                                         startdato = LocalDate.now()))

        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0, 2)

        Assertions.assertEquals(2, feeds.size)
    }

    @Test
    fun `Hent feed-meldinger med høy sistLestSekvensId gir tom liste`() {
        val feedListe = infotrygdFeedService.hentMeldingerFraFeed(1000)

        Assertions.assertTrue(feedListe.isEmpty())
    }
}