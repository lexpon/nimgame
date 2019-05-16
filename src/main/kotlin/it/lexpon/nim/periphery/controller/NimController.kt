package it.lexpon.nim.periphery.controller

import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.service.NimService
import it.lexpon.nim.periphery.datatransferobject.GameStatusResponse
import it.lexpon.nim.periphery.datatransferobject.LeftSticksResponse
import it.lexpon.nim.periphery.datatransferobject.NextPlayerResponse
import it.lexpon.nim.periphery.datatransferobject.PullSticksResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class NimController(
        private val nimService: NimService
) {


    @GetMapping("/status")
    fun getGameStatus(): GameStatusResponse =
            GameStatusResponse(nimService.getGameInformation())


    @PostMapping("/start")
    fun startGame(): GameStatusResponse =
            GameStatusResponse(nimService.startGame())

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