package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.Player
import org.springframework.stereotype.Component

@Component
class RandomPlayerGenerator {

    fun determineRandomPlayer(): Player = Player.values().toList().shuffled().first()

}