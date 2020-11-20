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
                            if (it.type.erVedtak())
                                VedtakInnhold(fnr = it.fnr, startdatoVedtakEF = it.startdato!!)
                            else
                                StartBehandlingInnhold(fnr = it.fnr),
                            sekvensId = it.sekvensId,
                            type = it.type
                    )
                })
