package it.lexpon.nim.core.service

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import it.lexpon.nim.core.domainobject.GameEventType.*
import it.lexpon.nim.core.domainobject.GameState.ENDED
import it.lexpon.nim.core.domainobject.GameState.RUNNING
import it.lexpon.nim.core.domainobject.Player.COMPUTER
import it.lexpon.nim.core.domainobject.Player.HUMAN
import it.lexpon.nim.core.exception.GameNotEndableException
import it.lexpon.nim.core.exception.GameNotRestartableException
import it.lexpon.nim.core.exception.GameNotStartableException
import it.lexpon.nim.core.exception.NoGameException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
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
    @Mock
    private lateinit var sticksToPullGenerator: SticksToPullGenerator

    @InjectMocks
    private lateinit var testee: NimGameHandler

    @Before
    fun initSticksToPullGenerator() {
        whenever(sticksToPullGenerator.getSticksToPullForComputer(any())).thenReturn(1)
    }

    @Test
    fun `should start game`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)

        // WHEN
        val eventInfo = testee.startGame()
        val info = testee.getGameInfo()

        // THEN
        assertThat(eventInfo).isNotNull
        assertThat(eventInfo.gameEvents).isNotNull
        assertThat(eventInfo.gameEvents.size).isEqualTo(1)
        assertThat(eventInfo.gameEvents[0].gameEventType).isEqualTo(START)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.leftSticks).isEqualTo(13)
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should start game and make a computer move`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(COMPUTER)

        // WHEN
        val eventInfo = testee.startGame()
        val info = testee.getGameInfo()

        // THEN
        assertThat(eventInfo).isNotNull
        assertThat(eventInfo.gameEvents).isNotNull
        assertThat(eventInfo.gameEvents.size).isEqualTo(2)
        assertThat(eventInfo.gameEvents[0].gameEventType).isEqualTo(START)
        assertThat(eventInfo.gameEvents[1].gameEventType).isEqualTo(COMPUTER_MOVE)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.leftSticks).isEqualTo(12)
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.winner).isNull()
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
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)
        testee.startGame()

        // WHEN
        val eventInfo = testee.reStartGame()
        val info = testee.getGameInfo()

        // THEN
        assertThat(eventInfo).isNotNull
        assertThat(eventInfo.gameEvents).isNotNull
        assertThat(eventInfo.gameEvents.size).isEqualTo(1)
        assertThat(eventInfo.gameEvents[0].gameEventType).isEqualTo(RESTART)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.leftSticks).isEqualTo(13)
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should restart a running game and make a computer move`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(COMPUTER)
        testee.startGame()

        // WHEN
        val gameEventInfo = testee.reStartGame()
        val info = testee.getGameInfo()

        // THEN
        assertThat(gameEventInfo).isNotNull
        assertThat(gameEventInfo.gameEvents).isNotNull
        assertThat(gameEventInfo.gameEvents.size).isEqualTo(2)
        assertThat(gameEventInfo.gameEvents[0].gameEventType).isEqualTo(RESTART)
        assertThat(gameEventInfo.gameEvents[1].gameEventType).isEqualTo(COMPUTER_MOVE)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.leftSticks).isEqualTo(12)
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should not restart a game - no game started yet`() {
        // THEN
        assertThrows<NoGameException> {
            // WHEN
            testee.reStartGame()
        }
    }

    @Test
    fun `should not restart a game - game ended already`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)
        testee.startGame()
        testee.endGame()

        // THEN
        assertThrows<GameNotRestartableException> {
            // WHEN
            testee.reStartGame()
        }
    }

    @Test
    fun `should end a running game`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)
        testee.startGame()

        // WHEN
        val eventInfo = testee.endGame()
        val info = testee.getGameInfo()

        // THEN
        assertThat(eventInfo).isNotNull
        assertThat(eventInfo.gameEvents).isNotNull
        assertThat(eventInfo.gameEvents.size).isEqualTo(1)
        assertThat(eventInfo.gameEvents[0].gameEventType).isEqualTo(END)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.leftSticks).isEqualTo(13)
        assertThat(info.state).isEqualTo(ENDED)
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should not end a game - game not started yet`() {
        // THEN
        assertThrows<NoGameException> {
            // WHEN
            testee.endGame()
        }
    }

    @Test
    fun `should not end a game - game ended already`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)
        testee.startGame()
        testee.endGame()

        // THEN
        assertThrows<GameNotEndableException> {
            // WHEN
            testee.endGame()
        }
    }

    @Test
    fun `should make move - human and computer`() {
        // GIVEN
        val sticksToPullByHuman = 2
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)
        testee.startGame()

        // WHEN
        val eventInfo = testee.makeMove(sticksToPullByHuman)
        val info = testee.getGameInfo()

        // THEN
        assertThat(eventInfo).isNotNull
        assertThat(eventInfo.gameEvents).isNotNull
        assertThat(eventInfo.gameEvents.size).isEqualTo(2)
        assertThat(eventInfo.gameEvents[0].gameEventType).isEqualTo(HUMAN_MOVE)
        assertThat(eventInfo.gameEvents[1].gameEventType).isEqualTo(COMPUTER_MOVE)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.leftSticks).isEqualTo(10)
        assertThat(info.state).isEqualTo(RUNNING)
        assertThat(info.winner).isNull()
    }

    @Test
    fun `should make move - human and computer - human wins`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)
        testee.startGame()
        testee.makeMove(3)
        testee.makeMove(3)
        testee.makeMove(2)

        // WHEN
        val eventInfo = testee.makeMove(1)
        val info = testee.getGameInfo()

        // THEN
        assertThat(eventInfo).isNotNull
        assertThat(eventInfo.gameEvents).isNotNull
        assertThat(eventInfo.gameEvents.size).isEqualTo(3)
        assertThat(eventInfo.gameEvents[0].gameEventType).isEqualTo(HUMAN_MOVE)
        assertThat(eventInfo.gameEvents[1].gameEventType).isEqualTo(COMPUTER_MOVE)
        assertThat(eventInfo.gameEvents[2].gameEventType).isEqualTo(END)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(COMPUTER)
        assertThat(info.leftSticks).isEqualTo(0)
        assertThat(info.state).isEqualTo(ENDED)
        assertThat(info.winner).isEqualTo(HUMAN)
    }

    @Test
    fun `should make move - human - computer wins`() {
        // GIVEN
        whenever(randomPlayerGenerator.determineRandomPlayer()).thenReturn(HUMAN)
        testee.startGame()
        testee.makeMove(3)
        testee.makeMove(3)
        testee.makeMove(3)

        // WHEN
        val eventInfo = testee.makeMove(1)
        val info = testee.getGameInfo()

        // THEN
        assertThat(eventInfo).isNotNull
        assertThat(eventInfo.gameEvents).isNotNull
        assertThat(eventInfo.gameEvents.size).isEqualTo(2)
        assertThat(eventInfo.gameEvents[0].gameEventType).isEqualTo(HUMAN_MOVE)
        assertThat(eventInfo.gameEvents[1].gameEventType).isEqualTo(END)

        assertThat(info).isNotNull
        assertThat(info.id).isEqualTo(1)
        assertThat(info.currentPlayer).isEqualTo(HUMAN)
        assertThat(info.leftSticks).isEqualTo(0)
        assertThat(info.state).isEqualTo(ENDED)
        assertThat(info.winner).isEqualTo(COMPUTER)
    }

}