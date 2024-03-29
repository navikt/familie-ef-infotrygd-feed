package no.nav.familie.ef.infotrygd.feed.config

import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(TokenGeneratorConfiguration::class)
class SecurityTestConfig
