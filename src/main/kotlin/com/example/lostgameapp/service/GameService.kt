package com.example.lostgameapp.service

import com.example.lostgameapp.entity.GameEntity
import com.example.lostgameapp.entity.UserEntity
import com.example.lostgameapp.repository.GameRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(
    private val gameRepository: GameRepository
) {
    fun createGame(sessionId: UUID, user: UserEntity): GameEntity {
        return gameRepository.save(
            GameEntity(
                sessionId = sessionId,
                user = user
            )
        )
    }

    fun getBySessionId(id: UUID): GameEntity? {
        return gameRepository.findBySessionId(id)
    }
}