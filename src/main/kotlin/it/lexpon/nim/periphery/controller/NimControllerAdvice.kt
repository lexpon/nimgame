package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.exception.GameNotStartableException
import it.lexpon.nim.periphery.datatransferobject.ErrorResponse
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [NimController::class])
class NimControllerAdvice {

    companion object : KLogging()

    @ExceptionHandler(GameNotStartableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleFeeNotFoundException(exception: GameNotStartableException): ErrorResponse =
            ErrorResponse(
                    message = "${exception.message}"
            ).also {
                logger.warn { it.message }
            }

}