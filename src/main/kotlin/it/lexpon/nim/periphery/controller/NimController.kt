package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.service.NimGameHandler
import it.lexpon.nim.periphery.datatransferobject.GameHistoryResponse
import it.lexpon.nim.periphery.datatransferobject.GameInfoResponse
import mu.KLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class NimController(
        private val nimGameHandler: NimGameHandler
) {

    companion object : KLogging()

    @GetMapping("/status")
    fun getGameStatus(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.getGameInfo())
                    .also { logger.info { it } }

    @PostMapping("/start")
    fun startGame(): GameInfoResponse {
        val gameEventInfo = nimGameHandler.startGame()
        val gameInfo = nimGameHandler.getGameInfo()

        return GameInfoResponse(gameEventInfo, gameInfo)
                .also { logger.info { "Game started. Response=$it" } }
    }

    @PostMapping("/restart")
    fun reStartGame(): GameInfoResponse {
        val gameEventInfo = nimGameHandler.reStartGame()
        val gameInfo = nimGameHandler.getGameInfo()

        return GameInfoResponse(gameEventInfo, gameInfo)
                .also { logger.info { "Game restarted. Response=$it" } }
    }

    @PostMapping("/end")
    fun endGame(): GameInfoResponse {
        val gameEventInfo = nimGameHandler.endGame()
        val gameInfo = nimGameHandler.getGameInfo()

        return GameInfoResponse(gameEventInfo, gameInfo)
                .also { logger.info { "Game ended. Response=$it" } }

    }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): GameInfoResponse {
        val gameEventInfo = nimGameHandler.makeMove(sticksToPull)
        val gameInfo = nimGameHandler.getGameInfo()

        return GameInfoResponse(gameEventInfo, gameInfo)
                .also { logger.info { "Made move. Response=$it" } }

    }

    @GetMapping("/history")
    fun getGameHistory(): GameHistoryResponse = GameHistoryResponse(nimGameHandler.getGameHistory())

}
