package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameState.ENDED
import it.lexpon.nim.core.domainobject.GameState.RUNNING
import it.lexpon.nim.core.domainobject.Player
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
        val game = NimGame.startGame()

        // WHEN
        val info = game.getNimGameInformation()

        // THEN
        assertThat(info).isNotNull
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.leftSticks).isEqualTo(13)
        assertThat(info.currentPlayer).isIn(Player.values().toList())
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should end a running game`() {
        // GIVEN
        val game = NimGame.startGame()

        // WHEN
        game.endGame()

        // THEN
        val info = game.getNimGameInformation()
        assertThat(info).isNotNull
        assertThat(info.state).isEqualTo(ENDED)
    }

    @Test
    fun `should not end an already ended game`() {
        // GIVEN
        val game = NimGame.startGame()
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
        val game = NimGame.startGame()
        game.pullSticks(1)

        // WHEN
        game.restartGame()

        // THEN
        val info = game.getNimGameInformation()
        assertThat(info).isNotNull
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.leftSticks).isEqualTo(13)
        assertThat(info.currentPlayer).isIn(Player.values().toList())
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should not restart an already ended game`() {
        // GIVEN
        val game = NimGame.startGame()
        game.endGame()

        // THEN
        assertThrows<GameNotRestartableException> {
            // WHEN
            game.restartGame()
        }
    }

    @Test
    fun `should only pull allowed number of sticks`() {
        // GIVEN
        val game = NimGame.startGame()

        // WHEN
        for (sticks in 1..3)
            game.pullSticks(sticks)

        // THEN
        val info = game.getNimGameInformation()
        assertThat(info.leftSticks).isEqualTo(7)
    }

    @Test
    fun `should not pull more than allowed number of sticks`() {
        // GIVEN
        val game = NimGame.startGame()

        // THEN
        assertThrows<SticksToPullException> {
            // WHEN
            game.pullSticks(1337)
        }
    }

}