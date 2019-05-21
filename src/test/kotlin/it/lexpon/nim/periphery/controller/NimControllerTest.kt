package it.lexpon.nim.periphery.controller

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class NimControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should get game status`() {
        // GIVEN
        mockMvc.perform(post("/api/v1/start"))

        // WHEN
        val result = mockMvc.perform(get("/api/v1/status"))

        // THEN
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.id").isNumber)
                .andExpect(jsonPath("$.gameState").value("RUNNING"))
                .andExpect(jsonPath("$.leftSticks").isNumber)
                .andExpect(jsonPath("$.winner").doesNotExist())
    }

    @Test
    fun `should start a game`() {
        // WHEN
        val result = mockMvc.perform(post("/api/v1/start"))

        // THEN
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.id").isNumber)
                .andExpect(jsonPath("$.gameState").value("RUNNING"))
                .andExpect(jsonPath("$.leftSticks").isNumber)
                .andExpect(jsonPath("$.gameEvents").isArray)
                .andExpect(jsonPath("$.winner").doesNotExist())
    }

    @Test
    fun `should end a game`() {
        // GIVEN
        mockMvc.perform(post("/api/v1/start"))

        // WHEN
        val result = mockMvc.perform(post("/api/v1/end"))

        // THEN
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.id").isNumber)
                .andExpect(jsonPath("$.gameState").value("ENDED"))
                .andExpect(jsonPath("$.leftSticks").isNumber)
                .andExpect(jsonPath("$.gameEvents").isArray)
                .andExpect(jsonPath("$.winner").doesNotExist())
    }

    @Test
    fun `should restart a game`() {
        // GIVEN
        mockMvc.perform(post("/api/v1/start"))

        // WHEN
        val result = mockMvc.perform(post("/api/v1/restart"))

        // THEN
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.id").isNumber)
                .andExpect(jsonPath("$.gameState").value("RUNNING"))
                .andExpect(jsonPath("$.leftSticks").isNumber)
                .andExpect(jsonPath("$.gameEvents").isArray)
                .andExpect(jsonPath("$.winner").doesNotExist())
    }

    @Test
    fun `should make a move`() {
        // GIVEN
        mockMvc.perform(post("/api/v1/start"))


        // WHEN
        val result = mockMvc.perform(
                post("/api/v1/pullsticks")
                        .param("sticksToPull", "2")
        )

        // THEN
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.id").isNumber)
                .andExpect(jsonPath("$.gameState").value("RUNNING"))
                .andExpect(jsonPath("$.leftSticks").isNumber)
                .andExpect(jsonPath("$.gameEvents").isArray)
                .andExpect(jsonPath("$.winner").doesNotExist())
    }

    @Test
    fun `should get history after game has ended`() {
        // GIVEN
        mockMvc.perform(post("/api/v1/start"))
        mockMvc.perform(post("/api/v1/end"))


        // WHEN
        val result = mockMvc.perform(get("/api/v1/history"))

        // THEN
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.gameHistory").isArray)
                .andExpect(jsonPath("$.gameHistory[0].id").isNumber)
                .andExpect(jsonPath("$.gameHistory[0].gameState").value("ENDED"))
                .andExpect(jsonPath("$.gameHistory[0].leftSticks").isNumber)
                .andExpect(jsonPath("$.gameHistory[0].winner").doesNotExist())
    }
}