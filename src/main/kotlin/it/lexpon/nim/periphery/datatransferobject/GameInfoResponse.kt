package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.GameEventInfo
import it.lexpon.nim.core.domainobject.GameInfo

data class GameInfoResponse(
        val id: Int,
        val gameState: String,
        val leftSticks: Int? = null,
        val gameEvents: List<String>? = null,
        val winner: String? = null
) {
    constructor(gameInfo: GameInfo) : this(
            id = gameInfo.id,
            gameState = gameInfo.state.name,
            leftSticks = gameInfo.leftSticks,
            winner = gameInfo.winner?.name
    )

    constructor(gameEventInfo: GameEventInfo) : this(
            id = gameEventInfo.gameInfo.id,
            gameState = gameEventInfo.gameInfo.state.name,
            leftSticks = gameEventInfo.gameInfo.leftSticks,
            winner = gameEventInfo.gameInfo.winner?.name,
            gameEvents = gameEventInfo.gameEvents.map { it.toString() }
    )
}