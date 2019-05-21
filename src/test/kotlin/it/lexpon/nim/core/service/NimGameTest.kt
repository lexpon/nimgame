package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameState.ENDED
import it.lexpon.nim.core.domainobject.GameState.RUNNING
import it.lexpon.nim.core.domainobject.Player.*
import it.lexpon.nim.core.exception.GameNotEndableException
import it.lexpon.nim.core.exception.GameNotRestartableException
import it.lexpon.nim.core.exception.SticksToPullException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class NimGameTest {

    @Test
    fun `should start new game`() {
        // given
        val game = NimGame.startGame(1, HUMAN)

        // WHEN
        val info = game.getGameInfo()

        // THEN
        assertThat(info).isNotNull
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.leftSticks).isEqualTo(13)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should end a running game`() {
        // GIVEN
        val game = NimGame.startGame(1, COMPUTER)

        // WHEN
        game.endGame()

        // THEN
        val info = game.getGameInfo()
        assertThat(info).isNotNull
        assertThat(info.state).isEqualTo(ENDED)
    }

    @Test
    fun `should win a game - human`() {
        // GIVEN
        val game = NimGame.startGame(1, HUMAN)
        game.pullSticks(3) // HUMAN
        game.pullSticks(3) // COMPUTER
        game.pullSticks(3) // HUMAN
        game.pullSticks(2) // COMPUTER
        game.pullSticks(1) // HUMAN

        // WHEN
        game.pullSticks(1) // COMPUTER


        // THEN
        val info = game.getGameInfo()
        assertThat(info).isNotNull
        assertThat(info.state).isEqualTo(ENDED)
        assertThat(info.leftSticks).isEqualTo(0)
        assertThat(info.currentPlayer).isEqualTo(COMPUTER)
        assertThat(info.winner).isEqualTo(HUMAN)
    }

    @Test
    fun `should not end an already ended game`() {
        // GIVEN
        val game = NimGame.startGame(1, HUMAN)
        game.endGame()

        // THEN
        assertThrows<GameNotEndableException> {
            // WHEN
            game.endGame()
        }
    }

    @Test
    fun `should restart a running game`() {
        // GIVEN
        val game = NimGame.startGame(1, HUMAN)
        game.pullSticks(1)

        // WHEN
        game.restartGame(HUMAN)

        // THEN
        val info = game.getGameInfo()
        assertThat(info).isNotNull
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.leftSticks).isEqualTo(13)
        assertThat(info.currentPlayer).isIn(values().toList())
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should not restart an already ended game`() {
        // GIVEN
        val game = NimGame.startGame(1, HUMAN)
        game.endGame()

        // THEN
        assertThrows<GameNotRestartableException> {
            // WHEN
            game.restartGame(COMPUTER)
        }
    }

    @Test
    fun `should only pull allowed number of sticks`() {
        // GIVEN
        val game = NimGame.startGame(1, COMPUTER)

        // WHEN
        for (sticks in 1..3)
            game.pullSticks(sticks)

        // THEN
        val info = game.getGameInfo()
        assertThat(info.leftSticks).isEqualTo(7)
    }

    @Test
    fun `should not pull more than allowed number of sticks`() {
        // GIVEN
        val game = NimGame.startGame(1, HUMAN)

        // THEN
        assertThrows<SticksToPullException> {
            // WHEN
            game.pullSticks(1337)
        }
    }

}