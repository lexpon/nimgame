package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.exception.GameNotEndableException
import it.lexpon.nim.core.exception.GameNotRestartableException
import it.lexpon.nim.core.exception.GameNotStartableException
import it.lexpon.nim.core.exception.SticksToPullException
import it.lexpon.nim.periphery.datatransferobject.ErrorResponse
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [NimController::class])
class NimControllerAdvice {

    companion object : KLogging()

    @ExceptionHandler(
            value = [
                GameNotStartableException::class,
                GameNotRestartableException::class,
                GameNotEndableException::class,
                SticksToPullException::class
            ]
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleFeeNotFoundException(exception: RuntimeException): ErrorResponse =
            ErrorResponse(
                    message = "${exception.message}"
            ).also {
                logger.warn { it.message }
            }

}