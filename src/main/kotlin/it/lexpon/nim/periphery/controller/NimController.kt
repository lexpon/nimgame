package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.domainobject.GameStatus.NOT_STARTED
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.service.NimService
import it.lexpon.nim.periphery.datatransferobject.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class NimController(
        private val nimService: NimService
) {


    @GetMapping("/status")
    fun getGameStatus(): GameStatusResponse {
        // TODO implement
        return GameStatusResponse(NOT_STARTED)
    }


    @PostMapping("/start")
    fun startGame(): StartGameResponse {
        val gameInformation = nimService.startGame()
        return StartGameResponse(gameInformation)
    }

    @GetMapping("/nextplayer")
    fun getNextPlayer(): NextPlayerResponse {
        // TODO implement
        return NextPlayerResponse(HUMAN)
    }

    @GetMapping("/leftsticks")
    fun getLeftSticks(): LeftSticksResponse {
        // TODO implement
        return LeftSticksResponse(13)
    }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): PullSticksResponse {
        // TODO implement
        return PullSticksResponse(
                leftSticks = 13,
                message = "dummy message"
        )
    }

}