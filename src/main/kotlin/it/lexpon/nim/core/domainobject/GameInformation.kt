package it.lexpon.nim.core.domainobject

data class GameInformation(
        val gameState: GameState,
        val leftSticks: Int,
        val winner: Player? = null
)