package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.service.NimService
import it.lexpon.nim.periphery.datatransferobject.GameStatusResponse
import mu.KLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class NimController(
        private val nimService: NimService
) {

    companion object : KLogging()

    @GetMapping("/status")
    fun getGameStatus(): GameStatusResponse =
            GameStatusResponse(nimService.getGameInformation())
                    .also { logger.info { it } }

    @PostMapping("/start")
    fun startGame(): GameStatusResponse {
        val moveInformation = nimService.startGame()
        val gameInformation = nimService.getGameInformation()

        return GameStatusResponse(moveInformation, gameInformation)
                .also { logger.info { "Game started. Response=$it" } }
    }

    @PostMapping("/restart")
    fun reStartGame(): GameStatusResponse {
        val moveInformation = nimService.reStartGame()
        val gameInformation = nimService.getGameInformation()

        return GameStatusResponse(moveInformation, gameInformation)
                .also { logger.info { "Game restarted. Response=$it" } }
    }

    @PostMapping("/end")
    fun endGame(): GameStatusResponse {
        val moveInformation = nimService.endGame()
        val gameInformation = nimService.getGameInformation()

        return GameStatusResponse(moveInformation, gameInformation)
                .also { logger.info { "Game ended. Response=$it" } }

    }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): GameStatusResponse {
        val moveInformation = nimService.makeMove(sticksToPull)
        val gameInformation = nimService.getGameInformation()

        return GameStatusResponse(moveInformation, gameInformation)
                .also { logger.info { "Made move. Response=$it" } }

    }

}
