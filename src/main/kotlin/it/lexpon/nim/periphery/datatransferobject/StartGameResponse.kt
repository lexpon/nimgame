package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.GameInformation
import it.lexpon.nim.core.domainobject.GameStatus
import it.lexpon.nim.core.domainobject.Player

data class StartGameResponse(
        val gameStatus: GameStatus,
        val nextPlayer: Player,
        val leftSticks: Int
) {
    constructor(gameInformation: GameInformation) : this(
            gameStatus = gameInformation.gameStatus,
            nextPlayer = gameInformation.nextPlayer!!,
            leftSticks = gameInformation.leftSticks
    )
}