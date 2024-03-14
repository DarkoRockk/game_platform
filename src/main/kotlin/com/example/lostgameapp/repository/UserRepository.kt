package com.example.lostgameapp.repository

import com.example.lostgameapp.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByUsername(username: String): UserEntity?
}