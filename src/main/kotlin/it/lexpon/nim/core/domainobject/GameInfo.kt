package it.lexpon.nim.core.domainobject

data class GameInfo(
        val id: Int,
        val state: GameState,
        val leftSticks: Int,
        val currentPlayer: Player,
        val winner: Player? = null
)