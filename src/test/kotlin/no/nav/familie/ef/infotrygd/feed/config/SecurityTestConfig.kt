package no.nav.familie.ef.infotrygd.feed.config

import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

@Configuration
@Import(TokenGeneratorConfiguration::class)
class SecurityTestConfig{

    @Bean
    @Primary
    @Qualifier("oidcTokenValidationFilterRegistrationBean")
    fun abc() = "s"
}