package it.lexpon.nim.core.domainobject

import it.lexpon.nim.core.domainobject.GameEventType.*

enum class GameEventType {
    START, HUMAN_MOVE, COMPUTER_MOVE, RESTART, END;
}

abstract class GameEvent(val message: String) {
    abstract val gameEventType: GameEventType
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