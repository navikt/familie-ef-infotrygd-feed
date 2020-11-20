package no.nav.familie.ef.infotrygd.feed.database

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface FeedRepository : CrudRepository<Feed, Long> {

    /*@Query(value = "SELECT f FROM Feed f WHERE f.sekvensId > :sistLesteSekvensId AND f.duplikat = false ORDER BY f.sekvensId asc")
    fun finnMeldingerMedSekvensIdStørreEnn(pageable: Pageable, sistLesteSekvensId: Long): List<Feed>*/
    fun findBySekvensIdGreaterThanOrderBySekvensIdAsc(pageable: Pageable, sekvensId: Long): List<Feed>

}