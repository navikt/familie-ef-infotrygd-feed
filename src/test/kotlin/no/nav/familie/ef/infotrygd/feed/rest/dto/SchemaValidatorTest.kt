package no.nav.familie.ef.infotrygd.feed.rest.dto

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
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
    fun `Dto for periode validerer mot schema`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForPeriode())
        val feilListe = schema.validate(node)
        assertThat(feilListe).isEmpty()
    }

    @Test
    fun `Dto for annulert periode validerer mot schema`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForPeriodeAnnulert())
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
        val node = objectMapper.valueToTree<JsonNode>(testDtoForPeriode("123456"))
        val feilListe = schema.validate(node)
        assertThat(feilListe).isNotEmpty
    }

    @Test
    fun `Dto for annulert periode validerer ikke dersom fnrStoenadsmottaker har feil format`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForPeriodeAnnulert("123456"))
        val feilListe = schema.validate(node)
        assertThat(feilListe).isNotEmpty
    }

    @Test
    fun `Skal feile feil InfotrygdHendelsetype - Vedtak`() {
        val filter = setOf(EF_Vedtak_OvergStoenad, EF_Vedtak_Barnetilsyn, EF_Vedtak_Skolepenger)
        for (type in InfotrygdHendelseType.values().filterNot { filter.contains(it) }) {
            val node = objectMapper.valueToTree<JsonNode>(testDtoForVedtak(type = type))
            val feilListe = schema.validate(node)
            assertThat(feilListe)
                    .withFailMessage("Feiler ikke $type")
                    .isNotEmpty
        }
    }

    @Test
    fun `Skal feile feil InfotrygdHendelsetype - StartBehandling`() {
        val filter = setOf(EF_StartBeh_OvergStoenad, EF_StartBeh_Barnetilsyn, EF_StartBeh_Skolepenger,
                           EF_PeriodeAnn_OvergStoenad) // PeriodeAnn er lik StartBehandling i innhold
        for (type in InfotrygdHendelseType.values().filterNot { filter.contains(it) }) {
            val node = objectMapper.valueToTree<JsonNode>(testDtoForStartBehandling(type = type))
            val feilListe = schema.validate(node)
            assertThat(feilListe)
                    .withFailMessage("Feiler ikke $type")
                    .isNotEmpty
        }
    }

    @Test
    fun `SchemaValidering vedtak`() {
        assertThat(validerSkjema(EF_Vedtak_Skolepenger,
                                 mapOf("fnr" to "12312312311",
                                       "startdato" to "2010-01-01"
                                 ))).isEmpty()
    }

    @Test
    fun `SchemaValidering start behandling`() {
        assertThat(validerSkjema(EF_StartBeh_Skolepenger,
                                 mapOf("fnr" to "12312312311"))).isEmpty()
    }

    @Test
    fun `SchemaValidering periode savner sluttdato`() {
        val innhold = mapOf("fnr" to "12312312311",
                            "startdato" to "2010-01-01")
        assertThat(validerSkjema(EF_Periode_OvergStoenad, innhold)).isNotEmpty
    }

    private fun validerSkjema(type: InfotrygdHendelseType, innhold: Map<String, String>): MutableSet<ValidationMessage>? {
        val node = objectMapper.valueToTree<JsonNode>(mapOf(
                "tittel" to "tittel",
                "inneholderFlereElementer" to true,
                "elementer" to listOf(mapOf(
                        "sekvensId" to 1,
                        "type" to type,
                        "metadata" to mapOf("opprettetDato" to "2018-04-18T09:03:29.202"),
                        "innhold" to innhold
                ))
        ))
        return schema.validate(node)
    }

    private fun testDtoForVedtak(fnr: String = "12345678910",
                                 type: InfotrygdHendelseType = EF_Vedtak_OvergStoenad): FeedDto =
            opprettFeed(type, VedtakInnhold(fnr = fnr, startdato = LocalDate.now()))

    private fun testDtoForStartBehandling(fnr: String = "12345678910",
                                          type: InfotrygdHendelseType = EF_StartBeh_OvergStoenad): FeedDto =
            opprettFeed(type, StartBehandlingInnhold(fnr = fnr))

    private fun testDtoForPeriode(fnr: String = "12345678910",
                                  type: InfotrygdHendelseType = EF_Periode_OvergStoenad): FeedDto =
            opprettFeed(type, PeriodeInnhold(fnr = fnr, startdato = LocalDate.now(), sluttdato = LocalDate.now()))

    private fun testDtoForPeriodeAnnulert(fnr: String = "12345678910",
                                          type: InfotrygdHendelseType = EF_PeriodeAnn_OvergStoenad): FeedDto =
            opprettFeed(type, PeriodeAnnulertInnhold(fnr = fnr))

    private fun opprettFeed(type: InfotrygdHendelseType,
                            innhold: Innhold) =
            FeedDto(
                    tittel = "Feed schema validator test",
                    inneholderFlereElementer = false,
                    elementer = listOf(
                            FeedElement(
                                    sekvensId = 42,
                                    type = type,
                                    metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                                    innhold = innhold
                            ))
            )

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