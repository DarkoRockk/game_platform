package com.example.lostgameapp.repository

import com.example.lostgameapp.entity.GameEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.*

@Repository
interface GameRepository : JpaRepository<GameEntity, BigInteger> {

    fun findBySessionId(id: UUID): GameEntity?
}