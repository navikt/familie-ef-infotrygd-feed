package no.nav.familie.ef.infotrygd.feed.database

import no.nav.familie.ef.infotrygd.feed.rest.dto.Type
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.time.LocalDateTime

data class Feed(@Id
                val sekvensId: Long = 0,
                val type: Type,
                var fnrStonadsmottaker: String? = null,
                var datoStartNyBa: LocalDate? = null,
                var fnrBarn: String? = null,
                var erDuplikat: Boolean? = false,
                var opprettetDato: LocalDateTime)

