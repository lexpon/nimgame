package it.lexpon.nim.core.domainobject

data class GameInformation(
        val gameStatus: GameStatus,
        val leftSticks: Int,
        val nextPlayer: Player?,
        val winner: Player? = null
)