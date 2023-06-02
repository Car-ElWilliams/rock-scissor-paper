package com.carelwilliams.rockpaperscissors.controller

import com.carelwilliams.rockpaperscissors.model.GameStats
import com.carelwilliams.rockpaperscissors.model.UpdateGameStats
import com.carelwilliams.rockpaperscissors.model.PostGameStats
import com.carelwilliams.rockpaperscissors.service.GameStatsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:4200"], methods = [RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET])
@RestController
@RequestMapping("api/stats")
class StatsController(private val service: GameStatsService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.stackTrace.toString(), HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getAllStats(): MutableList<GameStats> = service.getAllStats()

    @GetMapping("/{userId}")
    fun getUserStats(@PathVariable userId: String): GameStats = service.getUserStats(userId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postStats(@RequestBody payload: PostGameStats): GameStats = service.postStats(payload)

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateStats(@RequestBody payload: UpdateGameStats, @PathVariable userId: String): GameStats =
        service.updateStats(payload, userId)
}
