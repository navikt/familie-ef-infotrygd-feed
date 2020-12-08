package no.nav.familie.ef.infotrygd.feed.database

import no.nav.familie.kontrakter.ef.infotrygd.StønadType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDate
import java.time.LocalDateTime

data class Feed(@Id
                val sekvensId: Int = 0,
                val type: HendelseType,
                @Column("stonad")
                val stønad: StønadType,
                var fnr: String,
                var startdato: LocalDate?,
                var sluttdato: LocalDate?,
                var opprettetDato: LocalDateTime = LocalDateTime.now())

enum class HendelseType {
    VEDTAK,
    START_BEHANDLING,
    PERIODE,
    PERIODE_ANNULERT
}