package no.nav.familie.ef.infotrygd.feed.rest.dto

import java.time.LocalDate
import java.time.LocalDateTime

enum class InfotrygdHendelseType {
    EF_Vedtak_Skolepenger,
    EF_Vedtak_OvergStoenad,
    EF_Vedtak_Barnetilsyn,

    EF_StartBeh_Skolepenger,
    EF_StartBeh_OvergStoenad,
    EF_StartBeh_Barnetilsyn,

    EF_Periode_OvergStoenad,
    EF_PeriodeAnn_OvergStoenad
}

data class FeedDto(val elementer: List<FeedElement>,
                   val inneholderFlereElementer: Boolean,
                   val tittel: String)

data class FeedElement(val sekvensId: Int,
                       val type: InfotrygdHendelseType,
                       val metadata: ElementMetadata,
                       val innhold: Innhold)

// opprettetDato blir trunkert hos infotrygd, men er fint å ha med hele datot hvis det trengs for noen andre
data class ElementMetadata(val opprettetDato: LocalDateTime)

interface Innhold

data class VedtakInnhold(val fnr: String, val startdato: LocalDate) : Innhold
data class StartBehandlingInnhold(val fnr: String) : Innhold
data class PeriodeInnhold(val fnr: String, val startdato: LocalDate, val sluttdato: LocalDate) : Innhold
data class PeriodeAnnulertInnhold(val fnr: String) : Innhold