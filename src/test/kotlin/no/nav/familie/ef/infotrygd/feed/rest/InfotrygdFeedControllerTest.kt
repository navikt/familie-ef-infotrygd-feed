package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.familie.ef.infotrygd.feed.database.DbContainerInitializer
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.rest.dto.InfotrygdHendelseType
import no.nav.familie.ef.infotrygd.feed.service.InfotrygdFeedService
import no.nav.familie.kontrakter.ef.infotrygd.OpprettPeriodeHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.StønadType
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [DbContainerInitializer::class])
@ActiveProfiles("postgres")
@Tag("integration")
internal class InfotrygdFeedControllerTest {

    @Autowired private lateinit var infotrygdFeedController: InfotrygdFeedController
    @Autowired private lateinit var infotrygdFeedService: InfotrygdFeedService

    @Autowired lateinit var feedRepository: FeedRepository
    @Autowired lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    internal fun setUp() {
        feedRepository.deleteAll()
        jdbcTemplate.execute("ALTER SEQUENCE feed_sekvens_id_seq RESTART")
    }

    @Test
    internal fun `Hent feed`() {
        opprettVedtak("12345678901", StønadType.OVERGANGSSTØNAD)
        opprettVedtak("12345678901", StønadType.SKOLEPENGER)
        opprettStartBehandling("12345678901", StønadType.OVERGANGSSTØNAD)
        opprettPeriodeBehandling("12345678901", StønadType.OVERGANGSSTØNAD)

        assertThat(hentFeed(0))
                .isEqualTo(readExpected(0))
        assertThat(hentFeed(2))
                .isEqualTo(readExpected(2))
        assertThat(hentFeed(10))
                .isEqualTo(readExpected(10))
    }

    private fun hentFeed(sekvensnummer: Long): String? {
        val feed = infotrygdFeedController.feed(sekvensnummer)
        val nyFeed = feed.copy(elementer = feed.elementer.map {
            it.copy(metadata = it.metadata.copy(opprettetDato = LocalDate.of(2020, 1, 1).atStartOfDay()))
        })
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nyFeed)
    }

    private fun readExpected(id: Int): String {
        return this::class.java.classLoader.getResource("feed/feed_fra_$id.json").readText()
    }

    private fun opprettVedtak(fnr: String, type: StønadType) {
        infotrygdFeedService.opprettNyFeed(OpprettVedtakHendelseDto(fnr, 1, type, LocalDate.of(2020, 1, 1)))
    }

    private fun opprettStartBehandling(fnr: String, type: StønadType) {
        infotrygdFeedService.opprettNyFeed(OpprettStartBehandlingHendelseDto(fnr, 1, type))
    }

    private fun opprettPeriodeBehandling(fnr: String, type: StønadType) {
        infotrygdFeedService.opprettNyFeed(OpprettPeriodeHendelseDto(fnr, 1, type, LocalDate.now(), LocalDate.now()))
    }
}