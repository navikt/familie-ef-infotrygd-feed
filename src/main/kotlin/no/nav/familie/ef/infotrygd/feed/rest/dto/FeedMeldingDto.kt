package no.nav.familie.ef.infotrygd.feed.rest.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class FeedMeldingDto(val elementer: List<FeedElement>,
                          val inneholderFlereElementer: Boolean,
                          val tittel: String)

data class FeedElement(val innhold: Innhold,
                       val metadata: ElementMetadata,
                       val sekvensId: Int,
                       val type: Type)

data class ElementMetadata(val opprettetDato: LocalDateTime) // TODO: NB K9 bruker java.time.OffSetDateTime her

data class Innhold(val fnr: String, val datoStartNyEF: LocalDate)

enum class Type(infotrygdType: String) {
    SKOLEPENGER_VEDTAK("EF_Vedtak_Skolepenger"),
    SKOLEPENGER_START_BEHANDLING("EF_StartBeh_Skolepenger"),

    OVERGANGSSTØNAD_VEDTAK("EF_Vedtak_OvergStoenad"),
    OVERGANGSSTØNAD_START_BEHANDLING("EF_StartBeh_OvergStoenad"),

    BARNETILSYN_VEDTAK("EF_Vedtak_Barnetilsyn"),
    BARNETILSYN_START_BEHANDLING("EF_StartBeh_Barnetilsyn");
}