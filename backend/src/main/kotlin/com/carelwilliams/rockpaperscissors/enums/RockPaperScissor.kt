package com.carelwilliams.rockpaperscissors.enums

import com.fasterxml.jackson.annotation.JsonFormat


@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class RockPaperScissor(val pick: String) {
    ROCK("Rock"),
    PAPER("Paper"),
    SCISSOR("Scissor");

    private var value: String? = null


}
