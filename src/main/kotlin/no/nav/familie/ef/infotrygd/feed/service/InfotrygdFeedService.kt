package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.Feed
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class InfotrygdFeedService(val feedRepository: FeedRepository) {

    fun opprettNyFeed(opprettEntryDto: OpprettVedtakHendelseDto) {
        feedRepository.save(Feed(type = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr,
                                 startdato = opprettEntryDto.startdato))
    }

    fun opprettNyFeed(opprettEntryDto: OpprettStartBehandlingHendelseDto) {
        feedRepository.save(Feed(type = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr,
                                 startdato = null))
    }

    fun hentMeldingerFraFeed(sistLestSekvensId: Long, maxSize: Int = 100): List<Feed> =
            feedRepository.findBySekvensIdGreaterThanOrderBySekvensIdAsc(PageRequest.of(0, maxSize),
                                                                         sistLestSekvensId)
}