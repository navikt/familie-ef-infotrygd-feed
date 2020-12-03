package no.nav.familie.ef.infotrygd.feed.rest.dto

import no.nav.familie.ef.infotrygd.feed.database.Feed
import no.nav.familie.ef.infotrygd.feed.database.HendelseType
import no.nav.familie.kontrakter.ef.infotrygd.StønadType

fun konverterTilFeedMeldingDto(feedListe: List<Feed>): FeedDto =
        FeedDto(
                tittel = "Enslig forsørger feed",
                inneholderFlereElementer = feedListe.size > 1,
                elementer = feedListe.map {
                    FeedElement(
                            sekvensId = it.sekvensId,
                            type = mapHendelseType(it.type, it.stønad),
                            saksnummer = it.saksnummer,
                            metadata = ElementMetadata(opprettetDato = it.opprettetDato),
                            innhold = mapInnhold(it)
                    )
                })

private fun mapInnhold(it: Feed) =
        when (it.type) {
            HendelseType.VEDTAK -> VedtakInnhold(it.fnr, it.startdato!!)
            HendelseType.START_BEHANDLING -> StartBehandlingInnhold(it.fnr)
        }

private fun mapHendelseType(hendelseType: HendelseType, stønadType: StønadType) =
        when (hendelseType) {
            HendelseType.VEDTAK -> when (stønadType) {
                StønadType.BARNETILSYN -> InfotrygdHendelseType.EF_Vedtak_Barnetilsyn
                StønadType.OVERGANGSSTØNAD -> InfotrygdHendelseType.EF_Vedtak_OvergStoenad
                StønadType.SKOLEPENGER -> InfotrygdHendelseType.EF_Vedtak_Skolepenger
            }
            HendelseType.START_BEHANDLING -> when (stønadType) {
                StønadType.BARNETILSYN -> InfotrygdHendelseType.EF_StartBeh_Barnetilsyn
                StønadType.OVERGANGSSTØNAD -> InfotrygdHendelseType.EF_StartBeh_OvergStoenad
                StønadType.SKOLEPENGER -> InfotrygdHendelseType.EF_StartBeh_Skolepenger
            }
        }

