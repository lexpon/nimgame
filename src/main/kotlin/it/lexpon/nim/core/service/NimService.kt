package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameInformation
import it.lexpon.nim.core.domainobject.GameStatus
import it.lexpon.nim.core.domainobject.GameStatus.*
import it.lexpon.nim.core.domainobject.Player
import it.lexpon.nim.core.exception.GameNotEndableException
import it.lexpon.nim.core.exception.GameNotRestartableException
import it.lexpon.nim.core.exception.GameNotStartableException
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class NimService {

    companion object : KLogging() {
        private const val START_STICKS = 13
        private const val END_STICKS = 0
    }


    private var gameStatus: GameStatus = NOT_STARTED
    private var leftSticks: Int = START_STICKS
    private var nextPlayer: Player? = null
    private var winner: Player? = null


    fun startGame(): GameInformation {
        val allowedStatus = listOf(NOT_STARTED, ENDED)
        if (!allowedStatus.contains(gameStatus))
            throw GameNotStartableException("Cannot start game. Game can only be started if gameStatus is in $allowedStatus ")

        initializeGame()

        return getGameInformation()
    }


    fun reStartGame(): GameInformation {
        if (gameStatus != RUNNING)
            throw GameNotRestartableException("Cannot restart game. Game has to have gameStatus=$RUNNING to be restarted.")

        initializeGame()

        return getGameInformation()
    }


    private fun initializeGame() {
        gameStatus = RUNNING
        leftSticks = START_STICKS
        nextPlayer = determineRandomPlayer()
    }


    private fun determineRandomPlayer(): Player = Player.values().toList().shuffled().first()


    private fun terminateGame() {
        gameStatus = ENDED
        leftSticks = END_STICKS
        nextPlayer = null
        winner = null
    }


    fun endGame(): GameInformation {
        if (gameStatus != RUNNING)
            throw GameNotEndableException("Cannot end game. Game has to have gameStatus=$RUNNING to be ended.")

        terminateGame()

        return getGameInformation()
    }


    fun getGameInformation() = GameInformation(
            gameStatus = gameStatus,
            leftSticks = leftSticks,
            nextPlayer = nextPlayer,
            winner = winner
    )

}