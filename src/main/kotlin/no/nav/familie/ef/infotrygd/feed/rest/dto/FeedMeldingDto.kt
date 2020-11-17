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

data class Innhold(val fnr: String, val startdato: LocalDate)

enum class Type(infotrygdType: String) {
    SKOLEPENGER("EF_Vedtak_Skolepenger_v1"),
    OVERGANGSSTØNAD("EF_Vedtak_OvergStoenad_v1"),
    BARNETILSYN("EF_Vedtak_Barnetilsyn_v1");
}