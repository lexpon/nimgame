package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameEvent
import it.lexpon.nim.core.domainobject.GameEventType
import it.lexpon.nim.core.exception.NotPossibleEventCombinationException

object NimGameEventValidator {

    fun validateEventList(gameEventList: List<GameEvent>) {
        val eventTypeList = gameEventList.map { it.gameEventType }
        val possibleEventTypes = possibleEventTypes()
        if (!possibleEventTypes.contains(eventTypeList))
            throw NotPossibleEventCombinationException("GameEvent combination $eventTypeList is not possible. Only these combinations are allowed: $possibleEventTypes")
    }

    private fun possibleEventTypes() = listOf(
            listOf(GameEventType.START),
            listOf(GameEventType.START, GameEventType.COMPUTER_MOVE),
            listOf(GameEventType.HUMAN_MOVE, GameEventType.COMPUTER_MOVE),
            listOf(GameEventType.HUMAN_MOVE, GameEventType.COMPUTER_MOVE, GameEventType.END),
            listOf(GameEventType.HUMAN_MOVE, GameEventType.END),
            listOf(GameEventType.RESTART),
            listOf(GameEventType.RESTART, GameEventType.COMPUTER_MOVE),
            listOf(GameEventType.END)
    )
}