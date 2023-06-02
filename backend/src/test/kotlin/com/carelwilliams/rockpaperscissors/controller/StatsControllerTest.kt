package com.carelwilliams.rockpaperscissors.controller

// kt lint-disable no-wildcard-imports
import com.carelwilliams.rockpaperscissors.enums.RockPaperScissor
import com.carelwilliams.rockpaperscissors.model.GameStats
import com.carelwilliams.rockpaperscissors.model.PostGameStats
import com.carelwilliams.rockpaperscissors.model.UpdateGameStats
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
internal class StatsControllerTest
@Autowired
constructor(val mockMvc: MockMvc, val objectMapper: ObjectMapper) {
    val baseUrl = "/api/stats"

    fun postStatsBeforeTest(userId: String) {

        val STATS: PostGameStats =
            PostGameStats(
                userId = userId,
                picks = listOf(RockPaperScissor.ROCK, RockPaperScissor.PAPER),
                didUserWin = true,
                playTime = 30
            )

        mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(STATS)
        }
            .andDo { print() }
            .andExpect { status { isCreated() } }
    }

    @Nested
    @DisplayName("GET /api/stats")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllStats {
        @Test
        fun `Should return all stats`() {
            postStatsBeforeTest("1234")

            mockMvc.get(baseUrl).andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].userId") { value("1234") }
                jsonPath("$[0].totalPlayTimeInS") { value("30") }
            }
        }
    }

    @Nested
    @DisplayName("GET /api/stats/{userId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUserStats {
        @Test
        fun `should return the stats with the given userId`() {
            postStatsBeforeTest("12345")

            val userID = "12345"

            mockMvc.get("$baseUrl/$userID").andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.userId") { value(userID) }
            }
        }

        @Test
        fun `should return NOT FOUND if the user does not exist`() {
            val userId = "does_not_exist"

            mockMvc.get("$baseUrl/$userId").andDo { print() }.andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/stats")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewStat {
        @Test
        fun `should add the new stat`() {
            val userId = "98765"
            postStatsBeforeTest(userId)

            mockMvc.get("$baseUrl/$userId").andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.userId") { value(userId) }
            }

            @Test
            fun `should return BAD REQUEST if a userId already exists`() {

                val OTHER_STAT =
                    GameStats(
                        userId = "13233",
                        listOf(),
                        winPercentage = 3.15,
                        totalWins = 22,
                        historicPicks = listOf(),
                        totalLosses = 23,
                        totalPlayTimeInS = 100
                    )

                mockMvc
                    .post(baseUrl) {
                        contentType = MediaType.APPLICATION_JSON
                        content = objectMapper.writeValueAsString(OTHER_STAT)
                    }
                    .andDo { print() }
                    .andExpect { status { isCreated() } }

                val secondPost =
                    mockMvc.post(baseUrl) {
                        contentType = MediaType.APPLICATION_JSON
                        content = objectMapper.writeValueAsString(OTHER_STAT)
                    }
                secondPost.andDo { print() }.andExpect { status { isBadRequest() } }
            }
        }

        @Nested
        @DisplayName("PUT /api/stats/{userId}")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        inner class PUTStats {

            @Test
            fun `Should update existing resource`() {
                val userId = "12"
                postStatsBeforeTest(userId)

                val updateStat =
                    UpdateGameStats(
                        picks = listOf(RockPaperScissor.SCISSOR),
                        didUserWin = true,
                        playTime = 600,
                    )

                val performPut =
                    mockMvc.put("$baseUrl/${userId}") {
                        contentType = MediaType.APPLICATION_JSON
                        content = objectMapper.writeValueAsString(updateStat)
                    }

                performPut.andDo { print() }.andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        /*json(objectMapper.writeValueAsString(newStat))*/
                    }
                }
            }
        }
    }
}
