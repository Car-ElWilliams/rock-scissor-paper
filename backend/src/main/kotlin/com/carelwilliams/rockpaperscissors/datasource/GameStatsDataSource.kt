package com.carelwilliams.rockpaperscissors.datasource

import com.carelwilliams.rockpaperscissors.model.GameStats

interface GameStatsDataSource {
    fun getAllStats(): MutableList<GameStats>
    fun getUserStats(userId: String): GameStats
    fun postStats(newStats: GameStats): GameStats

}
