package it.lexpon.nim.core.service

import org.springframework.stereotype.Component

@Component
class SticksToPullGenerator {

    fun getSticksToPullForComputer(game: NimGame): Int = game.getPossibleSticksToPull().shuffled().first()

}
