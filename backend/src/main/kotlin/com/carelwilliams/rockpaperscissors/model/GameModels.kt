package com.carelwilliams.rockpaperscissors.model

import com.carelwilliams.rockpaperscissors.enums.RockPaperScissor

class GameStats(
    val userId: String,
    var mostCommonPick: List<RockPaperScissor>,
    var winPercentage: Double,
    var totalWins: Int,
    var totalLosses: Int,
    var totalPlayTimeInS: Int,
    var historicPicks: List<RockPaperScissor>,
)

class UpdateGameStats(
    val picks: List<RockPaperScissor>,
    val didUserWin: Boolean,
    val playTime: Int,
)

class PostGameStats(
    val userId: String,
    val picks: List<RockPaperScissor>,
    val didUserWin: Boolean,
    val playTime: Int
)

class PublicRequestKeys(
    val userId: String,
    val didUserWin: Boolean,
    val picks: List<RockPaperScissor>,
    val playTime: Int,
)

// -* Game Play Session *- //
class GamePlaySession(
    val userId: String,
    var roundResult: String?,
    var userScore: Int,
    var computerScore: Int,
    var hasGameEnded: Boolean,
    var computerChoice: RockPaperScissor?
)

class UpdateGamePlaySession(val userChoice: RockPaperScissor)
class PostGamePlaySession(val userId: String, val userChoice: RockPaperScissor)
class GetGamePlaySession(val userId: String)

