package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.service.NimService
import it.lexpon.nim.periphery.datatransferobject.GameInformationResponse
import mu.KLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class NimController(
        private val nimService: NimService
) {

    companion object : KLogging()

    @GetMapping("/status")
    fun getGameStatus(): GameInformationResponse =
            GameInformationResponse(nimService.getGameInformation())
                    .also { logger.info { it } }

    @PostMapping("/start")
    fun startGame(): GameInformationResponse {
        val moveInformation = nimService.startGame()
        val gameInformation = nimService.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Game started. Response=$it" } }
    }

    @PostMapping("/restart")
    fun reStartGame(): GameInformationResponse {
        val moveInformation = nimService.reStartGame()
        val gameInformation = nimService.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Game restarted. Response=$it" } }
    }

    @PostMapping("/end")
    fun endGame(): GameInformationResponse {
        val moveInformation = nimService.endGame()
        val gameInformation = nimService.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Game ended. Response=$it" } }

    }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): GameInformationResponse {
        val moveInformation = nimService.makeMove(sticksToPull)
        val gameInformation = nimService.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Made move. Response=$it" } }

    }

}
