package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.service.NimGameHandler
import it.lexpon.nim.periphery.datatransferobject.GameHistoryResponse
import it.lexpon.nim.periphery.datatransferobject.GameInfoResponse
import mu.KLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class NimController(
        private val nimGameHandler: NimGameHandler
) {

    companion object : KLogging()

    @GetMapping("/status")
    fun getGameStatus(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.getGameInfo())
                    .also { logger.info { it } }

    @PostMapping("/start")
    fun startGame(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.startGame())
                    .also { logger.info { "Game started. Response=$it" } }

    @PostMapping("/restart")
    fun reStartGame(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.reStartGame())
                    .also { logger.info { "Game restarted. Response=$it" } }

    @PostMapping("/end")
    fun endGame(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.endGame())
                    .also { logger.info { "Game ended. Response=$it" } }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): GameInfoResponse =
            GameInfoResponse(nimGameHandler.makeMove(sticksToPull))
                    .also { logger.info { "Made move. Response=$it" } }

    @GetMapping("/history")
    fun getGameHistory(): GameHistoryResponse = GameHistoryResponse(nimGameHandler.getGameHistory())

}
