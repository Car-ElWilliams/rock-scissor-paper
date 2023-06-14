package com.carelwilliams.rockpaperscissors.service

import com.carelwilliams.rockpaperscissors.datasource.GameStatsDataSource
import com.carelwilliams.rockpaperscissors.enums.RequestType
import com.carelwilliams.rockpaperscissors.enums.RockPaperScissor
import com.carelwilliams.rockpaperscissors.model.GameStats
import com.carelwilliams.rockpaperscissors.model.PublicRequestKeys
import com.carelwilliams.rockpaperscissors.model.PostGameStats
import com.carelwilliams.rockpaperscissors.model.UpdateGameStats
import kotlin.math.round
import org.springframework.stereotype.Service

@Service
class GameStatsService(private val dataSource: GameStatsDataSource) {

    fun getAllStats(): MutableList<GameStats> = dataSource.getAllStats()

    fun getUserStats(userId: String): GameStats = dataSource.getUserStats(userId)

    fun postStats(payload: PostGameStats): GameStats {
        val adjustedPayload =
            PublicRequestKeys(
                payload.userId,
                payload.didUserWin,
                payload.picks,
                payload.playTime
            )

        val result = addInternalKeys(adjustedPayload, RequestType.POST, null)

        return dataSource.postStats(result)
    }

    fun updateStats(payload: UpdateGameStats, userId: String): GameStats {
        val previousStats = dataSource.getUserStats(userId)
        val adjustedPayload =
            PublicRequestKeys(
                userId,
                payload.didUserWin,
                payload.picks,
                payload.playTime
            )

        return addInternalKeys(adjustedPayload, RequestType.PUT, previousStats)
    }
}


fun addInternalKeys(
    body: PublicRequestKeys,
    request: RequestType,
    previousGameStats: GameStats?
): GameStats {
    var result = GameStats(body.userId, listOf(), 0.0, 0, 0, 0, listOf())

    if (request == RequestType.PUT && previousGameStats !== null) {
        result = previousGameStats
    }

    if (body.didUserWin) result.totalWins++ else result.totalLosses++

    val isInfinity = result.totalLosses == 0
    val winPercentage =
        if (isInfinity) 100.0
        else (result.totalWins.toDouble() / (result.totalLosses + result.totalWins) * 100)

    val totalPlayTime = result.totalPlayTimeInS + body.playTime
    val historicPicks = body.picks + result.historicPicks
    val mostCommonPick = findMostCommonPick(historicPicks)

    result.winPercentage = winPercentage
    result.totalPlayTimeInS = totalPlayTime
    result.historicPicks = historicPicks
    result.mostCommonPick = mostCommonPick

    return result
}

fun findMostCommonPick(array: List<RockPaperScissor>): List<RockPaperScissor> {
    val stringOccurrences = array.groupingBy { it }.eachCount()
    val maxOccurrences = stringOccurrences.values.maxOrNull()

    return stringOccurrences.filterValues { it == maxOccurrences }.keys.toList()
}
