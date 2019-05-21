package it.lexpon.nim.core.service

import com.nhaarman.mockito_kotlin.whenever
import it.lexpon.nim.core.domainobject.GameEventType.COMPUTER_MOVE
import it.lexpon.nim.core.domainobject.GameEventType.START
import it.lexpon.nim.core.domainobject.Player.COMPUTER
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.exception.GameNotStartableException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class NimGameHandlerTest {

    @Mock
    private lateinit var randomPlayerGenerator: RandomPlayerGenerator

    @InjectMocks
    private lateinit var testee: NimGameHandler

    @Test
    fun `should start game`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)

        // WHEN
        val info = testee.startGame()

        // THEN
        assertThat(info).isNotNull
        assertThat(info.gameEvents).isNotNull
        assertThat(info.gameEvents.size).isEqualTo(1)
        assertThat(info.gameEvents[0].gameEventType).isEqualTo(START)
    }

    @Test
    fun `should start game and make a computer move`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(COMPUTER)

        // WHEN
        val info = testee.startGame()

        // THEN
        assertThat(info).isNotNull
        assertThat(info.gameEvents).isNotNull
        assertThat(info.gameEvents.size).isEqualTo(2)
        assertThat(info.gameEvents[0].gameEventType).isEqualTo(START)
        assertThat(info.gameEvents[1].gameEventType).isEqualTo(COMPUTER_MOVE)
    }

    @Test
    fun `should not start a game - game is running already`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(COMPUTER)
        testee.startGame()

        // THEN
        assertThrows<GameNotStartableException> {
            // WHEN
            testee.startGame()
        }
    }

    @Test
    fun `should restart a running game`() {

    }

    @Test
    fun `should restart a running game and make a computer move`() {

    }

    @Test
    fun `should not restart a game - no game started yet`() {

    }

    @Test
    fun `should not restart a game - game ended already`() {

    }

    @Test
    fun `should end a running game`() {

    }

    @Test
    fun `should not end a game - game not started yet`() {

    }

    @Test
    fun `should not end a game - game ended already`() {

    }

    @Test
    fun `should make move - human and computer`() {

    }

    @Test
    fun `should make move - human and computer - human wins`() {

    }

    @Test
    fun `should make move - human - computer wins`() {

    }

}