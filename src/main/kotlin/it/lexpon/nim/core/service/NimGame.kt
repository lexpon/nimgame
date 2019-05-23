package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameInfo
import it.lexpon.nim.core.domainobject.GameState
import it.lexpon.nim.core.domainobject.GameState.ENDED
import it.lexpon.nim.core.domainobject.GameState.RUNNING
import it.lexpon.nim.core.domainobject.Player
import it.lexpon.nim.core.domainobject.Player.COMPUTER
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.exception.GameNotEndableException
import it.lexpon.nim.core.exception.GameNotRestartableException
import it.lexpon.nim.core.exception.NumberOfSticksToPullException
import it.lexpon.nim.core.exception.PullingSticksNotPossibleException
import mu.KLogging

class NimGame private constructor(
        private var id: Int,
        private var state: GameState,
        private var leftSticks: Int,
        private var currentPlayer: Player,
        private var winner: Player? = null
) {


    companion object : KLogging() {
        private const val STICKS_START = 13
        private val STICKS_TO_PULL_POSSIBLE = listOf(1, 2, 3)

        fun startGame(gameId: Int, firstPlayer: Player): NimGame {
            val game = NimGame(
                    id = gameId,
                    state = RUNNING,
                    leftSticks = STICKS_START,
                    currentPlayer = firstPlayer
            )
            logger.debug { "Started new game: $game" }
            return game
        }
    }

    fun getGameInfo() = GameInfo(id, state, leftSticks, currentPlayer, winner)

    fun endGame() {
        if (state != RUNNING)
            throw GameNotEndableException("Cannot end game. Game has to have gameState=$RUNNING to be ended.")

        state = ENDED
        logger.debug { "Game ended: $this" }
    }

    fun restartGame(firstPlayer: Player) {
        if (state != RUNNING)
            throw GameNotRestartableException("Cannot restart game. Game has to have gameState=$RUNNING to be restarted.")

        leftSticks = STICKS_START
        currentPlayer = firstPlayer
        winner = null
        logger.debug { "Game restarted: $this" }
    }

    fun pullSticks(sticksToPull: Int) {
        if (state != RUNNING)
            throw PullingSticksNotPossibleException("Not possible to pull sticks when game has state=$state. It needs to have state=$RUNNING")

        val allowedNumberOfSticks = getPossibleSticksToPull()
        if (!allowedNumberOfSticks.contains(sticksToPull))
            throw NumberOfSticksToPullException("Not possible to pull $sticksToPull sticks. Number of sticks has to be in $allowedNumberOfSticks")

        leftSticks = leftSticks - sticksToPull
        val playerBeforePullingSticks = currentPlayer
        val nextPlayer = when (playerBeforePullingSticks) {
            HUMAN -> COMPUTER
            COMPUTER -> HUMAN
        }
        winner = if (leftSticks == 0) nextPlayer else null
        if (winner == null)
            currentPlayer = nextPlayer
        state = winner?.let { ENDED } ?: RUNNING

        logger.debug { "Pulled $sticksToPull stick by $playerBeforePullingSticks: $this" }
    }

    fun getPossibleSticksToPull(): List<Int> {
        if (leftSticks >= STICKS_TO_PULL_POSSIBLE.max()!!)
            return STICKS_TO_PULL_POSSIBLE

        val min = STICKS_TO_PULL_POSSIBLE.min()!!
        val max = leftSticks
        return IntRange(min, max).toList()
    }

    override fun toString() = getGameInfo().toString()

}
