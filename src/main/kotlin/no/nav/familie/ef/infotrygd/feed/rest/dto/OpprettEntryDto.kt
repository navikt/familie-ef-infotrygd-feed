package no.nav.familie.ef.infotrygd.feed.rest.dto

import java.time.LocalDate

data class OpprettEntryDto(val fnr: String, val startdato: LocalDate, val type: Type)