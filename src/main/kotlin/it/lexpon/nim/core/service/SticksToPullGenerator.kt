package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.ComputerStrategy
import it.lexpon.nim.core.domainobject.ComputerStrategy.PULL_RANDOM
import it.lexpon.nim.core.domainobject.ComputerStrategy.PULL_TO_WIN
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SticksToPullGenerator(
        @Value("\${computer.strategy:PULL_RANDOM}") val computerStrategy: String
) {

    companion object : KLogging() {
        private val FALLBACK_STRATEGY = PULL_RANDOM
    }

    fun getSticksToPullForComputer(game: NimGame): Int {
        val strategy: ComputerStrategy = try {
            ComputerStrategy.valueOf(computerStrategy)
        } catch (e: IllegalArgumentException) {
            logger.error(e) { "Could not map $computerStrategy to an enum value in ${ComputerStrategy.values().toList()}. Will use $FALLBACK_STRATEGY as fallback strategy" }
            FALLBACK_STRATEGY
        }

        return when (strategy) {
            PULL_RANDOM -> getRandomSticks(game)
            PULL_TO_WIN -> getSticksToWin(game)
        }

    }

    private fun getRandomSticks(game: NimGame): Int = game.getPossibleSticksToPull().shuffled().first()

    private fun getSticksToWin(game: NimGame): Int {
        val leftSticks = game.getGameInfo().leftSticks
        val possibleSticksToPull = game.getPossibleSticksToPull()

        val maxPlus1Sticks = possibleSticksToPull.max()!! + 1

        // less than 5 left -> goal: only 1 stick remains to be pulled by the loser
        if (leftSticks in 2..maxPlus1Sticks)
            return leftSticks - possibleSticksToPull.min()!!

        // only 1 stick left. it has to be pulled
        if (leftSticks == 1)
            return 1

        // goal: remaining sticks should be in (5, 9 , 13). so it is [remainingSticks] mod [(max possible sticks to pull) + 1 = 4] = 1 => remainingSticks mod 4 = 1
        for (stickToPull in possibleSticksToPull) {
            val newLeftSticks = leftSticks - stickToPull
            if (newLeftSticks % maxPlus1Sticks == 1)
                return stickToPull
        }

        // not possible to win by strategy
        // to have the maximum chance just pull 1 and hope that the opponent doesn't know anything about a winning strategy
        return 1
    }

}
