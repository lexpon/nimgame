package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.GameHistory

data class GameHistoryResponse(
        val gameHistory: List<GameInfoResponse>
) {

    constructor(gameHistory: GameHistory) : this(
            gameHistory = gameHistory.gameHistory.map { GameInfoResponse(it) }
    )

}