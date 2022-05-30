package no.nav.familie.ef.infotrygd.feed.database

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface FeedRepository : CrudRepository<Feed, Long> {

    fun findBySekvensIdGreaterThanOrderBySekvensIdAsc(pageable: Pageable, sekvensId: Long): List<Feed>
}
