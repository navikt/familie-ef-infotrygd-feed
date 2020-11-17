package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.Feed
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.rest.dto.OpprettEntryDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class InfotrygdFeedService(val feedRepository: FeedRepository) {

    fun opprettNyFeed(opprettEntryDto: OpprettEntryDto) {
        feedRepository.save(Feed(type = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr,
                                 startdato = opprettEntryDto.startdato))
    }

    fun hentMeldingerFraFeed(sistLestSekvensId: Long, maxSize: Int = 100): List<Feed> =
            feedRepository.findByErDuplikatIsFalseAndSekvensIdGreaterThanOrderBySekvensIdAsc(PageRequest.of(0, maxSize),
                                                                                             sistLestSekvensId)
}