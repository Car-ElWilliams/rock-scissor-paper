package com.carelwilliams.rockpaperscissors.datasource.mock

import com.carelwilliams.rockpaperscissors.datasource.GamePlayDataSource
import com.carelwilliams.rockpaperscissors.model.GamePlaySession
import org.springframework.stereotype.Repository

@Repository
class MockGamePlayDataSource : GamePlayDataSource {
    val currentSessions: MutableList<GamePlaySession> = mutableListOf()
    override fun postUserGameSession(session: GamePlaySession): GamePlaySession {
        val concatLists: List<GamePlaySession> = listOf(session) + currentSessions
        val hasDuplicates = noDuplicateSessions(concatLists)

        if (hasDuplicates) {
            throw IllegalArgumentException("UserId: ${session.userId} already exists, can not create new entry")
        }

        currentSessions.add(session)

        return session
    }

    override fun getUserGameSession(userId: String): GamePlaySession =
        currentSessions.firstOrNull() { it.userId == userId }
            ?: throw NoSuchElementException("Could not find user with userId: $userId")
}


fun noDuplicateSessions(items: List<GamePlaySession>): Boolean {
    val uniqueIds = mutableSetOf<String>()

    return items.any { !uniqueIds.add(it.userId) }
}

