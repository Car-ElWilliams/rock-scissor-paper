package com.carelwilliams.rockpaperscissors.model

import com.carelwilliams.rockpaperscissors.enums.RockPaperScissor

data class GameStats(
    val userId: String,
    var mostCommonPick: List<RockPaperScissor>,
    var winLoseRatio: Double,
    var totalWins: Int,
    var totalLosses: Int,
    var totalPlayTimeInS: Int,
    var historicPicks: List<RockPaperScissor>,
)

data class UpdateGameStats(
    val picks: List<RockPaperScissor>,
    val didUserWin: Boolean,
    val playTime: Int,
)
