package no.nav.familie.ef.infotrygd.feed.rest.dto

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import no.nav.familie.ef.infotrygd.feed.rest.dto.InfotrygdHendelseType.*
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalDateTime

class SchemaValidatorTest {

    @Test
    fun `Dto for startBehandling validerer mot schema`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForStartBehandling())
        val feilListe = schema.validate(node)
        assertThat(feilListe).isEmpty()
    }

    @Test
    fun `Dto for vedtak validerer mot schema`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForVedtak())
        val feilListe = schema.validate(node)
        assertThat(feilListe).isEmpty()
    }

    @Test
    fun `Dto for startBehandling validerer ikke dersom fnrBarn har feil format`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForStartBehandling("123456"))
        val feilListe = schema.validate(node)
        assertThat(feilListe).isNotEmpty
    }

    @Test
    fun `Dto for vedtak validerer ikke dersom fnrStoenadsmottaker har feil format`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForVedtak("123456"))
        val feilListe = schema.validate(node)
        assertThat(feilListe).isNotEmpty
    }

    @Test
    fun `Dto for periode validerer ikke dersom fnrStoenadsmottaker har feil format`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForPeriodeBehandling("123456"))
        val feilListe = schema.validate(node)
        assertThat(feilListe).isNotEmpty
    }

    @Test
    fun `Skal feile feil InfotrygdHendelsetype - Vedtak`() {
        val filter = setOf(EF_Vedtak_OvergStoenad, EF_Vedtak_Barnetilsyn, EF_Vedtak_Skolepenger)
        for(type in InfotrygdHendelseType.values().filterNot { filter.contains(it) }) {
            val node = objectMapper.valueToTree<JsonNode>(testDtoForVedtak(type = type))
            val feilListe = schema.validate(node)
            assertThat(feilListe).isNotEmpty
        }
    }

    @Test
    fun `Skal feile feil InfotrygdHendelsetype - StartBehandling`() {
        val filter = setOf(EF_StartBeh_OvergStoenad, EF_StartBeh_Barnetilsyn, EF_StartBeh_Skolepenger)
        for(type in InfotrygdHendelseType.values().filterNot { filter.contains(it) }) {
            val node = objectMapper.valueToTree<JsonNode>(testDtoForStartBehandling(type = type))
            val feilListe = schema.validate(node)
            assertThat(feilListe).isNotEmpty
        }
    }

    @Test
    fun `SchemaValidering vedtak`() {
        val type = "EF_Vedtak_Skolepenger"
        val innhold = mapOf(
                "fnr" to "12312312311",
                "startdatoVedtakEF" to "2010-01-01"
        )
        validerSkjema(type, innhold)
    }

    @Test
    fun `SchemaValidering start behandling`() {
        val type = "EF_StartBeh_Skolepenger"
        val innhold = mapOf("fnr" to "12312312311")
        validerSkjema(type, innhold)
    }

    private fun validerSkjema(type: String, innhold: Map<String, String>) {
        val node = objectMapper.valueToTree<JsonNode>(mapOf(
                "tittel" to "tittel",
                "inneholderFlereElementer" to true,
                "elementer" to listOf(mapOf(
                        "sekvensId" to 1,
                        "type" to type,
                        "saksnummer" to 1,
                        "metadata" to mapOf("opprettetDato" to "2018-04-18T09:03:29.202"),
                        "innhold" to innhold
                ))
        ))
        val feilListe = schema.validate(node)
        assertThat(feilListe).isEmpty()
    }

    private fun testDtoForVedtak(fnr: String = "12345678910",
                                 type: InfotrygdHendelseType = InfotrygdHendelseType.EF_Vedtak_OvergStoenad): FeedDto {
        return FeedDto(
                tittel = "Feed schema validator test",
                inneholderFlereElementer = false,
                elementer = listOf(
                        FeedElement(
                                sekvensId = 42,
                                type = type,
                                saksnummer = 1,
                                metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                                innhold = VedtakInnhold(fnr = fnr, startdatoVedtakEF = LocalDate.now())
                        ))
        )
    }

    private fun testDtoForStartBehandling(fnr: String = "12345678910",
                                          type: InfotrygdHendelseType = EF_StartBeh_OvergStoenad): FeedDto {
        return FeedDto(
                tittel = "Feed schema validator test",
                inneholderFlereElementer = false,
                elementer = listOf(
                        FeedElement(
                                sekvensId = 42,
                                type = type,
                                saksnummer = 1,
                                metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                                innhold = StartBehandlingInnhold(fnr = fnr)
                        ))
        )
    }

    private fun testDtoForPeriodeBehandling(fnr: String = "12345678910",
                                            type: InfotrygdHendelseType = EF_Periode_OvergStoenad): FeedDto {
        return FeedDto(
                tittel = "Feed schema validator test",
                inneholderFlereElementer = false,
                elementer = listOf(
                        FeedElement(
                                sekvensId = 42,
                                type = type,
                                saksnummer = 1,
                                metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                                innhold = PeriodeInnhold(fnr = fnr, periodestart = LocalDate.now(), periodeslutt = LocalDate.now())
                        ))
        )
    }

    private val schema: JsonSchema
        get() {
            val schemaNode = objectMapper.readTree(hentFeedSchema())
            return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4).getSchema(schemaNode)
        }

    private fun hentFeedSchema(): String {
        val inputStream = this::class.java.classLoader.getResourceAsStream("schema/ensligforsorger-feed-schema.json")
        return String(inputStream!!.readAllBytes(), Charset.forName("UTF-8"))
    }
}