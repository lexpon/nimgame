package it.lexpon.nim.core.domainobject

import it.lexpon.nim.core.domainobject.GameEventType.*
import it.lexpon.nim.core.service.NimGameEventValidator

data class GameEventInfo(
        val gameEvents: List<GameEvent>
) {

    constructor(gameEvent: GameEvent) : this(
            gameEvents = listOf(gameEvent)
    )

    init {
        NimGameEventValidator.validateEventList(gameEvents)
    }

}

enum class GameEventType {
    START, HUMAN_MOVE, COMPUTER_MOVE, RESTART, END;
}

abstract class GameEvent(val message: String) {
    abstract val gameEventType: GameEventType

    override fun toString(): String = "${this.gameEventType}: ${this.message}"
}

class Start(message: String) : GameEvent(message) {
    override val gameEventType: GameEventType = START
}

class End(message: String) : GameEvent(message) {
    override val gameEventType: GameEventType = END
}

class Restart(message: String) : GameEvent(message) {
    override val gameEventType: GameEventType = RESTART
}

abstract class Move(message: String) : GameEvent(message)

class HumanMove(sticksPulled: Int) : Move("$sticksPulled sticks pulled by Human") {
    override val gameEventType: GameEventType = HUMAN_MOVE
}

class ComputerMove(sticksPulled: Int) : Move("$sticksPulled sticks pulled by Computer") {
    override val gameEventType: GameEventType = COMPUTER_MOVE
}