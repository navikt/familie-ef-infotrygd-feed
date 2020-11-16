package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.Feed
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.rest.dto.Type
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class InfotrygdFeedService(val feedRepository: FeedRepository) {

    fun opprettNyFeed(type: Type,
            fnrBarn: String? = null,
            fnrStonadsmottaker: String? = null,
            datoStartNyBA: LocalDate? = null
    ) {
        val erDuplikat = type.takeIf { it == Type.BA_Foedsel_v1 }
                ?.let { feedRepository.countByTypeAndFnrBarn(type, fnrBarn!!) > 0 }
                ?: false

        feedRepository.save(
                Feed(
                        type = type,
                        fnrBarn = fnrBarn,
                        fnrStonadsmottaker = fnrStonadsmottaker,
                        datoStartNyBa = datoStartNyBA,
                        erDuplikat = erDuplikat,
                        opprettetDato = LocalDateTime.now()
                ))
    }

    fun hentMeldingerFraFeed(sistLestSekvensId: Long, maxSize: Int = 100): List<Feed> =
            feedRepository.findByErDuplikatIsFalseAndSekvensIdGreaterThanOrderBySekvensIdAsc(PageRequest.of(0, maxSize), sistLestSekvensId)
}