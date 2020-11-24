package no.nav.familie.ef.infotrygd.feed.rest.dto

import no.nav.familie.ef.infotrygd.feed.database.Feed

fun konverterTilFeedMeldingDto(feedListe: List<Feed>): FeedDto =
        FeedDto(
                tittel = "Enslig forsørger feed",
                inneholderFlereElementer = feedListe.size > 1,
                elementer = feedListe.map {
                    FeedElement(
                            metadata = ElementMetadata(opprettetDato = it.opprettetDato),
                            innhold =
                            when {
                                it.type.erVedtak() -> VedtakInnhold(fnr = it.fnr, startdatoVedtakEF = it.startdato!!)
                                it.type.erStartBehandling() -> StartBehandlingInnhold(fnr = it.fnr)
                                else -> throw IllegalStateException("Finner ikke mapping for ${it.type}")
                            },
                            sekvensId = it.sekvensId,
                            type = it.type
                    )
                })
