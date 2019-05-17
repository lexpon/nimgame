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
    fun startGame(): GameStatusResponse =
            GameStatusResponse(nimService.startGame())
                    .also { logger.info { "Game started. Response=$it" } }

    @PostMapping("/restart")
    fun reStartGame(): GameStatusResponse =
            GameStatusResponse(nimService.reStartGame())
                    .also { logger.info { "Game restarted. Response=$it" } }

    @PostMapping("/end")
    fun endGame(): GameStatusResponse =
            GameStatusResponse(nimService.endGame())
                    .also { logger.info { "Game ended. Response=$it" } }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): GameStatusResponse =
            GameStatusResponse(nimService.makeMove(sticksToPull))
                    .also { logger.info { "Made move. Response=$it" } }

}