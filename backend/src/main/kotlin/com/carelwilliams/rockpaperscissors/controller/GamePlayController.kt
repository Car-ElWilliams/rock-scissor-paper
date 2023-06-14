package com.carelwilliams.rockpaperscissors.controller

import com.carelwilliams.rockpaperscissors.model.*
import com.carelwilliams.rockpaperscissors.service.GamePlayService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(
    origins = ["http://localhost:4200"],
    methods = [RequestMethod.POST, RequestMethod.PATCH, RequestMethod.GET]
)
@RestController
@RequestMapping("api/session")
class PlayController(private val service: GamePlayService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.stackTrace.toString(), HttpStatus.BAD_REQUEST)

    @GetMapping("/{userId}")
    fun getUserSession(@PathVariable userId: GetGamePlaySession): GamePlaySession = service.getUserSession(userId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postGameSession(@RequestBody payload: PostGamePlaySession): GamePlaySession = service.postUserSession(payload)

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateGameSession(@RequestBody payload: UpdateGamePlaySession, @PathVariable userId: String): GamePlaySession =
        service.updateUserSession(payload, userId)
}
