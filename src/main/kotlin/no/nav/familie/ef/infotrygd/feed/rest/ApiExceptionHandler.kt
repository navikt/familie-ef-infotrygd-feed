package no.nav.familie.ef.infotrygd.feed.rest

import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.core.NestedExceptionUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception

@Suppress("unused")
@ControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    private fun rootCause(throwable: Throwable): String = NestedExceptionUtils.getMostSpecificCause(throwable).javaClass.simpleName

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        secureLogger.error("En feil har oppstått", ex)
        logger.error("En feil har oppstått - throwable=${rootCause(ex)} status=${statusCode.value()}")
        return super.handleExceptionInternal(ex, body, headers, statusCode, request)
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(throwable: Throwable): ResponseEntity<String> {
        val responseStatus = throwable::class.annotations.find { it is ResponseStatus }?.let { it as ResponseStatus }
        if (responseStatus != null) {
            return håndtertResponseStatusFeil(throwable, responseStatus)
        }
        return uventetFeil(throwable)
    }

    private fun uventetFeil(throwable: Throwable): ResponseEntity<String> {
        secureLogger.error("En feil har oppstått ${throwable.message}", throwable)
        logger.error("En feil har oppstått - throwable=${rootCause(throwable)} ")
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Uventet feil")
    }

    // Denne håndterer eks JwtTokenUnauthorizedException
    private fun håndtertResponseStatusFeil(
        throwable: Throwable,
        responseStatus: ResponseStatus,
    ): ResponseEntity<String> {
        val status = if (responseStatus.value != HttpStatus.INTERNAL_SERVER_ERROR) responseStatus.value else responseStatus.code
        val loggMelding =
            "En håndtert feil har oppstått" +
                " throwable=${rootCause(throwable)}" +
                " reason=${responseStatus.reason}" +
                " status=$status"

        loggFeil(throwable, loggMelding)
        return ResponseEntity.status(status).body("Håndtert feil")
    }

    private fun loggFeil(
        throwable: Throwable,
        loggMelding: String,
    ) {
        when (throwable) {
            is JwtTokenUnauthorizedException -> logger.warn(loggMelding)
            else -> logger.error(loggMelding)
        }
    }
}
