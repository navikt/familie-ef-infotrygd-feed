package no.nav.familie.ef.infotrygd.feed.rest.dto

data class FeedElement(
        val innhold: Innhold,
        val metadata: ElementMetadata,
        val sekvensId: Int,
        val type: Type
)