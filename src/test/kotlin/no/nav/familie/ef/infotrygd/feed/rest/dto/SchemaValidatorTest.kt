package no.nav.familie.ef.infotrygd.feed.rest.dto

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import no.nav.familie.kontrakter.ef.infotrygd.InfotrygdHendelseType
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
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
        assertThat(feilListe).hasSize(1)
    }

    @Test
    fun `Dto for vedtak validerer ikke dersom fnrStoenadsmottaker har feil format`() {
        val node = objectMapper.valueToTree<JsonNode>(testDtoForVedtak("123456"))
        val feilListe = schema.validate(node)
        assertThat(feilListe).hasSize(1)
    }

    private fun testDtoForVedtak(fnr: String = "12345678910"): FeedDto {
        return FeedDto(
                tittel = "Feed schema validator test",
                inneholderFlereElementer = false,
                elementer = listOf(
                        FeedElement(
                                innhold = VedtakInnhold(fnr = fnr, startdatoVedtakEF = LocalDate.now()),
                                metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                                sekvensId = 42,
                                type = InfotrygdHendelseType.EF_Vedtak_OvergStoenad
                        ))
        )
    }

    private fun testDtoForStartBehandling(fnr: String = "12345678910"): FeedDto {
        return FeedDto(
                tittel = "Feed schema validator test",
                inneholderFlereElementer = false,
                elementer = listOf(
                        FeedElement(
                                innhold = StartBehandlingInnhold(fnr = fnr),
                                metadata = ElementMetadata(opprettetDato = LocalDateTime.now()),
                                sekvensId = 42,
                                type = InfotrygdHendelseType.EF_StartBeh_OvergStoenad
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