package com.carelwilliams.rockpaperscissors.service

import com.carelwilliams.rockpaperscissors.datasource.GameStatsDataSource
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class GameStatisticsServiceTest {
    private val dataSource: GameStatsDataSource = mockk(relaxed = true)
    private val gameStatisticsService = GameStatsService(dataSource)

    @Test
    fun `Should call it's data source to retrieve all stats just once`() {
        gameStatisticsService.getAllStats()

        verify(exactly = 1) { dataSource.getAllStats() }
    }
}
