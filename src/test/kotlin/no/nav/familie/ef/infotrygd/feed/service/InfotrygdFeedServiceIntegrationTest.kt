package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.DbContainerInitializer
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.database.HendelseType
import no.nav.familie.kontrakter.ef.infotrygd.*
import no.nav.familie.kontrakter.ef.felles.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
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

    @AfterEach
    internal fun tearDown() {
        feedRepository.deleteAll()
    }

    @Test
    fun `Vedtak - Hent feeds fra database`() {
        val personIdenter = setOf(FNR, FNR2)
        infotrygdFeedService.opprettNyFeed(
            OpprettVedtakHendelseDto(
                type = StønadType.OVERGANGSSTØNAD,
                personIdenter = personIdenter,
                startdato = LocalDate.now()
            )
        )
        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0)

        assertThat(feeds)
            .withFailMessage("Skal opprette en entry per FNR")
            .hasSize(2)

        personIdenter.forEachIndexed { index, personIdent ->
            val feed = feeds[index]
            assertThat(feed.type).isEqualTo(HendelseType.VEDTAK)
            assertThat(feed.stønad).isEqualTo(StønadType.OVERGANGSSTØNAD)
            assertThat(feed.personIdent).isEqualTo(personIdent)
        }
    }

    @Test
    fun `StartBehandling - Hent feeds fra database`() {
        val personIdenter = setOf(FNR, FNR2)
        infotrygdFeedService.opprettNyFeed(
            OpprettStartBehandlingHendelseDto(
                type = StønadType.BARNETILSYN,
                personIdenter = personIdenter
            )
        )
        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0)

        personIdenter.forEachIndexed { index, personIdent ->
            val feed = feeds[index]
            assertThat(feed.type).isEqualTo(HendelseType.START_BEHANDLING)
            assertThat(feed.stønad).isEqualTo(StønadType.BARNETILSYN)
            assertThat(feed.personIdent).isEqualTo(personIdent)
        }
    }

    @Test
    fun `Verifiser at maks definert antall feeds blir returnert`() {
        val fnrStonadsmottaker = "10000000000"
        for (i in 1..3) infotrygdFeedService.opprettNyFeed(
            OpprettVedtakHendelseDto(
                type = StønadType.OVERGANGSSTØNAD,
                personIdenter = setOf(fnrStonadsmottaker + i),
                startdato = LocalDate.now()
            )
        )

        val feeds = infotrygdFeedService.hentMeldingerFraFeed(0, 2)

        assertThat(feeds).hasSize(2)
    }

    @Test
    fun `Hent feed-meldinger med høy sistLestSekvensId gir tom liste`() {
        assertThat(infotrygdFeedService.hentMeldingerFraFeed(1000)).isEmpty()
    }

    @Test
    internal fun `Opprett periode opprettet en annulert periodehenselde først`() {
        val personIdenter = setOf(FNR, FNR2)
        val perioder = listOf(Periode(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1), true))
        val opprettEntryDto = OpprettPeriodeHendelseDto(personIdenter, StønadType.OVERGANGSSTØNAD, perioder = perioder)
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        val feed = infotrygdFeedService.hentMeldingerFraFeed(0, 5)

        assertThat(feed).hasSize(4)
        personIdenter.forEachIndexed { index, personIdent ->
            val i = personIdenter.size * index
            assertThat(feed[i].type).isEqualTo(HendelseType.PERIODE_ANNULERT)
            assertThat(feed[i].startdato).isNull()
            assertThat(feed[i].sluttdato).isNull()

            assertThat(feed[i + 1].type).isEqualTo(HendelseType.PERIODE)
            assertThat(feed[i + 1].startdato).isNotNull
            assertThat(feed[i + 1].sluttdato).isNotNull
        }
    }

    @Test
    internal fun `Opprett periode uten perioder oppretter kun en annulert periode`() {
        val opprettEntryDto = OpprettPeriodeHendelseDto(setOf(FNR), StønadType.OVERGANGSSTØNAD, perioder = emptyList())
        infotrygdFeedService.opprettNyFeed(opprettEntryDto)
        val feed = infotrygdFeedService.hentMeldingerFraFeed(0, 5)
        assertThat(feed).hasSize(1)
        assertThat(feed[0].type).isEqualTo(HendelseType.PERIODE_ANNULERT)
    }



    companion object {

        const val FNR = "12345678911"
        const val FNR2 = "12345678912"
    }
}