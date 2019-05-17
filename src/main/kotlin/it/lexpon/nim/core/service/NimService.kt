package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameInformation
import it.lexpon.nim.core.domainobject.GameStatus
import it.lexpon.nim.core.domainobject.GameStatus.*
import it.lexpon.nim.core.domainobject.MoveInformation
import it.lexpon.nim.core.domainobject.Player
import it.lexpon.nim.core.domainobject.Player.COMPUTER
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.exception.*
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class NimService {

    companion object : KLogging() {
        private const val STICKS_START = 13
        private const val STICKS_END = 0
        private val STICKS_TO_PULL_POSSIBLE = listOf(1, 2, 3)
    }


    private var gameStatus: GameStatus = NOT_STARTED
    private var leftSticks: Int = STICKS_START
    private var currentPlayer: Player? = null
    private var winner: Player? = null
    private var moveInformation: MoveInformation? = null

    fun getGameInformation() = GameInformation(
            gameStatus = gameStatus,
            leftSticks = leftSticks,
            winner = winner,
            moveInformation = moveInformation
    )


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
        leftSticks = STICKS_START
        currentPlayer = determineRandomPlayer()
    }

    private fun determineRandomPlayer(): Player = Player.values().toList().shuffled().first()

    fun endGame(): GameInformation {
        if (gameStatus != RUNNING)
            throw GameNotEndableException("Cannot end game. Game has to have gameStatus=$RUNNING to be ended.")

        terminateGame()

        return getGameInformation()
    }


    fun makeMove(sticksToPullByHuman: Int): GameInformation {

        if (currentPlayer != HUMAN)
            throw MoveNotPossibleException("Current player has to be $HUMAN, but is $currentPlayer")

        val moveInformationBuilder = MoveInformation.Builder()

        pullSticks(sticksToPullByHuman)
        moveInformationBuilder.pulledSticksByHuman(sticksToPullByHuman)
        if (hasPlayerLost()) {
            terminateGame(winningPlayer = COMPUTER)
        } else {
            val sticksToPullByComputer: Int = getSticksToPullForComputer()
            pullSticks(sticksToPullByComputer)
            moveInformationBuilder.pulledSticksByComputer(sticksToPullByComputer)
            if (hasPlayerLost()) {
                terminateGame(winningPlayer = HUMAN)
            }
        }

        moveInformation = moveInformationBuilder.build()

        return getGameInformation()
    }

    private fun makeMoveByComputer() {
        val sticksToPull: Int = getSticksToPullForComputer()
        pullSticks(sticksToPull)
        if (hasPlayerLost())
            terminateGame(winningPlayer = HUMAN)
    }

    private fun getSticksToPullForComputer(): Int = getPossibleSticksToPull().shuffled().first()


    private fun pullSticks(sticksToPull: Int) {
        val allowedNumberOfSticks = getPossibleSticksToPull()
        if (!allowedNumberOfSticks.contains(sticksToPull))
            throw SticksToPullException("Not possible to pull $sticksToPull sticks. Number of sticks has to be in $allowedNumberOfSticks")
        leftSticks = leftSticks - sticksToPull
    }


    private fun getPossibleSticksToPull(): List<Int> {
        if (leftSticks >= STICKS_TO_PULL_POSSIBLE.max()!!)
            return STICKS_TO_PULL_POSSIBLE

        val min = STICKS_TO_PULL_POSSIBLE.min()!!
        val max = leftSticks
        return IntRange(min, max).toList()
    }


    private fun hasPlayerLost(): Boolean = leftSticks == 0


    private fun terminateGame(winningPlayer: Player? = null) {
        gameStatus = ENDED
        leftSticks = STICKS_END
        currentPlayer = null
        winner = winningPlayer
    }

}