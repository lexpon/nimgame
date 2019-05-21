package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.*
import it.lexpon.nim.core.domainobject.GameState.ENDED
import it.lexpon.nim.core.domainobject.GameState.RUNNING
import it.lexpon.nim.core.domainobject.Player.COMPUTER
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.exception.GameNotStartableException
import it.lexpon.nim.core.exception.MoveNotPossibleException
import it.lexpon.nim.core.exception.NoGameException
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class NimGameHandler(
        private val randomPlayerGenerator: RandomPlayerGenerator,
        private val sticksToPullGenerator: SticksToPullGenerator,
        private val nimGameIdGenerator: NimGameIdGenerator
) {

    companion object : KLogging()

    private var game: NimGame? = null
    private val gameHistory: MutableList<NimGame> = mutableListOf()

    fun getGameInfo() =
            game?.getGameInfo()
                    ?: run { throw NoGameException("Game has not been started. Not possible to get game info.") }

    fun getGameHistory(): GameHistory = GameHistory(gameHistory.map { it.getGameInfo() })

    fun startGame(): GameEventInfo {
        if (game != null && game!!.getGameInfo().state != ENDED)
            throw GameNotStartableException("There is a game running already. Starting a game only possible when it has not been started yet or if it is ended")

        game = NimGame.startGame(
                gameId = nimGameIdGenerator.getNewId(),
                firstPlayer = randomPlayerGenerator.determineRandomPlayer()
        )

        val events = mutableListOf<GameEvent>()
        events.add(Start("Game started"))
        makeComputerMoveIfNecessary(game!!, events)

        return GameEventInfo(events, game!!.getGameInfo())
    }

    private fun makeComputerMoveIfNecessary(game: NimGame, events: MutableList<GameEvent>) {
        val info = game.getGameInfo()
        if (info.state == RUNNING && info.currentPlayer == COMPUTER) {
            val sticksToPull = getSticksToPullForComputer()
            game.pullSticks(sticksToPull)
            events.add(ComputerMove(sticksToPull))
        }
    }

    private fun makeHumanMoveIfNecessary(game: NimGame, sticksToPull: Int, events: MutableList<GameEvent>) {
        val info = game.getGameInfo()
        if (info.state == RUNNING && info.currentPlayer == HUMAN) {
            game.pullSticks(sticksToPull)
            events.add(HumanMove(sticksToPull))
        }
    }

    fun reStartGame(): GameEventInfo =
            game?.let {
                val firstPlayer = randomPlayerGenerator.determineRandomPlayer()
                it.restartGame(firstPlayer)
                val events = mutableListOf<GameEvent>()
                events.add(Restart("Game restarted"))
                makeComputerMoveIfNecessary(it, events)
                return GameEventInfo(events, it.getGameInfo())
            } ?: run {
                throw NoGameException("Game has not been started yet. Not possible not restart it.")
            }

    fun endGame(): GameEventInfo =
            game?.let {
                it.endGame()
                gameHistory.add(it)
                return GameEventInfo(End("Game ended"), it.getGameInfo())
            } ?: run {
                throw NoGameException("Game has not been started yet. Not possible not end it.")
            }

    fun makeMove(sticksToPullByHuman: Int): GameEventInfo =
            game?.let {
                val currentState = it.getGameInfo().state
                if (currentState != RUNNING)
                    throw MoveNotPossibleException("Making moves is only possible if game has state=$RUNNING. Current state=$currentState")

                val events = mutableListOf<GameEvent>()
                makeHumanMoveIfNecessary(it, sticksToPullByHuman, events)
                makeComputerMoveIfNecessary(it, events)

                val info = it.getGameInfo()
                if (info.state == ENDED) {
                    gameHistory.add(it)
                    events.add(End("Game Ended. Winner is ${info.winner}"))
                }

                return GameEventInfo(events, it.getGameInfo())
            } ?: run {
                throw NoGameException("Game has not been started yet. Not possible to make a move.")
            }

    private fun getSticksToPullForComputer(): Int =
            game?.let { sticksToPullGenerator.getSticksToPullForComputer(it) }
                    ?: run { throw NoGameException("Game has not been started yet. Not possible to determine sticks to pull.") }

}
