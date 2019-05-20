package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.GameHistory

data class GameHistoryResponse(
        val gameInfos: List<GameInfoResponse>
) {

    constructor(gameHistory: GameHistory) : this(
            gameInfos = gameHistory.gameInfos.map { GameInfoResponse(it) }
    )

}