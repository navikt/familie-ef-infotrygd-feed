package no.nav.familie.ef.infotrygd.feed.database

import no.nav.familie.ef.infotrygd.feed.rest.dto.Type
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface FeedRepository : CrudRepository<Feed, Long> {

    /*@Query(value = "SELECT f FROM Feed f WHERE f.sekvensId > :sistLesteSekvensId AND f.duplikat = false ORDER BY f.sekvensId asc")
    fun finnMeldingerMedSekvensIdStørreEnn(pageable: Pageable, sistLesteSekvensId: Long): List<Feed>*/
    fun findByErDuplikatIsFalseAndSekvensIdGreaterThanOrderBySekvensIdAsc(pageable: Pageable, sekvensId: Long): List<Feed>

    /*@Query(value = "SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false end FROM Feed f WHERE f.type = :type AND f.fnrBarn = :fnrBarn")
    fun erDuplikatFoedselsmelding(type: Type, fnrBarn: String): Boolean*/

    fun countByTypeAndFnrBarn(type: Type, fnrBarn: String): Long
}