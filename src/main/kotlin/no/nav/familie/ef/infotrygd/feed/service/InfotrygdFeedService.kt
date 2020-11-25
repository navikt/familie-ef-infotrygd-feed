package no.nav.familie.ef.infotrygd.feed.service

import no.nav.familie.ef.infotrygd.feed.database.Feed
import no.nav.familie.ef.infotrygd.feed.database.FeedRepository
import no.nav.familie.ef.infotrygd.feed.database.HendelseType
import no.nav.familie.kontrakter.ef.infotrygd.OpprettPeriodeHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettStartBehandlingHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.OpprettVedtakHendelseDto
import no.nav.familie.kontrakter.ef.infotrygd.StønadType
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class InfotrygdFeedService(val feedRepository: FeedRepository) {

    fun opprettNyFeed(opprettEntryDto: OpprettVedtakHendelseDto) {
        feedRepository.save(Feed(type = HendelseType.VEDTAK,
                                 saksnummer = opprettEntryDto.saksnummer,
                                 stønad = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr,
                                 startdato = opprettEntryDto.startdato,
                                 sluttdato = null))
    }

    fun opprettNyFeed(opprettEntryDto: OpprettStartBehandlingHendelseDto) {
        feedRepository.save(Feed(type = HendelseType.START_BEHANDLING,
                                 saksnummer = opprettEntryDto.saksnummer,
                                 stønad = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr,
                                 startdato = null,
                                 sluttdato = null))
    }

    fun opprettNyFeed(opprettEntryDto: OpprettPeriodeHendelseDto) {
        if (opprettEntryDto.type != StønadType.OVERGANGSSTØNAD) {
            throw IllegalArgumentException("Støtter kun periode for overgangsstonad")
        }
        feedRepository.save(Feed(type = HendelseType.PERIODE,
                                 saksnummer = opprettEntryDto.saksnummer,
                                 stønad = opprettEntryDto.type,
                                 fnr = opprettEntryDto.fnr,
                                 startdato = opprettEntryDto.startdato,
                                 sluttdato = opprettEntryDto.sluttdato))
    }

    fun hentMeldingerFraFeed(sistLestSekvensId: Long, maxSize: Int = 100): List<Feed> =
            feedRepository.findBySekvensIdGreaterThanOrderBySekvensIdAsc(PageRequest.of(0, maxSize),
                                                                         sistLestSekvensId)
}