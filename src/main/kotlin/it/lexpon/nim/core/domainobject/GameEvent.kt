package it.lexpon.nim.core.domainobject

import it.lexpon.nim.core.domainobject.GameEventType.*
import it.lexpon.nim.core.exception.NotPossibleEventCombinationException

enum class GameEventType {
    START, HUMAN_MOVE, COMPUTER_MOVE, RESTART, END;

    companion object {
        fun possibleEventTypes() = listOf(
                listOf(START),
                listOf(START, COMPUTER_MOVE),
                listOf(HUMAN_MOVE, COMPUTER_MOVE),
                listOf(HUMAN_MOVE, COMPUTER_MOVE, END),
                listOf(HUMAN_MOVE, END),
                listOf(RESTART),
                listOf(RESTART, COMPUTER_MOVE),
                listOf(END)
        )
    }

}

class EventList(gameEventList: List<GameEvent>) {
    private val gameEventList: List<GameEvent>

    init {
        GameEvent.validateEventList(gameEventList)
        this.gameEventList = gameEventList
    }

    fun getGameEvents() = gameEventList
}

abstract class GameEvent(val message: String) {
    abstract val gameEventType: GameEventType

    companion object {
        fun validateEventList(gameEventList: List<GameEvent>) {
            val eventTypeList = gameEventList.map { it.gameEventType }
            val possibleEventTypes = GameEventType.possibleEventTypes()
            if (!possibleEventTypes.contains(eventTypeList))
                throw NotPossibleEventCombinationException("GameEvent combination $eventTypeList is not possible. Only these combinations are allowed: $possibleEventTypes")
        }
    }
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