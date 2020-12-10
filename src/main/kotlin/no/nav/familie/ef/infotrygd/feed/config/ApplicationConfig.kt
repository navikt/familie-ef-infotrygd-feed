package no.nav.familie.ef.infotrygd.feed.config

import no.nav.familie.log.filter.LogFilter
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootConfiguration
@ConfigurationPropertiesScan
@EnableJwtTokenValidation(ignore = ["org.springframework", "springfox.documentation.swagger"])
@ComponentScan(ApplicationConfig.pakkenavn)
class ApplicationConfig {

    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        log.info("Registering LogFilter filter")
        val filterRegistration: FilterRegistrationBean<LogFilter> = FilterRegistrationBean()
        filterRegistration.filter = LogFilter()
        filterRegistration.order = 1
        return filterRegistration
    }

    companion object {

        private val log = LoggerFactory.getLogger(ApplicationConfig::class.java)
        const val pakkenavn = "no.nav.familie.ef.infotrygd.feed"
    }
}