package it.lexpon.nim.controller

import it.lexpon.nim.datatransferobjects.LeftSticksResponse
import it.lexpon.nim.datatransferobjects.NextPlayerResponse
import it.lexpon.nim.datatransferobjects.PullSticksResponse
import it.lexpon.nim.datatransferobjects.StartGameResponse
import it.lexpon.nim.domainobjects.Player
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class NimController {

    @PostMapping("/start")
    fun startGame(): StartGameResponse {
        // TODO implement
        return StartGameResponse(Player.HUMAN, 13)
    }

    @GetMapping("/nextplayer")
    fun getNextPlayer(): NextPlayerResponse {
        // TODO implement
        return NextPlayerResponse(Player.HUMAN)
    }

    @GetMapping("/leftsticks")
    fun getLeftSticks(): LeftSticksResponse {
        // TODO implement
        return LeftSticksResponse(13)
    }

    @PostMapping("/pullsticks")
    fun pullSticks(@RequestParam sticksToPull: Int): PullSticksResponse {
        // TODO implement
        return PullSticksResponse(true)
    }

}