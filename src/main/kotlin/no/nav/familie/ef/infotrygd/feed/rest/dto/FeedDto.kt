package no.nav.familie.ef.infotrygd.feed.rest.dto

import no.nav.familie.kontrakter.ef.infotrygd.InfotrygdHendelseType
import java.time.LocalDate
import java.time.LocalDateTime

data class FeedDto(val elementer: List<FeedElement>,
                   val inneholderFlereElementer: Boolean,
                   val tittel: String)

data class FeedElement(val innhold: Innhold,
                       val metadata: ElementMetadata,
                       val sekvensId: Int,
                       val type: InfotrygdHendelseType)

data class ElementMetadata(val opprettetDato: LocalDateTime) // TODO: NB K9 bruker java.time.OffSetDateTime her

interface Innhold

data class VedtakInnhold(val fnr: String, val startdatoVedtakEF: LocalDate) : Innhold
data class StartBehandlingInnhold(val fnr: String) : Innhold

fun InfotrygdHendelseType.erVedtak() =
        this == InfotrygdHendelseType.EF_Vedtak_Skolepenger
        || this == InfotrygdHendelseType.EF_Vedtak_OvergStoenad
        || this == InfotrygdHendelseType.EF_Vedtak_Barnetilsyn

fun InfotrygdHendelseType.erStartBehandling() =
        this == InfotrygdHendelseType.EF_StartBeh_Skolepenger
        || this == InfotrygdHendelseType.EF_StartBeh_OvergStoenad
        || this == InfotrygdHendelseType.EF_StartBeh_Barnetilsyn