package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.MoveInformation
import it.lexpon.nim.core.domainobject.NimGameInformation

data class GameInformationResponse(
        val gameState: String,
        val leftSticks: Int? = null,
        val gameEvents: List<String>? = null,
        val winner: String? = null
) {
    constructor(gameInformation: NimGameInformation) : this(
            gameState = gameInformation.state.name,
            leftSticks = gameInformation.leftSticks,
            winner = gameInformation.winner?.name
    )

    constructor(moveInformation: MoveInformation, gameInformation: NimGameInformation) : this(
            gameState = gameInformation.state.name,
            leftSticks = gameInformation.leftSticks,
            winner = gameInformation.winner?.name,
            gameEvents = moveInformation.gameEvents.map { "${it.gameEventType}: ${it.message}" }
    )
}