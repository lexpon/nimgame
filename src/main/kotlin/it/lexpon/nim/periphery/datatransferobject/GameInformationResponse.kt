package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.GameInformation
import it.lexpon.nim.core.domainobject.MoveInformation

data class GameInformationResponse(
        val gameState: String,
        val leftSticks: Int? = null,
        val gameEvents: List<String>? = null,
        val winner: String? = null
) {
    constructor(gameInformation: GameInformation) : this(
            gameState = gameInformation.state.name,
            leftSticks = gameInformation.leftSticks,
            winner = gameInformation.winner?.name
    )

    constructor(moveInformation: MoveInformation, gameInformation: GameInformation) : this(
            gameState = gameInformation.state.name,
            leftSticks = gameInformation.leftSticks,
            winner = gameInformation.winner?.name,
            gameEvents = moveInformation.gameEventList.getGameEvents().map { "${it.gameEventType}: ${it.message}" }
    )
}