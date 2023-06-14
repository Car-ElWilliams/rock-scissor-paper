package com.carelwilliams.rockpaperscissors.controller

// kt lint-disable no-wildcard-imports
import com.carelwilliams.rockpaperscissors.enums.RockPaperScissor
import com.carelwilliams.rockpaperscissors.model.GamePlaySession
import com.carelwilliams.rockpaperscissors.model.PostGamePlaySession
import com.carelwilliams.rockpaperscissors.model.UpdateGamePlaySession
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class PlayControllerTest
@Autowired
constructor(val mockMvc: MockMvc, val objectMapper: ObjectMapper) {
    val baseUrl = "/api/session"

    fun addNewGameBeforeTest(userId: String) {

        val SESSION: PostGamePlaySession =
            PostGamePlaySession(
                userId,
                RockPaperScissor.ROCK
            )
        print(SESSION)
        mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(SESSION)
        }
            .andDo { print() }
            .andExpect { status { isCreated() } }
    }


    @Nested
    @DisplayName("GET /api/session/{userId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUserSession {
        @Test
        fun `should return the stats with the given userId`() {
            val userId = "12345"

            addNewGameBeforeTest(userId)

            mockMvc.get("$baseUrl/$userId").andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.userId") { value(userId) }
            }
        }

        @Test
        fun `should return NOT FOUND if the user does not exist`() {
            val userId = "does_not_exist"

            mockMvc.get("$baseUrl/$userId").andDo { print() }.andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/session")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewSession {
        @Test
        fun `should add the new game session`() {
            val userId = "98765"
            addNewGameBeforeTest(userId)

            mockMvc.get("$baseUrl/$userId").andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.userId") { value(userId) }
            }
        }

        @Test
        fun `should return BAD REQUEST if a userId already exists`() {

            val userId = "112"
            val OTHER_STAT =
                GamePlaySession(
                    userId = userId,
                    computerScore = 2,
                    hasGameEnded = false,
                    userScore = 2,
                    roundResult = null,
                    computerChoice = null
                )

            addNewGameBeforeTest(userId)

            val secondPost =
                mockMvc.post(baseUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(OTHER_STAT)
                }
            secondPost.andDo { print() }.andExpect { status { isBadRequest() } }
        }


        @Nested
        @DisplayName("PATCH /api/session/{userId}")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        inner class PATCHStats {

            @Test
            fun `Should update existing resource`() {
                val userId = "12"
                addNewGameBeforeTest(userId)

                val payload = UpdateGamePlaySession(userChoice = RockPaperScissor.SCISSOR)
                val performPatch =
                    mockMvc.patch("$baseUrl/${userId}") {
                        contentType = MediaType.APPLICATION_JSON
                        content =
                            objectMapper.writeValueAsString(payload)
                    }

                performPatch.andDo { print() }.andExpect {
                    status { isCreated() }
                }
            }
        }
    }
}



