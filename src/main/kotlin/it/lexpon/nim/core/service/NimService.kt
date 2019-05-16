package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameInformation
import it.lexpon.nim.core.domainobject.GameStatus
import it.lexpon.nim.core.domainobject.GameStatus.NOT_STARTED
import it.lexpon.nim.core.domainobject.GameStatus.RUNNING
import it.lexpon.nim.core.domainobject.Player
import it.lexpon.nim.core.exception.GameNotStartableException
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class NimService {

    companion object : KLogging() {
        private const val START_STICKS = 13
    }

    private var gameStatus: GameStatus = NOT_STARTED
    private var leftSticks: Int = START_STICKS
    private var nextPlayer: Player? = null
    private var winner: Player? = null


    fun startGame(): GameInformation {
        if (gameStatus != NOT_STARTED)
            throw GameNotStartableException("Cannot start game. Game can only be started if gameStatus=$NOT_STARTED")

        gameStatus = RUNNING
        leftSticks = START_STICKS
        nextPlayer = determineRandomPlayer()

        return getGameInformation()
    }


    private fun determineRandomPlayer(): Player = Player.values().toList().shuffled().first()


    fun getGameInformation() = GameInformation(
            gameStatus = gameStatus,
            leftSticks = leftSticks,
            nextPlayer = nextPlayer,
            winner = winner
    )

}