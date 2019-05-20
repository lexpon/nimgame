package it.lexpon.nim.core.domainobject

data class NimGameInformation(
        val state: GameState,
        val leftSticks: Int,
        val currentPlayer: Player,
        val winner: Player? = null
)