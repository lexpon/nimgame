package it.lexpon.nim.datatransferobjects

import it.lexpon.nim.domainobjects.Player

data class StartGameResponse(
        val nextPlayer: Player,
        val numberOfSticks: Int
)