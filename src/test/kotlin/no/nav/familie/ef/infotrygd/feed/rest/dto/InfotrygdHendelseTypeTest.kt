package no.nav.familie.ef.infotrygd.feed.rest.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class InfotrygdHendelseTypeTest {

    @Test
    internal fun `Hendelsetype kan maks være 26 tegn`() {
        assertThat(InfotrygdHendelseType.values().filter { it.name.length > 26 }).isEmpty()
    }
}