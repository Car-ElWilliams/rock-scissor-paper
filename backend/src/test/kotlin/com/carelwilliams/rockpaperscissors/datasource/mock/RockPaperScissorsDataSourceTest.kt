package com.carelwilliams.rockpaperscissors.datasource.mock

import com.carelwilliams.rockpaperscissors.model.GameStats
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class RockPaperScissorsDataSourceTest {

    private val mockGameStatsDataSource = MockGameStatsDataSource()
    val gameStats = GameStats("1234", listOf(), 2.0, 2, 2, 22, listOf())

    @Test
    fun `Should get current stats`() {
        mockGameStatsDataSource.postStats(gameStats)

        val results = mockGameStatsDataSource.getUserStats(userId = "1234")

        Assertions.assertThat(results).isEqualTo(gameStats)
    }
}
