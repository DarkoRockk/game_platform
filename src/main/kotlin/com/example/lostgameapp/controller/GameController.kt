package com.example.lostgameapp.controller

import com.example.lostgameapp.dto.request.ProviderRequestDTO
import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.service.GameProcessService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("open-api-games/v1")
class GameController(
    private val gameProcessService: GameProcessService
) {


    @PostMapping("/games-processor")
    fun processGame(
        @RequestHeader("Sign") sign: String,
        @RequestBody requestRaw: String
    ): ProviderResponseDTO {
        return gameProcessService.handleRequest(sign, requestRaw)
    }
}