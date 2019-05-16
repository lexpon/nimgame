package it.lexpon.nim.core.service

import it.lexpon.nim.core.domainobject.GameStatus.RUNNING
import it.lexpon.nim.core.domainobject.Player
import it.lexpon.nim.core.exception.GameNotStartableException
import org.assertj.core.api.Assertions.assertThat
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
    fun `should start game`() {
        // WHEN
        val gameInformation = testee.startGame()

        // THEN
        assertThat(gameInformation.gameStatus).isEqualTo(RUNNING)
        assertThat(gameInformation.leftSticks).isEqualTo(13)
        assertThat(gameInformation.nextPlayer).isIn(Player.values().toList())
        assertThat(gameInformation.winner).isNull()
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

}