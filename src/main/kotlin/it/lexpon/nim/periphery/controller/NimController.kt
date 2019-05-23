package it.lexpon.nim.periphery.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
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

    @ApiOperation(value = "Returns the current game information")
    @GetMapping("/status")
    fun getGameStatus(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.getGameInfo())
                    .also { logger.info { it } }

    @ApiOperation(
            value = "Starts a new Nim game",
            notes = "A new game can only be started if there is no game running currently. If there is a running game, you have to end it first, before you would start a new " +
                    "game. The first player is determined randomly. If the first player is the computer, then it will also make the first move automatically."
    )
    @PostMapping("/start")
    fun startGame(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.startGame())
                    .also { logger.info { "Game started. Response=$it" } }

    @ApiOperation(
            value = "Restart a game",
            notes = "A game can only be restarted if it is running currently."
    )
    @PostMapping("/restart")
    fun reStartGame(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.reStartGame())
                    .also { logger.info { "Game restarted. Response=$it" } }

    @ApiOperation(
            value = "End a game",
            notes = "A game can only be ended if it is running currently."
    )
    @PostMapping("/end")
    fun endGame(): GameInfoResponse =
            GameInfoResponse(nimGameHandler.endGame())
                    .also { logger.info { "Game ended. Response=$it" } }

    @ApiOperation(
            value = "Make a move and pull sticks",
            notes = "After the human move the computer will also make a move if possible. When there is no move possible anymore because the last stick was pulled then the winner" +
                    "will be shown in the response."
    )
    @PostMapping("/pullsticks")
    fun pullSticks(
            @RequestParam
            @ApiParam(
                    value = "Number of sticks which should be pulled by human",
                    example = "1"
            )
            sticksToPull: Int
    ): GameInfoResponse =
            GameInfoResponse(nimGameHandler.makeMove(sticksToPull))
                    .also { logger.info { "Made move. Response=$it" } }

    @ApiOperation(value = "Shows the games which have been run in the current session")
    @GetMapping("/history")
    fun getGameHistory(): GameHistoryResponse = GameHistoryResponse(nimGameHandler.getGameHistory())

}
