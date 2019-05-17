package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.GameInformation
import it.lexpon.nim.core.domainobject.MoveInformation

data class GameStatusResponse(
        val gameState: String,
        val leftSticks: Int,
        val eventList: List<String>? = null,
        val winner: String? = null
) {
    constructor(gameInformation: GameInformation) : this(
            gameState = gameInformation.gameState.name,
            leftSticks = gameInformation.leftSticks,
            winner = gameInformation.winner?.name
    )

    constructor(moveInformation: MoveInformation, gameInformation: GameInformation) : this(
            gameState = gameInformation.gameState.name,
            leftSticks = gameInformation.leftSticks,
            winner = gameInformation.winner?.name,
            eventList = moveInformation.eventList.getGameEvents().map { it.message }
    )
}