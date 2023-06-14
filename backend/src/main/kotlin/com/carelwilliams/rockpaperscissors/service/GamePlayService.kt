@file:Suppress("UnnecessaryVariable")

package com.carelwilliams.rockpaperscissors.service

import com.carelwilliams.rockpaperscissors.datasource.GamePlayDataSource
import com.carelwilliams.rockpaperscissors.enums.RockPaperScissor
import com.carelwilliams.rockpaperscissors.enums.GameResults
import com.carelwilliams.rockpaperscissors.model.GamePlaySession
import com.carelwilliams.rockpaperscissors.model.GetGamePlaySession
import com.carelwilliams.rockpaperscissors.model.PostGamePlaySession
import com.carelwilliams.rockpaperscissors.model.UpdateGamePlaySession
import org.springframework.stereotype.Service


@Service
class GamePlayService(private val dataSource: GamePlayDataSource) {

    fun getUserSession(payload: GetGamePlaySession) = dataSource.getUserGameSession(payload.userId)

    fun postUserSession(payload: PostGamePlaySession): GamePlaySession {
        val computerChoice = randomizeComputerChoice()
        val newGameSession =
            GamePlaySession(
                computerScore = 0,
                userId = payload.userId,
                hasGameEnded = false,
                userScore = 0,
                roundResult = null,
                computerChoice = computerChoice
            )

        val roundWinner = determineWinner(payload.userChoice, computerChoice)
        val mutatedGameSession = updateScore(roundWinner, newGameSession)

        return dataSource.postUserGameSession(mutatedGameSession)
    }

    fun updateUserSession(payload: UpdateGamePlaySession, userId: String): GamePlaySession {
        val currentSession = dataSource.getUserGameSession(userId)

        val computerChoice = randomizeComputerChoice()
        val roundWinner = determineWinner(payload.userChoice, computerChoice)
        val updatedSession = updateScore(roundWinner, currentSession)
        updatedSession.computerChoice = computerChoice

        return updatedSession
    }
}

fun determineWinner(
    userChoice: RockPaperScissor,
    computerChoice: RockPaperScissor
): GameResults {
    val userWon =
        (userChoice === RockPaperScissor.ROCK &&
                computerChoice === RockPaperScissor.SCISSOR) ||
                (userChoice === RockPaperScissor.PAPER &&
                        computerChoice === RockPaperScissor.ROCK) ||
                (userChoice === RockPaperScissor.SCISSOR &&
                        computerChoice === RockPaperScissor.PAPER);

    val tie = userChoice === computerChoice;

    return if (tie) {
        GameResults.TIE
    } else if (userWon) {
        GameResults.USER_WIN;
    } else {
        GameResults.COMPUTER_WIN;
    }
}

fun updateScore(roundWinner: GameResults, gameSession: GamePlaySession): GamePlaySession {

    if (gameSession.hasGameEnded) {
        gameSession.userScore = 0
        gameSession.computerScore = 0
        gameSession.roundResult = null
        gameSession.hasGameEnded = false
    }

    when (roundWinner) {
        GameResults.COMPUTER_WIN -> {
            gameSession.computerScore++
            gameSession.roundResult = GameResults.COMPUTER_WIN.result
        }

        GameResults.USER_WIN -> {
            gameSession.userScore++
            gameSession.roundResult = GameResults.USER_WIN.result
        }

        else -> {
            gameSession.roundResult = GameResults.TIE.result
        }
    }

    val shouldEndGame = gameSession.userScore >= 3 || gameSession.computerScore >= 3
    if (shouldEndGame) {
        gameSession.hasGameEnded = true
    }

    return gameSession
}

fun randomizeComputerChoice(): RockPaperScissor {
    val choices = RockPaperScissor.values()
    val computerChoice = choices[(choices.indices).random()]

    return computerChoice
}

      