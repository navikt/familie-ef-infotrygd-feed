package no.nav.familie.ef.infotrygd.feed.rest.dto

import no.nav.familie.ef.infotrygd.feed.database.Feed

fun konverterTilFeedMeldingDto(feedListe: List<Feed>): FeedMeldingDto =
        FeedMeldingDto(
                tittel = "Enslig forsørger feed",
                inneholderFlereElementer = feedListe.size > 1,
                elementer = feedListe.map {
                    FeedElement(
                            metadata = ElementMetadata(opprettetDato = it.opprettetDato),
                            innhold = Innhold(datoStartNyEF = it.startdato,
                                              fnr = it.fnr),
                            sekvensId = it.sekvensId,
                            type = it.type
                    )
                })
