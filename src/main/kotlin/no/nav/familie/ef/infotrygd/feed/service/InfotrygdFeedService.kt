package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.Feed
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.database.HendelseType
import no.nav.familie.kontrakter.ef.infotrygd.OpprettPeriodeHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InfotrygdFeedService(val feedRepository: FeedRepository) {

    fun opprettNyFeed(opprettEntryDto: OpprettVedtakHendelseDto) {
        feedRepository.save(Feed(type = HendelseType.VEDTAK,
                                 stønad = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr,
                                 startdato = opprettEntryDto.startdato))
    }

    fun opprettNyFeed(opprettEntryDto: OpprettStartBehandlingHendelseDto) {
        feedRepository.save(Feed(type = HendelseType.START_BEHANDLING,
                                 stønad = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr))
    }

    @Transactional
    fun opprettNyFeed(opprettEntryDto: OpprettPeriodeHendelseDto) {
        feedRepository.save(Feed(type = HendelseType.PERIODE_ANNULERT,
                                 stønad = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr
        ))
        if (opprettEntryDto.perioder.isEmpty()) {
            return
        }
        feedRepository.saveAll(opprettEntryDto.perioder.map {
            Feed(type = HendelseType.PERIODE,
                 stønad = opprettEntryDto.type,
                 fnr = opprettEntryDto.fnr,
                 startdato = it.startdato,
                 sluttdato = it.sluttdato,
                 fullOvergangsstonad = opprettEntryDto.fullOvergangsstønad
            )
        })
    }

    fun hentMeldingerFraFeed(sistLestSekvensId: Long, maxSize: Int = 100): List<Feed> =
            feedRepository.findBySekvensIdGreaterThanOrderBySekvensIdAsc(PageRequest.of(0, maxSize),
                                                                         sistLestSekvensId)
}
