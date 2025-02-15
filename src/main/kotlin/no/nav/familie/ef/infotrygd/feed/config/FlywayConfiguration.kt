package no.nav.familie.ef.infotrygd.feed.config

import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!dev")
@ConditionalOnProperty("spring.flyway.enabled")
class FlywayConfiguration {
    @Bean
    fun flywayConfig(
        @Value("\${spring.cloud.vault.database.role}") role: String,
    ): FlywayConfigurationCustomizer =
        FlywayConfigurationCustomizer { c: FluentConfiguration ->
            c.initSql("SET ROLE \"$role\"")
        }
}
