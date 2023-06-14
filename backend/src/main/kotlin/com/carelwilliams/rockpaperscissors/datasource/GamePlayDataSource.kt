package com.carelwilliams.rockpaperscissors.datasource

import com.carelwilliams.rockpaperscissors.model.GamePlaySession

interface GamePlayDataSource {
    fun getUserGameSession(userId: String): GamePlaySession
    fun postUserGameSession(session: GamePlaySession): GamePlaySession

}

