package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.service.NimGameHandler
import it.lexpon.nim.periphery.datatransferobject.GameInformationResponse
import mu.KLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class NimController(
        private val nimGameHandler: NimGameHandler
) {

    companion object : KLogging()

    @GetMapping("/status")
    fun getGameStatus(): GameInformationResponse =
            GameInformationResponse(nimGameHandler.getGameInformation())
                    .also { logger.info { it } }

    @PostMapping("/start")
    fun startGame(): GameInformationResponse {
        val moveInformation = nimGameHandler.startGame()
        val gameInformation = nimGameHandler.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Game started. Response=$it" } }
    }

    @PostMapping("/restart")
    fun reStartGame(): GameInformationResponse {
        val moveInformation = nimGameHandler.reStartGame()
        val gameInformation = nimGameHandler.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Game restarted. Response=$it" } }
    }

    @PostMapping("/end")
    fun endGame(): GameInformationResponse {
        val moveInformation = nimGameHandler.endGame()
        val gameInformation = nimGameHandler.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Game ended. Response=$it" } }

    }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): GameInformationResponse {
        val moveInformation = nimGameHandler.makeMove(sticksToPull)
        val gameInformation = nimGameHandler.getGameInformation()

        return GameInformationResponse(moveInformation, gameInformation)
                .also { logger.info { "Made move. Response=$it" } }

    }

}
