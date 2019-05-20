package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameState.ENDED
import it.lexpon.nim.core.domainobject.GameState.RUNNING
import it.lexpon.nim.core.domainobject.NimGameInformation
import it.lexpon.nim.core.domainobject.Player
import it.lexpon.nim.core.domainobject.Player.COMPUTER
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.exception.GameNotEndableException
import it.lexpon.nim.core.exception.GameNotRestartableException
import it.lexpon.nim.core.exception.PullSticksNotPossibleException
import it.lexpon.nim.core.exception.SticksToPullException
import mu.KLogging

class NimGame private constructor(private var info: NimGameInformation) {

    companion object : KLogging() {
        private const val STICKS_START = 13
        private val STICKS_TO_PULL_POSSIBLE = listOf(1, 2, 3)

        fun startGame(firstPlayer: Player): NimGame {
            val game = NimGame(
                    info = NimGameInformation(
                            state = RUNNING,
                            leftSticks = STICKS_START,
                            currentPlayer = firstPlayer
                    )
            )
            logger.debug { "Started new game. Info=${game.info}" }
            return game
        }
    }

    fun getNimGameInformation() = info

    fun endGame() {
        if (info.state != RUNNING)
            throw GameNotEndableException("Cannot end game. Game has to have gameState=$RUNNING to be ended.")

        info = info.copy(state = ENDED)
        logger.debug { "Game ended. NimGameInformation=$info" }
    }

    fun restartGame(firstPlayer: Player) {
        if (info.state != RUNNING)
            throw GameNotRestartableException("Cannot restart game. Game has to have gameState=$RUNNING to be restarted.")

        info = info.copy(
                leftSticks = STICKS_START,
                currentPlayer = firstPlayer,
                winner = null
        )
    }

    fun pullSticks(sticksToPull: Int) {
        if (info.state != RUNNING)
            throw PullSticksNotPossibleException("Not possible to pull sticks when game has state=${info.state}. It needs to have state=$RUNNING")

        val allowedNumberOfSticks = getPossibleSticksToPull()
        if (!allowedNumberOfSticks.contains(sticksToPull))
            throw SticksToPullException("Not possible to pull $sticksToPull sticks. Number of sticks has to be in $allowedNumberOfSticks")

        val currentPlayer = info.currentPlayer
        val nextLeftSticks = info.leftSticks - sticksToPull
        val nextPlayer = when (currentPlayer) {
            HUMAN -> COMPUTER
            COMPUTER -> HUMAN
        }
        val winner = if (nextLeftSticks == 0) nextPlayer else null
        val state = winner?.let { ENDED } ?: RUNNING

        info = info.copy(
                state = state,
                leftSticks = nextLeftSticks,
                currentPlayer = if (winner == null) nextPlayer else currentPlayer,
                winner = winner
        )

        logger.debug { "Pulled $sticksToPull by $currentPlayer. Info=$info" }
    }

    fun getPossibleSticksToPull(): List<Int> {
        if (info.leftSticks >= STICKS_TO_PULL_POSSIBLE.max()!!)
            return STICKS_TO_PULL_POSSIBLE

        val min = STICKS_TO_PULL_POSSIBLE.min()!!
        val max = info.leftSticks
        return IntRange(min, max).toList()
    }

}
