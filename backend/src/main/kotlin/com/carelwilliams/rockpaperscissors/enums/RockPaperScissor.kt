package com.carelwilliams.rockpaperscissors.enums

import com.fasterxml.jackson.annotation.JsonFormat


@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class RockPaperScissor(val pick: String) {
    ROCK("Rock"),
    PAPER("Paper"),
    SCISSOR("Scissor");
}

enum class RequestType(val type: String) {
    GET("Get"), POST("Post"), PUT("Put")
}
