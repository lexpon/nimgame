package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.*
import it.lexpon.nim.core.domainobject.GameState.*
import it.lexpon.nim.core.domainobject.Player.COMPUTER
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.exception.NoGameException
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class NimService {

    companion object : KLogging()

    private var game: NimGame? = null

    fun getGameInformation() =
            game?.let { GameInformation(it.getNimGameInformation()) }
                    ?: GameInformation(state = NOT_STARTED)

    fun startGame(): MoveInformation {
        game = NimGame.startGame(determineRandomPlayer())

        val events = mutableListOf<GameEvent>()
        events.add(Start("Game started"))
        makeComputerMoveIfNecessary(game!!, events)

        return MoveInformation(GameEventList(events))
    }

    private fun determineRandomPlayer(): Player = Player.values().toList().shuffled().first()

    private fun makeComputerMoveIfNecessary(game: NimGame, events: MutableList<GameEvent>) =
            game.let {
                val info = it.getNimGameInformation()
                if (info.state == RUNNING && info.currentPlayer == COMPUTER) {
                    val sticksToPull = getSticksToPullForComputer()
                    it.pullSticks(sticksToPull)
                    events.add(ComputerMove(sticksToPull))
                }
            }

    private fun makeHumanMoveIfNecessary(game: NimGame, sticksToPull: Int, events: MutableList<GameEvent>) =
            game.let {
                val info = it.getNimGameInformation()
                if (info.state == RUNNING && info.currentPlayer == HUMAN) {
                    it.pullSticks(sticksToPull)
                    events.add(HumanMove(sticksToPull))
                }
            }

    fun reStartGame(): MoveInformation =
            game?.let {
                it.restartGame(determineRandomPlayer())
                val events = mutableListOf<GameEvent>()
                events.add(Restart("Game restarted"))
                makeComputerMoveIfNecessary(it, events)
                return MoveInformation(GameEventList(events))
            } ?: run {
                throw NoGameException("Game has not been started yet. Not possible not restart it.")
            }

    fun endGame(): MoveInformation =
            game?.let {
                it.endGame()
                return MoveInformation(GameEventList(listOf(End("Game ended"))))
            } ?: run {
                throw NoGameException("Game has not been started yet. Not possible not end it.")
            }

    fun makeMove(sticksToPullByHuman: Int): MoveInformation =
            game?.let {
                val events = mutableListOf<GameEvent>()
                makeHumanMoveIfNecessary(it, sticksToPullByHuman, events)
                makeComputerMoveIfNecessary(it, events)

                val info = it.getNimGameInformation()
                if (info.state == ENDED)
                    events.add(End("Game Ended. Winner is ${info.winner}"))

                return MoveInformation(GameEventList(events))
            } ?: run {
                throw NoGameException("Game has not been started yet. Not possible to make a move.")
            }

    private fun getSticksToPullForComputer(): Int =
            game?.getPossibleSticksToPull()?.shuffled()?.first()
                    ?: run { throw NoGameException("Game has not been started yet. Not possible to determine sticks to pull.") }

}