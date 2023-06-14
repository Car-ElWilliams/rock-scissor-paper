package com.carelwilliams.rockpaperscissors.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class RockPaperScissor() {
    ROCK,
    PAPER,
    SCISSOR
}

enum class RequestType() {
    GET, POST, PUT
}

enum class GameResults(val result: String) {
    TIE("Tie"),
    USER_WIN("You win"),
    COMPUTER_WIN("Computer win"),
}
  