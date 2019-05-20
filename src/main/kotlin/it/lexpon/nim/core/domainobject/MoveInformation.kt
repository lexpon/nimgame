package it.lexpon.nim.core.domainobject

import it.lexpon.nim.core.service.NimGameEventValidator

data class MoveInformation(
        val gameEvents: List<GameEvent>
) {

    constructor(gameEvent: GameEvent) : this(
            gameEvents = listOf(gameEvent)
    )

    init {
        NimGameEventValidator.validateEventList(gameEvents)
    }

}