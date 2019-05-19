package it.lexpon.nim.core.domainobject

import it.lexpon.nim.core.service.NimGameInformation

data class GameInformation(
        val state: GameState,
        val leftSticks: Int? = null,
        val currentPlayer: Player? = null,
        val winner: Player? = null
) {
    constructor(info: NimGameInformation) : this(
            state = info.state,
            leftSticks = info.leftSticks,
            currentPlayer = info.currentPlayer,
            winner = info.winner
    )
}