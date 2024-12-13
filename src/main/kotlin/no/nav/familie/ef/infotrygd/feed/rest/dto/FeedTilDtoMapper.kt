package no.nav.familie.ef.infotrygd.feed.rest.dto

import no.nav.familie.ef.infotrygd.feed.database.Feed
import no.nav.familie.ef.infotrygd.feed.database.HendelseType
import no.nav.familie.kontrakter.ef.felles.StønadType

fun konverterTilFeedMeldingDto(feedListe: List<Feed>): FeedDto =
    FeedDto(
        tittel = "Enslig forsørger feed",
        inneholderFlereElementer = feedListe.size > 1,
        elementer =
            feedListe.map {
                FeedElement(
                    sekvensId = it.sekvensId,
                    type = mapHendelseType(it.type, it.stønad),
                    metadata = ElementMetadata(opprettetDato = it.opprettetDato),
                    innhold = mapInnhold(it),
                )
            },
    )

private fun mapInnhold(it: Feed) =
    when (it.type) {
        HendelseType.VEDTAK -> VedtakInnhold(it.personIdent, it.startdato!!)
        HendelseType.START_BEHANDLING -> StartBehandlingInnhold(it.personIdent)
        HendelseType.PERIODE -> PeriodeInnhold(it.personIdent, it.startdato!!, it.sluttdato!!, it.fullOvergangsstonad!!)
        HendelseType.PERIODE_ANNULERT -> PeriodeAnnulertInnhold(it.personIdent)
    }

private fun mapHendelseType(
    hendelseType: HendelseType,
    stønadType: StønadType,
) = when (hendelseType) {
    HendelseType.VEDTAK ->
        when (stønadType) {
            StønadType.BARNETILSYN -> InfotrygdHendelseType.EF_Vedtak_Barnetilsyn
            StønadType.OVERGANGSSTØNAD -> InfotrygdHendelseType.EF_Vedtak_OvergStoenad
            StønadType.SKOLEPENGER -> InfotrygdHendelseType.EF_Vedtak_Skolepenger
        }
    HendelseType.START_BEHANDLING ->
        when (stønadType) {
            StønadType.BARNETILSYN -> InfotrygdHendelseType.EF_StartBeh_Barnetilsyn
            StønadType.OVERGANGSSTØNAD -> InfotrygdHendelseType.EF_StartBeh_OvergStoenad
            StønadType.SKOLEPENGER -> InfotrygdHendelseType.EF_StartBeh_Skolepenger
        }
    HendelseType.PERIODE ->
        when (stønadType) {
            StønadType.OVERGANGSSTØNAD -> InfotrygdHendelseType.EF_Periode_OvergStoenad
            else -> error("Har ikke mappet fler periodehendelser enn til overgangsstønad")
        }
    HendelseType.PERIODE_ANNULERT ->
        when (stønadType) {
            StønadType.OVERGANGSSTØNAD -> InfotrygdHendelseType.EF_PeriodeAnn_OvergStoenad
            else -> error("Har ikke mappet fler annulert periodehendelser enn til overgangsstønad")
        }
}
