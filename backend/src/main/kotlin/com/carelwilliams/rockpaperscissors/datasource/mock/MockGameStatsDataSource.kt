package com.carelwilliams.rockpaperscissors.datasource.mock

import com.carelwilliams.rockpaperscissors.datasource.GameStatsDataSource
import com.carelwilliams.rockpaperscissors.model.GameStats
import org.springframework.stereotype.Repository

@Repository
class MockGameStatsDataSource : GameStatsDataSource {
    val stats: MutableList<GameStats> = mutableListOf()

    override fun postStats(newStats: GameStats): GameStats {
        val concatLists: List<GameStats> = listOf(newStats) + stats
        val hasDuplicates = hasDuplicateItems(concatLists)

        if (hasDuplicates) {
            throw IllegalArgumentException("UserId: ${newStats.userId} already exists, can not create new entry")
        }

        stats.add(newStats)

        return newStats
    }

    override fun getAllStats(): MutableList<GameStats> = stats

    override fun getUserStats(userId: String): GameStats = stats.firstOrNull() { it.userId == userId }
        ?: throw NoSuchElementException("Could not find user with userId: $userId")
}

fun hasDuplicateItems(items: List<GameStats>): Boolean {
    val uniqueIds = mutableSetOf<String>()

    return items.any { !uniqueIds.add(it.userId) }
}
