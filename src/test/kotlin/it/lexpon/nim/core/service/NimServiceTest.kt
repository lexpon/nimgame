package it.lexpon.nim.core.service

import it.lexpon.nim.core.exception.GameNotRestartableException
import it.lexpon.nim.core.exception.GameNotStartableException
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class NimServiceTest {

    private var testee: NimService = NimService()

    @Before
    fun init() {
        testee = NimService()
    }

    @Test
    fun `should start game - not started yet`() {
        // WHEN
        val gameInformation = testee.startGame()

        // THEN
//        assertThat(gameInformation.gameState).isEqualTo(RUNNING)
//        assertThat(gameInformation.leftSticks).isEqualTo(13)
//        assertThat(gameInformation.winner).isNull()
    }

    @Test
    fun `should start game - game ended before`() {
        // GIVEN
        testee.startGame()
        testee.endGame()

        // WHEN
        val gameInformation = testee.startGame()

        // THEN
//        assertThat(gameInformation.gameState).isEqualTo(RUNNING)
//        assertThat(gameInformation.leftSticks).isEqualTo(13)
//        assertThat(gameInformation.winner).isNull()
    }

    @Test
    fun `should not start game if it has been started already`() {
        // GIVEN
        // ... game has been started already
        testee.startGame()

        // THEN
        assertThrows<GameNotStartableException> {
            // WHEN
            testee.startGame()
        }
    }

    @Test
    fun `should restart a running game`() {
        // GIVEN
        testee.startGame()

        // WHEN
        val gameInformation = testee.reStartGame()

        // THEN
//        assertThat(gameInformation.gameState).isEqualTo(RUNNING)
//        assertThat(gameInformation.leftSticks).isEqualTo(13)
//        assertThat(gameInformation.winner).isNull()
    }

    @Test
    fun `should not restart game - game not started yet`() {
        // THEN
        assertThrows<GameNotRestartableException> {
            // WHEN
            testee.reStartGame()
        }
    }

    @Test
    fun `should not restart game - game ended`() {
        // GIVEN
        testee.startGame()
        testee.endGame()

        // THEN
        assertThrows<GameNotRestartableException> {
            // WHEN
            testee.reStartGame()
        }
    }

}