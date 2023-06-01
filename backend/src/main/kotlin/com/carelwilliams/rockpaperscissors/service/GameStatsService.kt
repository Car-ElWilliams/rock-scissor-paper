package com.carelwilliams.rockpaperscissors.service

import com.carelwilliams.rockpaperscissors.datasource.GameStatsDataSource
import com.carelwilliams.rockpaperscissors.enums.RockPaperScissor
import com.carelwilliams.rockpaperscissors.model.GameStats
import com.carelwilliams.rockpaperscissors.model.UpdateGameStats
import org.springframework.stereotype.Service
import kotlin.math.round

@Service
class GameStatsService(private val dataSource: GameStatsDataSource) {

    fun getAllStats(): MutableList<GameStats> = dataSource.getAllStats()

    fun getUserStats(userId: String): GameStats {
        return dataSource.getUserStats(userId)
    }

    fun postStats(payload: GameStats): GameStats = dataSource.postStats(payload)

    fun updateStats(payload: UpdateGameStats, userId: String): GameStats {
        val newStats = dataSource.getUserStats(userId)

        if (payload.didUserWin) {
            newStats.totalWins++
        } else {
            newStats.totalLosses++
        }

        val isInfinity = newStats.totalLosses == 0

        val updatedWinLoseRatio = if(isInfinity) newStats.totalWins.toDouble() else (newStats.totalWins.toDouble() / newStats.totalLosses.toDouble()).round(2)
        val updatedTotalPlayTime = newStats.totalPlayTimeInS + payload.playTime
        val updatedHistoricPicks = payload.picks + newStats.historicPicks
        // Should not be included in public response
        val updatedMostCommonPick = findMostCommonPick(updatedHistoricPicks)

        newStats.winLoseRatio = updatedWinLoseRatio
        newStats.totalPlayTimeInS = updatedTotalPlayTime
        newStats.historicPicks = updatedHistoricPicks
        newStats.mostCommonPick = updatedMostCommonPick

        return newStats
    }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }

    return round(this * multiplier) / multiplier
}

fun findMostCommonPick(array: List<RockPaperScissor>): List<RockPaperScissor> {
    val stringOccurrences = array.groupingBy { it }.eachCount()
    val maxOccurrences = stringOccurrences.values.maxOrNull()

    return stringOccurrences.filterValues { it == maxOccurrences }.keys.toList()
}
