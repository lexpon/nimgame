package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.*
import it.lexpon.nim.core.domainobject.GameState.*
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

    private var gameState: GameState = NOT_STARTED
    private var leftSticks: Int = STICKS_START
    private var currentPlayer: Player? = null
    private var winner: Player? = null

    fun getGameInformation() = GameInformation(
            gameState = gameState,
            leftSticks = leftSticks,
            winner = winner
    )


    fun startGame(): MoveInformation {
        val allowedStatus = listOf(NOT_STARTED, ENDED)
        if (!allowedStatus.contains(gameState))
            throw GameNotStartableException("Cannot start game. Game can only be started if gameState is in $allowedStatus ")

        val events = mutableListOf<GameEvent>()

        initializeGame()
        events.add(Start("Game started"))

        if (currentPlayer == COMPUTER) {
            val sticksToPull = getSticksToPullForComputer()
            pullSticks(sticksToPull)
            events.add(ComputerMove(sticksToPull))
            currentPlayer = HUMAN
        }

        return MoveInformation(EventList(events))
    }


    fun reStartGame(): MoveInformation {
        if (gameState != RUNNING)
            throw GameNotRestartableException("Cannot restart game. Game has to have gameState=$RUNNING to be restarted.")

        val events = mutableListOf<GameEvent>()

        initializeGame()
        events.add(Restart("Game restarted"))

        if (currentPlayer == COMPUTER) {
            val sticksToPull = getSticksToPullForComputer()
            pullSticks(sticksToPull)
            events.add(ComputerMove(sticksToPull))
            currentPlayer = HUMAN
        }

        return MoveInformation(EventList(events))
    }


    private fun initializeGame() {
        gameState = RUNNING
        leftSticks = STICKS_START
        currentPlayer = determineRandomPlayer()
    }

    private fun determineRandomPlayer(): Player = Player.values().toList().shuffled().first()

    fun endGame(): MoveInformation {
        if (gameState != RUNNING)
            throw GameNotEndableException("Cannot end game. Game has to have gameState=$RUNNING to be ended.")

        terminateGame()

        return MoveInformation(EventList(listOf(End("Game ended"))))
    }


    fun makeMove(sticksToPullByHuman: Int): MoveInformation {
        if (gameState != RUNNING)
            throw MoveNotPossibleException("Wrong gameState=$gameState. Pulling sticks only possible when gameState=$RUNNING")

        if (currentPlayer != HUMAN)
            throw MoveNotPossibleException("Current player has to be $HUMAN, but is $currentPlayer")

        val events = mutableListOf<GameEvent>()

        pullSticks(sticksToPullByHuman)
        events.add(HumanMove(sticksToPullByHuman))
        if (hasPlayerLost()) {
            terminateGame(winningPlayer = COMPUTER)
            events.add(End("Game ended. Computer won."))
        } else {
            val sticksToPullByComputer: Int = getSticksToPullForComputer()
            pullSticks(sticksToPullByComputer)
            events.add(ComputerMove(sticksToPullByComputer))
            if (hasPlayerLost()) {
                terminateGame(winningPlayer = HUMAN)
                events.add(End("Game ended. Human won."))
            }
        }

        return MoveInformation(EventList(events))
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
        gameState = ENDED
        leftSticks = STICKS_END
        currentPlayer = null
        winner = winningPlayer
    }

}