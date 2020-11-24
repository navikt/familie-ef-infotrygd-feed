package no.nav.familie.ef.infotrygd.feed.database

import no.nav.familie.kontrakter.ef.infotrygd.InfotrygdHendelseType
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.time.LocalDateTime

data class Feed(@Id
                val sekvensId: Int = 0,
                val type: InfotrygdHendelseType,
                var fnr: String,
                var startdato: LocalDate?,
                var opprettetDato: LocalDateTime = LocalDateTime.now())

