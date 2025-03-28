package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.familie.ef.infotrygd.feed.database.DbContainerInitializer
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.familie.kontrakter.ef.felles.StønadType
import no.nav.familie.kontrakter.ef.infotrygd.OpprettPeriodeHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.Periode
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [DbContainerInitializer::class])
@ActiveProfiles("postgres")
@Tag("integration")
@EnableMockOAuth2Server
internal class InfotrygdFeedControllerTest {
    @Autowired private lateinit var infotrygdFeedController: InfotrygdFeedController

    @Autowired private lateinit var infotrygdFeedService: InfotrygdFeedService

    @Autowired lateinit var feedRepository: FeedRepository

    @Autowired lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    internal fun setUp() {
        jdbcTemplate.execute("ALTER SEQUENCE feed_sekvens_id_seq RESTART")
    }

    @AfterEach
    internal fun tearDown() {
        feedRepository.deleteAll()
    }

    @Test
    internal fun `Hent feed`() {
        opprettVedtak("12345678901", StønadType.OVERGANGSSTØNAD)
        opprettVedtak("12345678901", StønadType.SKOLEPENGER)
        opprettStartBehandling("12345678901", StønadType.OVERGANGSSTØNAD)
        opprettPeriode("12345678901", StønadType.OVERGANGSSTØNAD)

        listOf(0, 2, 10).forEach {
            assertThat(hentFeed(it)).isEqualTo(readExpected(it))
        }
    }

    private fun hentFeed(sekvensnummer: Int): String? {
        val feed = infotrygdFeedController.feed(sekvensnummer.toLong())
        val nyFeed =
            feed.copy(
                elementer =
                    feed.elementer.map {
                        it.copy(metadata = it.metadata.copy(opprettetDato = LocalDate.of(2020, 1, 1).atStartOfDay()))
                    },
            )
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nyFeed)
    }

    private fun readExpected(id: Int): String =
        this::class.java.classLoader
            .getResource("feed/feed_fra_$id.json")
            .readText()

    private fun opprettVedtak(
        fnr: String,
        type: StønadType,
    ) {
        infotrygdFeedService.opprettNyFeed(OpprettVedtakHendelseDto(setOf(fnr), type, LocalDate.of(2020, 1, 1)))
    }

    private fun opprettStartBehandling(
        fnr: String,
        type: StønadType,
    ) {
        infotrygdFeedService.opprettNyFeed(OpprettStartBehandlingHendelseDto(setOf(fnr), type))
    }

    private fun opprettPeriode(
        fnr: String,
        type: StønadType,
    ) {
        val perioder =
            listOf(
                Periode(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1), true),
                Periode(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 1), false),
            )
        infotrygdFeedService.opprettNyFeed(OpprettPeriodeHendelseDto(setOf(fnr), type, perioder))
    }
}
