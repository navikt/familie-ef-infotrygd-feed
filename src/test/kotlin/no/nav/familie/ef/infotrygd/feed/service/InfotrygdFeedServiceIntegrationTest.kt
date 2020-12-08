package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.DbContainerInitializer
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.database.HendelseType
import no.nav.familie.kontrakter.ef.infotrygd.*
import org.assertj.core.api.Assertions.assertThat
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
        infotrygdFeedService.opprettNyFeed(OpprettVedtakHendelseDto(type = StønadType.OVERGANGSSTØNAD,
                                                                    fnr = FNR,
                                                                    startdato = LocalDate.now()))
        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0)

        assertThat(feeds.find {
            it.type == HendelseType.VEDTAK
            && it.stønad == StønadType.OVERGANGSSTØNAD
            && it.fnr == FNR
        }).isNotNull
    }

    @Test
    fun `StartBehandling - Hent feeds fra database`() {
        infotrygdFeedService.opprettNyFeed(
                OpprettStartBehandlingHendelseDto(type = StønadType.BARNETILSYN,
                                                  fnr = FNR))
        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0)

        assertThat(feeds.find {
            it.type == HendelseType.START_BEHANDLING
            && it.stønad == StønadType.BARNETILSYN
            && it.fnr == FNR
        }).isNotNull
    }

    @Test
    fun `Verifiser at maks definert antall feeds blir returnert`() {
        val fnrStonadsmottaker = "10000000000"
        for (i in 1..3) infotrygdFeedService.opprettNyFeed(
                OpprettVedtakHendelseDto(type = StønadType.OVERGANGSSTØNAD,
                                         fnr = fnrStonadsmottaker + i,
                                         startdato = LocalDate.now()))

        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0, 2)

        assertThat(feeds).hasSize(2)
    }

    @Test
    fun `Hent feed-meldinger med høy sistLestSekvensId gir tom liste`() {
        assertThat(infotrygdFeedService.hentMeldingerFraFeed(1000)).isEmpty()
    }

    @Test
    internal fun `Opprett periode opprettet en annulert periodehenselde først`() {
        val perioder = listOf(Periode(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1)))
        infotrygdFeedService.opprettNyFeed(OpprettPeriodeHendelseDto(FNR, StønadType.OVERGANGSSTØNAD, perioder = perioder))
        val feed = infotrygdFeedService.hentMeldingerFraFeed(0, 5)
        assertThat(feed).hasSize(2)
        assertThat(feed[0].type).isEqualTo(HendelseType.PERIODE_ANNULERT)
        assertThat(feed[1].type).isEqualTo(HendelseType.PERIODE)
        assertThat(feed[1].startdato).isNotNull
        assertThat(feed[1].sluttdato).isNotNull
    }

    @Test
    internal fun `Opprett periode uten perioder oppretter kun en annulert periode`() {
        infotrygdFeedService.opprettNyFeed(OpprettPeriodeHendelseDto(FNR, StønadType.OVERGANGSSTØNAD, perioder = emptyList()))
        val feed = infotrygdFeedService.hentMeldingerFraFeed(0, 5)
        assertThat(feed).hasSize(1)
        assertThat(feed[0].type).isEqualTo(HendelseType.PERIODE_ANNULERT)
    }

    companion object {

        const val FNR = "12345678911"
    }
}