package com.example.lostgameapp.service

import com.example.lostgameapp.entity.GameEntity
import com.example.lostgameapp.repository.GameRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val userService: UserService
) {
    @Transactional
    fun getOrCreateGame(sessionId: UUID, email: String): GameEntity {
        var game = gameRepository.findBySessionId(sessionId)
        if (game == null) {
            val user = userService.getOrCreateUser(email)
            game = gameRepository.save(
                GameEntity(
                    sessionId = sessionId,
                    user = user
                )
            )
        }
        return game
    }

    fun getBySessionId(id: UUID): GameEntity? {
        return gameRepository.findBySessionId(id)
    }
}