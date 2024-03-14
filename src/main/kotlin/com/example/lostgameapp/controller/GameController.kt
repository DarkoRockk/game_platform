package com.example.lostgameapp.controller

import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.service.GameProcessService
import org.springframework.web.bind.annotation.*

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