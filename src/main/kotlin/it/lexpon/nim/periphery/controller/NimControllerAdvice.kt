package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.exception.*
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
                GameNotEndableException::class,
                GameNotRestartableException::class,
                GameNotStartableException::class,
                MoveNotPossibleException::class,
                NoGameException::class,
                NumberOfSticksToPullException::class,
                PullingSticksNotPossibleException::class
            ]
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequests(exception: RuntimeException): ErrorResponse =
            ErrorResponse(
                    message = "${exception.message}"
            ).also {
                logger.warn { it.message }
            }

    @ExceptionHandler(
            value = [
                NotPossibleEventCombinationException::class
            ]
    )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalErrors(exception: RuntimeException): ErrorResponse =
            ErrorResponse(
                    message = "${exception.message}"
            ).also {
                logger.warn { it.message }
            }

}